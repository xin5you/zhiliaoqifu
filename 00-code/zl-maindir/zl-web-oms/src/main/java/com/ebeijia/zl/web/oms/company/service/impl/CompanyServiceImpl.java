package com.ebeijia.zl.web.oms.company.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.facade.account.req.AccountTransferReqVo;
import com.ebeijia.zl.facade.account.req.AccountTxnVo;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyBillingTypeInf;
import com.ebeijia.zl.web.oms.common.util.OrderConstants;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrderDetail;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderDetailService;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderService;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.ebeijia.zl.web.oms.company.service.CompanyService;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService{
	
	Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

	@Autowired
	private CompanyInfFacade companyInfFacade;
	
	@Autowired
	private BatchOrderService batchOrderService;
	
	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;

	@Autowired
	private InaccountOrderService inaccountOrderService;

	@Autowired
	private InaccountOrderDetailService inaccountOrderDetailService;

	@Autowired
	private AccountTransactionFacade accountTransactionFacade;

	@Autowired
	private AccountQueryFacade accountQueryFacade;

	@Override
	public int openAccountCompany(HttpServletRequest req) {
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		CompanyInf companyInf = null;
		try {
			companyInf = companyInfFacade.getCompanyInfById(companyId);
			if (StringUtil.isNullOrEmpty(companyInf)) {
				logger.error("## 查询企业信息失败，companyId--->{}", companyId);
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 查询企业{}信息失败", companyId);
			return 0;
		}
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		BatchOrderList orderList = new BatchOrderList();
		orderList.setUserName(companyInf.getName());
		orderList.setPhoneNo(companyInf.getPhoneNo());
		orderList.setAccountType(UserType.TYPE200.getCode());
		
		LinkedList<BatchOrderList> batchOrderList = new LinkedList<>();
		batchOrderList.add(orderList);
		
		int orderResult = batchOrderService.addBatchOrderAndOrderList(req, batchOrderList, TransCode.MB80.getCode(), UserType.TYPE200.getCode());
		if (orderResult < 0) {
			logger.error("## 新增企业开户订单信息失败");
			return 0;
		}
		
		String orderId = jedisClusterUtils.get(OrderConstants.companyOrderIdSession);
		try {
			int i = batchOrderService.batchOpenAccountITF(orderId, user, BatchOrderStat.BatchOrderStat_30.getCode());
			if (i < 1) {
				logger.error("## 调用开户接口失败");
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 调用企业开户接口失败");
			return 0;
		}
				
		jedisClusterUtils.del(OrderConstants.companyOrderIdSession);
		
		try {
			companyInf.setIsOpen(IsOpenAccountEnum.ISOPEN_TRUE.getCode());
			if (!companyInfFacade.updateCompanyInf(companyInf)) {
				logger.error("## 更新企业{}开户成功状态失败", companyId);
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 更新企业{}开户状态失败", companyId);
		}
		return 1;
	}

	@Override
	public Map<String, Object> addCompanyTransferCommit(HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);

		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		try {
			CompanyInf company = companyInfFacade.getCompanyInfById(companyId);
			if (company == null || company.getIsOpen().equals(IsOpenAccountEnum.ISOPEN_FALSE.getCode())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "平台打款失败，该平台不存在或未开户");
				return resultMap;
			}

			InaccountOrder order = inaccountOrderService.getById(orderId);
			List<InaccountOrderDetail> orderDetailList = inaccountOrderDetailService.getInaccountOrderDetailByOrderId(orderId);
			if (order == null || orderDetailList == null || orderDetailList.size() < 1) {
				logger.error("## 查询平台{}打款订单{}信息为空", companyId, orderId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("status", "暂无可打款订单，请重新查看订单信息");
				return resultMap;
			}

			AccountTransferReqVo reqVo = new AccountTransferReqVo();
			reqVo.setTransAmt(order.getCompanyInSumAmt());
			reqVo.setUploadAmt(order.getCompanyInSumAmt());
			reqVo.setTfrInUserId(order.getCompanyId());
			reqVo.setTfrOutUserId(companyId);

			List<AccountTxnVo> transList = new ArrayList<>();
			Set<String> bIds = new TreeSet<>();
			for (InaccountOrderDetail orderDetail :orderDetailList ) {
				AccountTxnVo txnVo = new AccountTxnVo();
				txnVo.setTxnBId(orderDetail.getBId());
				txnVo.setTxnAmt(orderDetail.getCompanyInAmt());
				txnVo.setUpLoadAmt(orderDetail.getCompanyInAmt());
				transList.add(txnVo);
				bIds.add(orderDetail.getBId());
			}

			reqVo.setTransList(transList);
			reqVo.setTransId(TransCode.MB40.getCode());
			reqVo.setTransChnl(TransChnl.CHANNEL0.toString());
			reqVo.setUserId(companyId);
			reqVo.setbIds(bIds);
			reqVo.setUserType(UserType.TYPE200.getCode());
			reqVo.setDmsRelatedKey(order.getTfrCompanyOrderId());
			reqVo.setUserChnlId(companyId);
			reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
			reqVo.setTransDesc(order.getRemarks());
			reqVo.setTransNumber(1);
			logger.info("平台转账，远程调用转账接口请求参数--->{}", JSONArray.toJSONString(reqVo));
			BaseResult result = new BaseResult();
			try {
				result = accountTransactionFacade.executeTransfer(reqVo);
			} catch (Exception e) {
				logger.error("## 远程调用转账接口异常", e);
			}
			logger.info("平台转账，远程调用转账接口返回参数--->{}", JSONArray.toJSONString(result));
			try {
				if (StringUtil.isNullOrEmpty(result.getCode())) {
					logger.info("平台转账，远程调用查询接口请求参数--->dmsRelatedKe{},transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
					result = accountTransactionFacade.executeQuery(reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
					logger.info("平台转账，远程调用查询接口返回参数--->{}", JSONArray.toJSONString(result));
				}
			} catch (Exception e) {
				logger.error("## 平台转账，远程调用查询接口出错,入参--->dmsRelatedKey{},transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl(), e);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "网络异常，请稍后再试");
			}

			if (result != null && Constants.SUCCESS_CODE.toString().equals(result.getCode())) {
				order.setCompanyReceiverCheck(ReceiverEnum.RECEIVER_TRUE.getCode());
			}

			if (!inaccountOrderService.saveOrUpdate(order)) {
				logger.error("## 更新企业{}收款状态{}失败", order.getCompanyId(), order.getPlatformReceiverCheck());
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "系统异常，请联系管理员");
				return resultMap;
			}

		} catch (Exception e) {
			logger.error(" ## 平台{}打款异常", companyId, e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "平台打款失败，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> updateCompanyTransferStat(HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);

		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		try {
			InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderId);
			order.setCompanyReceiverCheck(ReceiverEnum.RECEIVER_TRUE.getCode());
			if (!inaccountOrderService.saveOrUpdate(order)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("status", "网络异常，请稍后再试");
			}
		} catch (Exception e) {
			logger.error("## 企业{}收款异常", companyId, e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("status", "系统异常，请联系管理员");
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> editCompanyInAmtCommit(HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);

		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);

		String orderListId = StringUtil.nullToString(req.getParameter("orderListId"));
		String companyInAmt = StringUtil.nullToString(req.getParameter("companyInAmt"));
		if (StringUtil.isNullOrEmpty(companyInAmt)) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "请输入企业收款金额");
			return resultMap;
		}

		//查询当前编辑收款订单明细信息
		InaccountOrderDetail orderDetail = inaccountOrderDetailService.getById(orderListId);
		if (orderDetail == null) {
			logger.error("## 查询企业收款订单明细{}为空", orderListId);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "查询订单明细信息异常");
			return resultMap;
		}

		//查询收款订单信息
		InaccountOrder order = inaccountOrderService.getById(orderDetail.getOrderId());
		if (orderDetail == null) {
			logger.error("## 查询企业收款订单{}为空", orderDetail.getOrderId());
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "查询订单信息异常");
			return resultMap;
		}

		//查询订单所有明细信息
		List<InaccountOrderDetail> orderDetail1List = inaccountOrderDetailService.getInaccountOrderDetailByOrderId(orderDetail.getOrderId());
		if (orderDetail1List == null && orderDetail1List.size() < 1) {
			logger.error("## 查询企业收款所有订单{}明细信息为空", orderDetail.getOrderId());
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "查询订单明细信息异常");
			return resultMap;
		}

		//过滤掉当前修改的企业收款金额记录
		orderDetail1List = 	orderDetail1List.stream().filter(t -> !orderListId.equals(t.getOrderListId())).collect(Collectors.toList());
		//计算出企业所有收款金额总和
		BigDecimal companyInAmtSum = orderDetail1List.stream().map(InaccountOrderDetail::getCompanyInAmt).reduce(BigDecimal.valueOf(BigDecimal.ROUND_UP, 0), BigDecimal::add);
		companyInAmtSum = companyInAmtSum.add(new BigDecimal(NumberUtils.RMBYuanToCent(companyInAmt))).setScale(0, BigDecimal.ROUND_UP);
		if (companyInAmtSum.compareTo(order.getPlatformInSumAmt()) == 1) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "企业收款金额不合法，请重新输入");
			return resultMap;
		}


		orderDetail.setCompanyInAmt(new BigDecimal(NumberUtils.RMBYuanToCent(companyInAmt)));
		orderDetail.setUpdateTime(System.currentTimeMillis());
		orderDetail.setUpdateUser(user.getId());
		orderDetail.setLockVersion(orderDetail.getLockVersion() + 1);

		order.setCompanyInSumAmt(companyInAmtSum);
		order.setUpdateTime(System.currentTimeMillis());
		order.setUpdateUser(user.getId());
		order.setLockVersion(order.getLockVersion() + 1);

		if (!inaccountOrderDetailService.updateById(orderDetail)) {
			logger.error("## 编辑企业收款金额{}订单明细{}失败", companyInAmt, orderDetail.getOrderListId());
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑企业收款金额失败，请稍后再试");
			return resultMap;
		}

		if (!inaccountOrderService.updateById(order)) {
			logger.error("## 编辑企业收款金额{}订单{}失败", new BigDecimal(NumberUtils.RMBCentToYuan(companyInAmtSum.toString())).toString(), order.getOrderId());
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑企业收款金额失败，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	/*public static void main(String[] args) {
		BigDecimal a = new BigDecimal("2.00");
		BigDecimal b = new BigDecimal(1);
		if(a.compareTo(b) == 1){
			System.out.print("aaa");
		}
	}*/
}
