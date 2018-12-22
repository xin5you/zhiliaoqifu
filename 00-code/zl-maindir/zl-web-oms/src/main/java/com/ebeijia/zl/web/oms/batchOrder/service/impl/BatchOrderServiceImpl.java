package com.ebeijia.zl.web.oms.batchOrder.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.enums.TransChnl;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;
import com.ebeijia.zl.facade.account.req.AccountRechargeReqVo;
import com.ebeijia.zl.facade.account.req.AccountTransferReqVo;
import com.ebeijia.zl.facade.account.req.AccountTxnVo;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.ebeijia.zl.web.oms.batchOrder.mapper.BatchOrderListMapper;
import com.ebeijia.zl.web.oms.batchOrder.mapper.BatchOrderMapper;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrder;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderService;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.ebeijia.zl.web.oms.utils.OrderConstants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("batchOrderService")
public class BatchOrderServiceImpl extends ServiceImpl<BatchOrderMapper, BatchOrder> implements BatchOrderService {

	Logger logger = LoggerFactory.getLogger(BatchOrderServiceImpl.class);
	
	@Autowired
	private BatchOrderListMapper batchOrderListMapper;
	
	@Autowired
	private BatchOrderMapper batchOrderMapper;
	
	@Autowired
	private AccountManageFacade accountManageFacade;
	
	@Autowired
	private AccountTransactionFacade accountTransactionFacade;
	
	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;

	@Override
	public List<BatchOrder> getBatchOrderList(BatchOrder order) {
		return batchOrderMapper.getBatchOrderList(order);
	}

	@Override
	public PageInfo<BatchOrder> getBatchOrderPage(int startNum, int pageSize, BatchOrder order,
			HttpServletRequest req) {
		PageHelper.startPage(startNum, pageSize);
		order.setOrderId(StringUtil.nullToString(req.getParameter("orderId")));//订单号
		order.setOrderName(StringUtil.nullToString(req.getParameter("orderName")));//订单名称
		order.setCompanyId(StringUtil.nullToString(req.getParameter("companyId")));
		order.setOrderStat(StringUtil.nullToString(req.getParameter("orderStat")));//订单状态
		/*String startTime = StringUtil.nullToString(req.getParameter("startTime"));
		String endTime = StringUtil.nullToString(req.getParameter("endTime"));
		if (!StringUtil.isNullOrEmpty(startTime)) {
			long startTimeLong = DateUtil.DateTimeToLongTime(startTime);
			order.setStartTime(String.valueOf(startTimeLong));//开始时间
		}
		if (!StringUtil.isNullOrEmpty(endTime)) {
			long endTimeLong = DateUtil.DateTimeToLongTime(endTime);
			order.setEndTime(String.valueOf(endTimeLong));//结束时间
		}*/
		List<BatchOrder> list = batchOrderMapper.getBatchOrderList(order);
		PageInfo<BatchOrder> page = null;
		if(list != null){
			list.forEach(batchOrder ->{
				batchOrder.setOrderStat(BatchOrderStat.findStat(batchOrder.getOrderStat()));
				if (!StringUtil.isNullOrEmpty(batchOrder.getSumAmount())) {
					batchOrder.setSumAmount(NumberUtils.RMBCentToYuan(batchOrder.getSumAmount()));
				} else {
					batchOrder.setSumAmount(NumberUtils.RMBCentToYuan(0));
				}
				
			});
			page = new PageInfo<BatchOrder>(list);
		}
		return page;
	}

	@Override
	public int addBatchOrderAndOrderList(HttpServletRequest req, LinkedList<BatchOrderList> personInfList,
			String orderType, String accountType) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		BatchOrder order = new BatchOrder();
		order.setOrderId(IdUtil.getNextId());
		order.setOrderName(StringUtil.nullToString(req.getParameter("orderName")));
		order.setCompanyId(StringUtil.nullToString(req.getParameter("companyId")));
		if (TransCode.MB80.getCode().equals(orderType)) {
			order.setOrderStat(BatchOrderStat.BatchOrderStat_30.getCode());
		} else {
			order.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
		}
		order.setOrderType(orderType);
		order.setOrderDate(System.currentTimeMillis());
		order.setCreateUser(user.getId().toString());
		order.setCreateTime(System.currentTimeMillis());
		order.setUpdateUser(user.getId().toString());
		order.setUpdateTime(System.currentTimeMillis());
		
		String [] billingTypes = req.getParameterValues("billingTypes[]");
		String billingType = StringUtil.nullToString(req.getParameter("billingType"));

		List<BatchOrderList> list = new ArrayList<>();
		if (TransCode.CW80.getCode().equals(orderType)) {
			for (SpecAccountTypeEnum t : SpecAccountTypeEnum.values()) {
				for (BatchOrderList orderList : personInfList) {
					BatchOrderList o = new BatchOrderList();
					o.setOrderListId(IdUtil.getNextId());
					o.setOrderId(order.getOrderId());
					o.setUserName(orderList.getUserName());
					o.setPhoneNo(orderList.getPhoneNo());
					o.setUserCardNo(orderList.getUserCardNo());
					o.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
					o.setAccountType(accountType);
					o.setBizType(t.getbId());
					o.setCreateUser(order.getCreateUser());
					o.setUpdateUser(order.getUpdateUser());
					o.setCreateTime(System.currentTimeMillis());
					o.setUpdateTime(System.currentTimeMillis());
					list.add(o);
				}
			}
		} else if (TransCode.MB50.getCode().equals(orderType)) {
			for (BatchOrderList orderList : personInfList) {
				BatchOrderList o = new BatchOrderList();
				o.setOrderListId(IdUtil.getNextId());
				o.setOrderId(order.getOrderId());
				o.setUserName(orderList.getUserName());
				o.setPhoneNo(orderList.getPhoneNo());
				o.setUserCardNo(orderList.getUserCardNo());
				o.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
				o.setAccountType(accountType);
				o.setBizType(billingType);
				o.setAmount(new BigDecimal(NumberUtils.RMBYuanToCent(orderList.getAmount().toString())));
				o.setTfrInBid(billingType);
				o.setTfrInId(orderList.getPhoneNo());
				o.setTfrOutBid(billingType);
				o.setTfrOutId(order.getCompanyId());
				o.setCreateUser(order.getCreateUser());
				o.setUpdateUser(order.getUpdateUser());
				o.setCreateTime(System.currentTimeMillis());
				o.setUpdateTime(System.currentTimeMillis());
				list.add(o);
			}
		} else if (TransCode.MB80.getCode().equals(orderType)) {
			for (SpecAccountTypeEnum t : SpecAccountTypeEnum.values()) {
				if (!SpecAccountTypeEnum.A01.getbId().equals(t.getbId())) {
					for (BatchOrderList orderList : personInfList) {
						BatchOrderList o = new BatchOrderList();
						o.setOrderListId(IdUtil.getNextId());
						o.setOrderId(order.getOrderId());
						o.setUserName(orderList.getUserName());
						o.setPhoneNo(orderList.getPhoneNo());
						o.setUserCardNo(orderList.getUserCardNo());
						o.setOrderStat(BatchOrderStat.BatchOrderStat_30.getCode());
						o.setAccountType(accountType);
						o.setBizType(t.getbId());
						o.setCreateUser(order.getCreateUser());
						o.setUpdateUser(order.getUpdateUser());
						o.setCreateTime(System.currentTimeMillis());
						o.setUpdateTime(System.currentTimeMillis());
						list.add(o);
					}
				}
			}
		}
		
		int orderResult = 0;
		int orderListResult = 0;
		try {
			orderResult = batchOrderMapper.addBatchOrder(order);//添加订单数据
			orderListResult = batchOrderListMapper.addBatchOrderListByList(list);//添加订单明细
			//添加订单明细
			if (orderResult < 1 && orderListResult < 1) {
				logger.error("添加批量订单及订单明细失败batchOrder---->>{},batchOrderList----->>{}", JSONArray.toJSONString(order), JSONArray.toJSONString(list));
				return 0;
			}
		} catch (Exception e) {
			logger.error("新增批量订单数据出错batchOrder---->>{},batchOrderList----->>{}", JSONArray.toJSONString(order), JSONArray.toJSONString(list), e);
		}
		
		if (TransCode.MB80.getCode().equals(orderType)) {
			if (UserType.TYPE200.getCode().equals(accountType)) {
				jedisClusterUtils.setex(OrderConstants.companyOrderIdSession, order.getOrderId(), 1800);
			} else if (UserType.TYPE300.getCode().equals(accountType)) {
				jedisClusterUtils.setex(OrderConstants.providerOrderIdSession, order.getOrderId(), 1800);
			} else if (UserType.TYPE400.getCode().equals(accountType)) {
				jedisClusterUtils.setex(OrderConstants.retailChnlOrderIdSession, order.getOrderId(), 1800);
			}
		}
		return 1;
	}

	@Override
	public BatchOrder getBatchOrderById(String orderId) {
		return batchOrderMapper.getBatchOrderById(orderId);
	}

	@Override
	public int deleteOpenAccountOrRechargeCommit(String orderId, User user) {
		BatchOrder order = batchOrderMapper.getBatchOrderById(orderId);
		order.setOrderStat(BatchOrderStat.BatchOrderStat_20.getCode());
		order.setUpdateTime(System.currentTimeMillis());
		order.setUpdateUser(user.getId());
		
		List<BatchOrderList> orderList = batchOrderListMapper.getBatchOrderListByOrderId(orderId);
		if (orderList != null && orderList.size() > 0) {
			for (BatchOrderList list : orderList) {
				list.setOrderStat(BatchOrderStat.BatchOrderStat_20.getCode());
				list.setUpdateTime(System.currentTimeMillis());
				list.setUpdateUser(user.getId());
			}
		}
		
		int i = 0;
		int j = 0;
		i = batchOrderMapper.updateBatchOrder(order);
		if (i < 1) {
			logger.error("## 批量删除订单信息失败，orderId--->{}", orderId);
			return 0;
		}

		if (orderList.size() > 1) {
			j = batchOrderListMapper.updateBatchOrderListByList(orderList);
		} else if (orderList.size() == 1) {
			j = batchOrderListMapper.updateBatchOrderList(orderList.get(0));
		}
		if (j < 1 || j != orderList.size()) {
			logger.error("## 批量删除订单明细失败，orderId--->{}", orderId);
			return 0;
		}	
		return 1;
	}

	@Override
	public int updateBatchOrderAndOrderListByOrderStat(String orderId, String orderStat, User user) {
		BatchOrder order = batchOrderMapper.getBatchOrderById(orderId);
		List<BatchOrderList> orderList = batchOrderListMapper.getBatchOrderListByOrderId(orderId);
		
		order.setOrderStat(orderStat);
		order.setUpdateUser(user.getId());
		order.setUpdateTime(System.currentTimeMillis());
		
		for (BatchOrderList oList : orderList) {
			oList.setOrderStat(orderStat);
			oList.setUpdateUser(user.getId());
			oList.setUpdateTime(System.currentTimeMillis());
		}
		
		int orderResult = batchOrderMapper.updateBatchOrder(order);
		if (orderResult < 1) {
			logger.error("## 更新订单信息失败,orderId--->{}", orderId);
			return 0;
		}
		int orderListResult = batchOrderListMapper.updateBatchOrderListByList(orderList);
		if (orderListResult < 1) {
			logger.error("## 更新订单明细信息失败,orderId--->{}", orderId);
			return 0;
		}
		return 1;
	}

	@Override
	public int batchOpenAccountITF(String orderId, User user, String orderStat) {
		if (StringUtil.isNullOrEmpty(orderId)) {
			logger.error("## 批量开户订单号为空");
			return 0;
		}
		BatchOrder order = batchOrderMapper.getBatchOrderById(orderId);
		
		BatchOrderList orderList = new BatchOrderList();
		orderList.setOrderId(orderId);
		orderList.setOrderStat(orderStat);
		List<BatchOrderList> batchOrderList = new ArrayList<>();
		try {
			batchOrderList = batchOrderListMapper.getBatchOrderListByOrderStat(orderList);
			if (batchOrderList == null || batchOrderList.size() < 1) {
				logger.error("## 批量开户名单为空");
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 查询开户订单信息异常");
			return 0;
		}
		List<AccountOpenReqVo> reqVoList = new ArrayList<>();
		Set<String> bIds = new TreeSet<>();
		String accountType = null;
		for (BatchOrderList batchOrder : batchOrderList) {
			bIds.add(batchOrder.getBizType());
			AccountOpenReqVo reqVo = new AccountOpenReqVo();
			reqVo.setUserName(batchOrder.getUserName());
			reqVo.setMobilePhone(batchOrder.getPhoneNo());
			reqVo.setIcardNo(batchOrder.getUserCardNo());
			reqVo.setCompanyId(order.getCompanyId());
			if (batchOrder.getAccountType().equals(UserType.TYPE100.getCode())) {
				reqVo.setTransId(TransCode.CW80.getCode());
			} else {
				reqVo.setTransId(TransCode.MB80.getCode());
				reqVo.setUserId(order.getCompanyId());
				reqVo.setUserChnlId(order.getCompanyId());
			}
			reqVo.setTransChnl(TransChnl.CHANNEL0.toString());
			reqVo.setUserType(batchOrder.getAccountType());
			reqVo.setDmsRelatedKey(batchOrder.getOrderListId());
			reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
			reqVoList.add(reqVo);
			accountType = batchOrder.getAccountType();
		}
		
		if (UserType.TYPE100.getCode().equals(accountType)) {
			reqVoList = reqVoList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(AccountOpenReqVo::getMobilePhone))), ArrayList::new));
		} else {
			reqVoList = reqVoList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(AccountOpenReqVo::getUserName))), ArrayList::new));
		}
		
		int openAccount = 0;
		BaseResult result = null;
		List<BatchOrderList> batchOrderLists = new ArrayList<>();
		for (AccountOpenReqVo req : reqVoList) {
			req.setbIds(bIds);
			try {
				result = accountManageFacade.createAccount(req);
			} catch (Exception e) {
				logger.error("## 远程调用开户接口出错,请求参数{}", JSONArray.toJSONString(req), e);
				return 0;
			}
			BatchOrderList orderLists = new BatchOrderList();
			orderLists.setPhoneNo(req.getMobilePhone());
			batchOrderLists = batchOrderListMapper.getBatchOrderListByOrder(orderLists);
			for (BatchOrderList oLists : batchOrderLists) {
				if (!StringUtil.isNullOrEmpty(result) && result.getCode().equals(Constants.SUCCESS_CODE.toString())) {
					oLists.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
				} else {
					oLists.setOrderStat(BatchOrderStat.BatchOrderStat_99.getCode());
				}
			}
			openAccount = batchOrderListMapper.updateBatchOrderListByList(batchOrderLists);

		}
		if (!StringUtil.isNullOrEmpty(result) && result.getCode().equals(Constants.SUCCESS_CODE.toString())) {
			if (openAccount > 0) {
				order.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
			} else {
				order.setOrderStat(BatchOrderStat.BatchOrderStat_40.getCode());
			}
		} else {
			order.setOrderStat(BatchOrderStat.BatchOrderStat_99.getCode());
		}
		int i = batchOrderMapper.updateBatchOrder(order);
		if (i < 1) {
			logger.error("## 更新开户后的订单信息失败，orderId--->{}", orderId);
			return 0;
		}
		
		return 1;
	}

	@Override
	public int batchRechargeITF(String orderId, User user, String orderStat) {
		BatchOrder order = batchOrderMapper.getBatchOrderById(orderId);
		
		BatchOrderList orderList = new BatchOrderList();
		orderList.setOrderId(orderId);
		orderList.setOrderStat(orderStat);
		List<BatchOrderList> batchOrderList = batchOrderListMapper.getBatchOrderListByOrderStat(orderList);
		if (batchOrderList == null) {
			logger.error("## 批量充值名单为空");
			return 0;
		}
		
		if (batchOrderList != null && batchOrderList.size() > 0) {
			List<AccountRechargeReqVo> reqVoList = new ArrayList<>();
			for (BatchOrderList batchOrder : batchOrderList) {
				AccountRechargeReqVo reqVo = new AccountRechargeReqVo();
				reqVo.setMobilePhone(batchOrder.getPhoneNo());
				reqVo.setFromCompanyId(batchOrder.getCompanyId());
				reqVo.setPriBId(batchOrder.getBizType());
				reqVo.setTransAmt(batchOrder.getAmount());
				reqVo.setUploadAmt(batchOrder.getAmount());

				AccountTxnVo txnVo = new AccountTxnVo();
				txnVo.setTxnBId(batchOrder.getBizType());
				txnVo.setTxnAmt(batchOrder.getAmount());
				List<AccountTxnVo> transList = new ArrayList<>();
				transList.add(txnVo);

				reqVo.setTransList(transList);
				if (batchOrder.getAccountType().equals(UserType.TYPE100.getCode())) {
					reqVo.setTransId(TransCode.MB50.getCode());
				} else {
					reqVo.setTransId(TransCode.MB20.getCode());
					reqVo.setUserId(batchOrder.getCompanyId());
				}
				
				reqVo.setTransChnl(TransChnl.CHANNEL0.toString());

				Set<String> bIds = new TreeSet<>();
				bIds.add(batchOrder.getBizType());
				reqVo.setbIds(bIds);

				reqVo.setUserType(batchOrder.getAccountType());
				reqVo.setDmsRelatedKey(batchOrder.getOrderListId());
				reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
				reqVoList.add(reqVo);
			}
			BaseResult result = new BaseResult();
			if (batchOrderList.size() > 1) {
				try {
					result = accountTransactionFacade.executeRecharge(reqVoList);
				} catch (Exception e) {
					logger.error("## 远程调用批量充值接口出错{}", reqVoList, e);
				}
			} else {
				try {
					result = accountTransactionFacade.executeRecharge(reqVoList.get(0));
				} catch (Exception e) {
					logger.error("## 远程调用充值接口出错{}", reqVoList.get(0), e);
				}
			}
			for (BatchOrderList oList : batchOrderList) {
				if (!StringUtil.isNullOrEmpty(result) && result.getCode().equals(Constants.SUCCESS_CODE.toString())) {
					oList.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
					order.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
				} else {
					oList.setOrderStat(BatchOrderStat.BatchOrderStat_99.getCode());
					order.setOrderStat(BatchOrderStat.BatchOrderStat_99.getCode());
				}
			}
			int orderListResult = batchOrderListMapper.updateBatchOrderListByList(batchOrderList);
			int orderRsult = batchOrderMapper.updateBatchOrder(order);
			if (orderListResult < 0 || orderRsult < 0) {
				logger.error("## 更新批量充值后的订单状态信息失败，batchOrder--->{},batchOrderList--->{}", JSONArray.toJSONString(order), JSONArray.toJSONString(batchOrderList));
				return 0;
			}
		}
		return 1;
	}

	@Override
	public int addBatchOrder(BatchOrder order) {
		return batchOrderMapper.addBatchOrder(order);
	}

	@Override
	public BatchOrder getBatchOrderByOrderId(String orderId) {
		return batchOrderMapper.getBatchOrderByOrderId(orderId);
	}

	@Override
	public int batchTransferAccountITF(String orderId, User user, String orderStat) {
		BatchOrder order = batchOrderMapper.getBatchOrderById(orderId);
		
		BatchOrderList orderList = new BatchOrderList();
		orderList.setOrderId(orderId);
		orderList.setOrderStat(orderStat);
		List<BatchOrderList> batchOrderList = batchOrderListMapper.getBatchOrderListByOrderStat(orderList);
		if (batchOrderList == null) {
			logger.error("## 批量充值名单为空");
			return 0;
		}
		
		if (batchOrderList != null && batchOrderList.size() > 0) {
			List<AccountTransferReqVo> reqVoList = new ArrayList<>();
			for (BatchOrderList batchOrder : batchOrderList) {
				AccountTransferReqVo reqVo = new AccountTransferReqVo();
				reqVo.setTransAmt(batchOrder.getAmount());
				reqVo.setUploadAmt(batchOrder.getAmount());
				reqVo.setTfrInBId(batchOrder.getTfrInBid());
				reqVo.setTfrInUserId(batchOrder.getTfrInId());
				reqVo.setTfrOutBId(batchOrder.getTfrOutBid());
				reqVo.setTfrOutUserId(batchOrder.getTfrOutId());
				reqVo.setTransId(TransCode.MB50.getCode());
				reqVo.setTransChnl(TransChnl.CHANNEL0.toString());
				reqVo.setUserId(batchOrder.getPhoneNo());
				reqVo.setUserType(UserType.TYPE200.getCode());
				reqVo.setDmsRelatedKey(batchOrder.getOrderListId());
				reqVo.setUserChnlId(order.getCompanyId());
				reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());

				Set<String> bIds = new TreeSet<>();
				bIds.add(batchOrder.getBizType());
				reqVo.setbIds(bIds);

				reqVoList.add(reqVo);
			}
			BaseResult result = new BaseResult();
			if (batchOrderList.size() > 1) {
				try {
					
				} catch (Exception e) {
					logger.error("## 远程调用批量转账接口出错{}", reqVoList, e);
				}
			} else {
				try {
					result = accountTransactionFacade.executeTransfer(reqVoList.get(0));
				} catch (Exception e) {
					logger.error("## 远程调用转账接口出错{}", reqVoList.get(0), e);
					
				}
			}
			for (BatchOrderList oList : batchOrderList) {
				if (result != null && result.getCode().equals(Constants.SUCCESS_CODE.toString())) {
					oList.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
					order.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
				} else {
					oList.setOrderStat(BatchOrderStat.BatchOrderStat_99.getCode());
					order.setOrderStat(BatchOrderStat.BatchOrderStat_99.getCode());
				}
			}
			int orderListResult = batchOrderListMapper.updateBatchOrderListByList(batchOrderList);
			int orderRsult = batchOrderMapper.updateBatchOrder(order);
			if (orderListResult < 0 || orderRsult < 0) {
				logger.error("## 更新批量充值后的订单状态信息失败，batchOrder--->{},batchOrderList--->{}", JSONArray.toJSONString(order), JSONArray.toJSONString(batchOrderList));
				return 0;
			}
		}
		return 1;
	}

}
