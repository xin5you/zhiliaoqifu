package com.ebeijia.zl.web.oms.specialAccount.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.ebeijia.zl.web.oms.specialAccount.mapper.SpeAccountBatchOrderListMapper;
import com.ebeijia.zl.web.oms.specialAccount.mapper.SpeAccountBatchOrderMapper;
import com.ebeijia.zl.web.oms.specialAccount.model.SpeAccountBatchOrder;
import com.ebeijia.zl.web.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.ebeijia.zl.web.oms.specialAccount.service.SpeAccountBatchOrderService;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.enums.TransChnl;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;
import com.ebeijia.zl.facade.account.req.AccountRechargeReqVo;
import com.ebeijia.zl.facade.account.req.AccountTxnVo;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("speAccountBatchOrderService")
public class SpeAccountBatchOrderServiceImpl implements SpeAccountBatchOrderService {

	Logger logger = LoggerFactory.getLogger(SpeAccountBatchOrderServiceImpl.class);
	
	ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	@Autowired
	private SpeAccountBatchOrderListMapper speAccountBatchOrderListMapper;
	
	@Autowired
	private SpeAccountBatchOrderMapper speAccountBatchOrderMapper;
	
	@Reference(check = false, version = "1.0.0")
	private AccountManageFacade accountManageFacade;
	
	@Reference(check = false, version = "1.0.0")
	private AccountTransactionFacade accountTransactionFacade;
	
	@Override
	public List<SpeAccountBatchOrder> getSpeAccountBatchOrderList(SpeAccountBatchOrder order) {
		return speAccountBatchOrderMapper.getSpeAccountBatchOrderList(order);
	}

	@Override
	public int addSpeAccountBatchOrder(SpeAccountBatchOrder order) {
		return speAccountBatchOrderMapper.addSpeAccountBatchOrder(order);
	}

	@Override
	public int updateSpeAccountBatchOrder(SpeAccountBatchOrder order) {
		return speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order);
	}

	@Override
	public int deleteSpeAccountBatchOrder(String orderId) {
		return speAccountBatchOrderMapper.deleteSpeAccountBatchOrder(orderId);
	}

	@Override
	public PageInfo<SpeAccountBatchOrder> getSpeAccountBatchOrderPage(int startNum, int pageSize, SpeAccountBatchOrder order, HttpServletRequest req) {
		PageHelper.startPage(startNum, pageSize);
		order.setOrderId(StringUtil.nullToString(req.getParameter("orderId")));//订单号
		order.setOrderName(StringUtil.nullToString(req.getParameter("orderName")));//订单名称
		order.setCompanyId(StringUtil.nullToString(req.getParameter("companyId")));
		order.setOrderStat(StringUtil.nullToString(req.getParameter("orderStat")));//订单状态
		order.setStartTime(StringUtil.nullToString(req.getParameter("startTime")));//开始时间
		order.setEndTime(StringUtil.nullToString(req.getParameter("endTime")));//结束时间
		List<SpeAccountBatchOrder> list = getSpeAccountBatchOrderList(order);
		PageInfo<SpeAccountBatchOrder> page = null;
		if(list != null){
			list.forEach(batchOrder ->{
				HttpSession session = req.getSession();
				User user = (User)session.getAttribute(Constants.SESSION_USER);
				batchOrder.setCreateUser(user.getId());
				batchOrder.setOrderStat(BatchOrderStat.findStat(batchOrder.getOrderStat()));
				if (!StringUtil.isNullOrEmpty(batchOrder.getSumAmount())) {
					batchOrder.setSumAmount(NumberUtils.RMBCentToYuan(batchOrder.getSumAmount()));
				} else {
					batchOrder.setSumAmount(NumberUtils.RMBCentToYuan(0));
				}
			});
			page = new PageInfo<SpeAccountBatchOrder>(list);
		}
		return page;
	}

	@Override
	public int addSpeAccountBatchOrder(HttpServletRequest req, LinkedList<SpeAccountBatchOrderList> personInfList, String orderType) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		String [] billingTypes=req.getParameterValues("billingTypes[]");
		String billingType = StringUtil.nullToString(req.getParameter("billingType"));
		if (TransCode.MB50.getCode().equals(orderType)) {
			billingTypes = new String[]{billingType};
		}
		SpeAccountBatchOrder order = new SpeAccountBatchOrder();
		order.setOrderId(UUID.randomUUID().toString());
		order.setOrderName(StringUtil.nullToString(req.getParameter("orderName")));
		order.setCompanyId(StringUtil.nullToString(req.getParameter("companyId")));
		order.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
		order.setOrderType(orderType);
		order.setOrderDate(System.currentTimeMillis());
		order.setCreateUser(user.getId().toString());
		order.setCreateTime(System.currentTimeMillis());
		order.setUpdateUser(user.getId().toString());
		order.setUpdateTime(System.currentTimeMillis());
		int orderResult = 0;
		int orderListResult = 0;
		try {
			orderResult = speAccountBatchOrderMapper.addSpeAccountBatchOrder(order);//添加订单数据
			List<SpeAccountBatchOrderList> list = new ArrayList<>();
			
			for (String type : billingTypes) {
				for (SpeAccountBatchOrderList orderList : personInfList) {
					SpeAccountBatchOrderList o = new SpeAccountBatchOrderList();
					o.setOrderListId(UUID.randomUUID().toString());
					o.setOrderId(order.getOrderId());
					o.setUserName(orderList.getUserName());
					o.setPhoneNo(orderList.getPhoneNo());
					o.setUserCardNo(orderList.getUserCardNo());
					o.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
					o.setAccountType(UserType.TYPE100.getCode());
					o.setBizType(type);
					if (TransCode.MB50.getCode().equals(orderType)) {
						o.setTfrInId(o.getBizType());
						o.setTfrInBid(o.getPhoneNo());
						o.setTfrOutId(order.getCompanyId());
						o.setTfrOutBid(SpecAccountTypeEnum.A0.getCode());
						o.setAmount(new Double(NumberUtils.RMBYuanToCent(String.valueOf(orderList.getAmount()))));
					}
					o.setCreateUser(order.getCreateUser());
					o.setUpdateUser(order.getUpdateUser());
					o.setCreateTime(System.currentTimeMillis());
					o.setUpdateTime(System.currentTimeMillis());
					list.add(o);
				}
			}
			orderListResult = speAccountBatchOrderListMapper.addSpeAccountBatchOrderList(list);//添加订单明细
			//添加订单明细
			if (orderResult > 0 && orderListResult > 0) {
				return 1;
			}
		} catch (Exception e) {
			logger.error("批量开户数据提交出错a---->>{},b----->>{}", orderResult, orderListResult, e);
		}
		return 0;
	}

	@Override
	public SpeAccountBatchOrder getSpeAccountBatchOrderByOrderId(String orderId) {
		SpeAccountBatchOrder order = speAccountBatchOrderMapper.getSpeAccountBatchOrderByOrderId(orderId);
		if (StringUtil.isNullOrEmpty(order.getSumAmount())) {
			order.setSumAmount(NumberUtils.RMBCentToYuan("0"));
		} else {
			order.setSumAmount(NumberUtils.RMBCentToYuan(order.getSumAmount()));
		}
		return order;
	}

	@Override
	public SpeAccountBatchOrder getSpeAccountBatchOrderById(String orderId) {
		return speAccountBatchOrderMapper.getSpeAccountBatchOrderById(orderId);
	}
	
	public List<SpeAccountBatchOrderList> getBatchOrderList(String orderId, String commitStat) {
		List<SpeAccountBatchOrderList> list = null;
		if (BatchOrderStat.BatchOrderStat_10.getCode().equals(commitStat)) {
			list = speAccountBatchOrderListMapper.getSpeAccountBatchOrderListByOrderId(orderId);
		}
		if (BatchOrderStat.BatchOrderStat_99.getCode().equals(commitStat)) {
			list = speAccountBatchOrderListMapper.getSpeAccountBatchOrderListFailListByOrderId(orderId);
		}
		return list;
	}

	@Override
	public int batchSpeAccountBatchOpenAccountITF(String orderId, User user, String commitStat) {
		SpeAccountBatchOrder order = speAccountBatchOrderMapper.getSpeAccountBatchOrderById(orderId);
		if (order != null && BatchOrderStat.BatchOrderStat_10.getCode().equals(order.getOrderStat())) {
			order.setOrderStat(BatchOrderStat.BatchOrderStat_30.getCode());
			order.setUpdateUser(user.getId().toString());
			order.setUpdateTime(System.currentTimeMillis());
			speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order); // 修改订单状态
		}
		List<SpeAccountBatchOrderList> speList = getBatchOrderList(orderId, commitStat);
		if (speList == null) {
			logger.error("## 批量开户名单为空");
			return 0;
		}
		List<AccountOpenReqVo> reqVoList = new ArrayList<>();
		Set<String> bIds = new TreeSet<>();
			for (SpeAccountBatchOrderList batchOrder : speList) {
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
				}
				reqVo.setTransChnl(TransChnl.CHANNEL0.toString());
				//reqVo.setUserId(batchOrder.getCompanyId());
				reqVo.setUserType(batchOrder.getAccountType());
				reqVo.setDmsRelatedKey(batchOrder.getOrderListId());
				reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
				reqVoList.add(reqVo);
			}
			reqVoList = reqVoList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(AccountOpenReqVo::getMobilePhone))), ArrayList::new));
			int openAccount = 0;
			BaseResult result = null;
			for (AccountOpenReqVo req : reqVoList) {
				req.setbIds(bIds);
				try {
					result = accountManageFacade.createAccount(req);
				} catch (Exception e) {
					logger.error("## 远程调用开户接口失败{}", e);
					return 0;
				}
				SpeAccountBatchOrderList orderList = new SpeAccountBatchOrderList();
				orderList.setPhoneNo(req.getMobilePhone());
				List<SpeAccountBatchOrderList> batchOrderList = speAccountBatchOrderListMapper.getSpeAccountBatchOrderListByOrder(orderList);
				for (SpeAccountBatchOrderList orderLists : batchOrderList) {
					if (!StringUtil.isNullOrEmpty(result) && result.getCode().equals(Constants.SUCCESS_CODE.toString())) {
						orderList.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
					} else {
						orderList.setOrderStat(BatchOrderStat.BatchOrderStat_99.getCode());
					}
				}
				openAccount += speAccountBatchOrderListMapper.updateSpeAccountBatchOrderListByList(batchOrderList);
				
			}
			if (!StringUtil.isNullOrEmpty(result) && result.getCode().equals(Constants.SUCCESS_CODE.toString())) {
				if (openAccount > 0 && speList.size() == openAccount) {
					order.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
				} else {
					order.setOrderStat(BatchOrderStat.BatchOrderStat_40.getCode());
				}
			} else {
				order.setOrderStat(BatchOrderStat.BatchOrderStat_99.getCode());
			}
			return speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order);
	}

	@Override
	public int batchSpeAccountRechargeITF(String orderId, User user, String commitStat) {
		SpeAccountBatchOrder order = speAccountBatchOrderMapper.getSpeAccountBatchOrderById(orderId);
		if ((order != null && BatchOrderStat.BatchOrderStat_10.getCode().equals(order.getOrderStat()))) {
			order.setOrderStat(BatchOrderStat.BatchOrderStat_30.getCode());
			order.setUpdateUser(user.getId().toString());
			order.setUpdateTime(System.currentTimeMillis());
			speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order); // 修改订单状态
		}	
		List<SpeAccountBatchOrderList> speList = getBatchOrderList(orderId, commitStat);
		int i = 0 ;
		if (speList != null && speList.size() > 0) {
			List<AccountRechargeReqVo> reqVoList = new ArrayList<>();
			for (SpeAccountBatchOrderList batchOrder : speList) {
				AccountRechargeReqVo reqVo = new AccountRechargeReqVo();
				reqVo.setMobilePhone(batchOrder.getPhoneNo());
				reqVo.setFromCompanyId(batchOrder.getCompanyId());
				reqVo.setPriBId(batchOrder.getBizType());
				reqVo.setTransAmt(new BigDecimal(batchOrder.getAmount()));
				reqVo.setUploadAmt(new BigDecimal(batchOrder.getAmount()));

				AccountTxnVo txnVo = new AccountTxnVo();
				txnVo.setTxnBId(batchOrder.getBizType());
				txnVo.setTxnAmt(new BigDecimal(batchOrder.getAmount()));
				List<AccountTxnVo> transList = new ArrayList<>();
				transList.add(txnVo);

				reqVo.setTransList(transList);
				reqVo.setTransId(TransCode.MB50.getCode());
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
			int orderListResult = 0;
			if (speList.size() > 1) {
				try {
					result = accountTransactionFacade.executeRecharge(reqVoList);
				} catch (Exception e) {
					logger.error("## 远程调用批量充值接口出错{}", reqVoList, e);
				}
			} else {
				try {
					result = accountTransactionFacade.executeRechargeByOneBId(reqVoList.get(0));
				} catch (Exception e) {
					logger.error("## 远程调用充值接口出错{}", reqVoList.get(0), e);
				}
			}
			for (SpeAccountBatchOrderList orderList : speList) {
				if (!StringUtil.isNullOrEmpty(result) && result.getCode().equals(Constants.SUCCESS_CODE.toString())) {
					orderList.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
					order.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
				} else {
					orderList.setOrderStat(BatchOrderStat.BatchOrderStat_99.getCode());
					order.setOrderStat(BatchOrderStat.BatchOrderStat_99.getCode());
				}
			}
			orderListResult = speAccountBatchOrderListMapper.updateSpeAccountBatchOrderListByList(speList);
			if (orderListResult > 0) {
				return speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order);
			}
		}
		return 0;
	}

	@Override
	public int deleteOpenAccountCommit(String orderId, User user) {
		SpeAccountBatchOrder order = speAccountBatchOrderMapper.getSpeAccountBatchOrderById(orderId);
		order.setOrderStat(BatchOrderStat.BatchOrderStat_20.getCode());
		order.setUpdateTime(System.currentTimeMillis());
		order.setUpdateUser(user.getId());
		List<SpeAccountBatchOrderList> orderList = speAccountBatchOrderListMapper.getSpeAccountBatchOrderListByOrderId(orderId);
		int i = speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order);
		int j = 0;
		if (i < 1) {
			logger.error("## 批量删除充值订单{}失败", orderId);
			return 0;
		}
		if (orderList != null && orderList.size() >= 1) {
			for (SpeAccountBatchOrderList list : orderList) {
				list.setOrderStat(BatchOrderStat.BatchOrderStat_20.getCode());
				list.setUpdateTime(System.currentTimeMillis());
				list.setUpdateUser(user.getId());
			}
			if (orderList.size() > 1) {
				j = speAccountBatchOrderListMapper.updateSpeAccountBatchOrderListByList(orderList);
			} else if (orderList.size() == 1) {
				j = speAccountBatchOrderListMapper.updateSpeAccountBatchOrderList(orderList.get(0));
			}
		}
		if (i > 0 && j == orderList.size()) {
			return 1;
		}
		return 0;
	}

}
