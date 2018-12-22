package com.ebeijia.zl.web.oms.company.service.impl;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.facade.account.req.AccountTransferReqVo;
import com.ebeijia.zl.facade.account.req.AccountTxnVo;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
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
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderListService;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderService;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.ebeijia.zl.web.oms.company.service.CompanyService;
import com.ebeijia.zl.web.oms.utils.OrderConstants;
import org.springframework.ui.ModelMap;

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
			companyInf.setIsOpen(IsOpenEnum.ISOPEN_TRUE.getCode());
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
	public ModelMap addCompanyTransferCommit(HttpServletRequest req) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);

		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		try {
			CompanyInf company = companyInfFacade.getCompanyInfById(companyId);
			if (company == null || company.getIsOpen().equals(IsOpenEnum.ISOPEN_FALSE.getCode())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "收款失败，该企业不存在或未开户");
				return resultMap;
			}

			InaccountOrder order = inaccountOrderService.getById(orderId);
			List<InaccountOrderDetail> orderDetailList = inaccountOrderDetailService.getInaccountOrderDetailByOrderId(orderId);
			if (order == null || orderDetailList == null || orderDetailList.size() < 1) {
				logger.error("## 查询企业{}收款订单{}信息为空", companyId, orderId);
				resultMap.addAttribute("status", Boolean.FALSE);
				resultMap.addAttribute("status", "暂无可收款订单，请重新查看订单信息");
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
			reqVo.setDmsRelatedKey(orderId);
			reqVo.setUserChnlId(companyId);
			reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
			reqVo.setTransDesc(order.getRemarks());
			reqVo.setTransNumber(1);

			BaseResult result = new BaseResult();
			try {
				result = accountTransactionFacade.executeTransfer(reqVo);
			} catch (Exception e) {
				logger.error("## 远程调用转账接口异常", e);
				resultMap.addAttribute("status", Boolean.FALSE);
				resultMap.addAttribute("msg", "网络异常，请稍后再试");
				return resultMap;
			}
			System.out.println("============================================================"+JSONArray.toJSONString(result));
			logger.error("远程调用转账接口返回参数--->{}", JSONArray.toJSONString(result));
			if (result != null && Constants.SUCCESS_CODE.toString().equals(result.getCode())) {
				order.setPlatformReceiverCheck(ReceiverEnum.RECEIVER_TRUE.getCode());
			}

			if (!inaccountOrderService.updateById(order)) {
				logger.error("## 更新平台{}收款状态{}失败", companyId, order.getPlatformReceiverCheck());
				resultMap.addAttribute("status", Boolean.FALSE);
				resultMap.addAttribute("msg", "系统异常，请联系管理员");
				return resultMap;
			}

		} catch (Exception e) {
			logger.error(" ## 企业平台{}收款异常", companyId, e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "企业平台收款失败，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	@Override
	public ModelMap updateCompanyTransferStat(HttpServletRequest req) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);

		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		try {
			InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderId);
			order.setCompanyReceiverCheck(ReceiverEnum.RECEIVER_TRUE.getCode());
			if (!inaccountOrderService.updateById(order)) {
				resultMap.addAttribute("status", Boolean.FALSE);
				resultMap.addAttribute("status", "网络异常，请稍后再试");
			}
		} catch (Exception e) {
			logger.error("## 企业{}收款异常", companyId, e);
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("status", "系统异常，请联系管理员");
		}
		return resultMap;
	}


}
