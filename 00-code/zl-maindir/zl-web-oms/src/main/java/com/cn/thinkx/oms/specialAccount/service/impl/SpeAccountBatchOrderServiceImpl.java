package com.cn.thinkx.oms.specialAccount.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.SendResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cn.thinkx.oms.common.util.OmsEnum.BatchOrderStat;
import com.cn.thinkx.oms.specialAccount.mapper.CompanyInfMapper;
import com.cn.thinkx.oms.specialAccount.mapper.SpeAccountBatchOrderListMapper;
import com.cn.thinkx.oms.specialAccount.mapper.SpeAccountBatchOrderMapper;
import com.cn.thinkx.oms.specialAccount.model.BillingTypeInf;
import com.cn.thinkx.oms.specialAccount.model.CompanyInf;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrder;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.cn.thinkx.oms.specialAccount.service.BillingTypeInfService;
import com.cn.thinkx.oms.specialAccount.service.SpeAccountBatchOrderService;
import com.cn.thinkx.oms.sys.model.User;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("speAccountBatchOrderService")
public class SpeAccountBatchOrderServiceImpl implements SpeAccountBatchOrderService {

	Logger logger = LoggerFactory.getLogger(SpeAccountBatchOrderServiceImpl.class);

	@Autowired
	private SpeAccountBatchOrderListMapper speAccountBatchOrderListMapper;
	
	@Autowired
	private SpeAccountBatchOrderMapper speAccountBatchOrderMapper;
	
	@Autowired
	private CompanyInfMapper companyInfMapper;
	
	@Autowired
	@Qualifier("billingTypeInfService")
	private BillingTypeInfService billingTypeInfService;
	
	@Reference(check=false)
	private AccountManageFacade accountManageFacade;
	
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
	public int addSpeAccountBatchOrder(SpeAccountBatchOrder order, LinkedList<SpeAccountBatchOrderList> personInfList) {
		int orderResult = 0;
		int orderListResult = 0;
		try {
			/*orderResult = speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order);//添加订单数据
			personInfList.forEach(orderList ->{
				orderList.setOrderId(order.getOrderId());
				orderList.setOrderStat(BatchOrderStat.BatchOrderStat_30.getCode());
				orderList.setAccountType(order.getAccountType());
				orderList.setBizType(order.getBizType());
				orderList.setUpdateUser(order.getCreateUser());
				orderList.setUpdateTime(System.currentTimeMillis());
			});
			orderListResult = speAccountBatchOrderListMapper.updateSpeAccountBatchOrderListByList(personInfList);*/
			
			orderResult = speAccountBatchOrderMapper.addSpeAccountBatchOrder(order);//添加订单数据
			personInfList.forEach(orderList ->{
				orderList.setOrderId(order.getOrderId());
				if (StringUtil.isNotEmpty(orderList.getAmount())) {
					orderList.setAmount(NumberUtils.RMBYuanToCent(orderList.getAmount()));
				}
				orderList.setOrderId(order.getOrderId());
				orderList.setOrderStat(BatchOrderStat.BatchOrderStat_30.getCode());
				orderList.setAccountType(order.getAccountType());
				orderList.setBizType(order.getBizType());
				orderList.setCreateUser(order.getCreateUser());
				orderList.setUpdateUser(order.getUpdateUser());
				orderList.setCreateTime(System.currentTimeMillis());
				orderList.setUpdateTime(System.currentTimeMillis());
			});
			orderListResult = speAccountBatchOrderListMapper.addSpeAccountBatchOrderList(personInfList);//添加订单明细
			//添加订单明细
			if (orderResult > 0 && orderListResult >0) {
				return 1;
			}
		} catch (Exception e) {
			logger.error("批量开户数据提交出错a---->>{},b----->>{}", orderResult, orderListResult, e);
		}
		return 0;
	}

	@Override
	public SpeAccountBatchOrder getSpeAccountBatchOrderByOrderId(String orderId) {
		return speAccountBatchOrderMapper.getSpeAccountBatchOrderByOrderId(orderId);
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
	public void batchSpeAccountBatchOpenAccountITF(String orderId, User user, String commitStat) {
		SpeAccountBatchOrder order = speAccountBatchOrderMapper.getSpeAccountBatchOrderById(orderId);
		if (order != null && BatchOrderStat.BatchOrderStat_10.getCode().equals(order.getOrderStat())) {
			order.setOrderStat(BatchOrderStat.BatchOrderStat_40.getCode());
			order.setUpdateUser(user.getId().toString());
			order.setUpdateTime(System.currentTimeMillis());
			this.updateSpeAccountBatchOrder(order); // 修改订单状态
			billingTypeInfService.getBillingTypeInfList(new BillingTypeInf());
			List<SpeAccountBatchOrderList> speList = getBatchOrderList(orderId, commitStat);
			if (speList != null) {
				speList.forEach(spe ->{
					SpeAccountBatchOrder speOrder = null;
					//TODO 调用开户接口
//					SpeAccountBatchOrderList batchOrder = speList.get(0);
					for (SpeAccountBatchOrderList batchOrder : speList) {
						AccountOpenReqVo reqVo = new AccountOpenReqVo();
						reqVo.setUserName(batchOrder.getUserName());
						reqVo.setMobilePhone(batchOrder.getPhoneNo());
						reqVo.setIcardNo(batchOrder.getUserCardNo());
						reqVo.setCompanyId(batchOrder.getCompanyId());
						reqVo.setTransId(TransCode.CW80.getCode());
						reqVo.setTransChnl(UserChnlCode.USERCHNL1001.getCode());
						reqVo.setUserId(batchOrder.getCompanyId());
						Set<String> bIds = new TreeSet<>();
						
//						bIds.addAll(c)
						reqVo.setbIds(bIds);
//						BaseResult result = accountManageFacade.createAccount(req);
					}
					
					
					
					
					speOrder = speAccountBatchOrderMapper.getSpeAccountBatchOrderById(order.getOrderId());
					SendResult result = null;
					try {
						
					} catch (Exception e) {
						logger.error(" ## 发送开户模块消息出错，result：[{}]",result,e);
					}
				});
			}
		}
	}

	@Override
	public void batchSpeAccountRechargeITF(String orderId, User user, String commitStat) {
		SpeAccountBatchOrder order = speAccountBatchOrderMapper.getSpeAccountBatchOrderById(orderId);
		if ((order != null && BatchOrderStat.BatchOrderStat_10.getCode().equals(order.getOrderStat()))) {
			
			order.setOrderStat(BatchOrderStat.BatchOrderStat_30.getCode());
			order.setUpdateUser(user.getId().toString());
			order.setUpdateTime(System.currentTimeMillis());
			this.updateSpeAccountBatchOrder(order); // 修改订单状态
			
			List<SpeAccountBatchOrderList> speList = getBatchOrderList(orderId, commitStat);
			if(speList != null){
				speList.forEach(spe ->{
					SpeAccountBatchOrder speOrder = null;
//					PersonInf person = personInfMapper.getPersonInfByPhoneNo(spe.getPhoneNo());
					
					CompanyInf companyInf = companyInfMapper.getCompanyInfById(spe.getCompanyId());//所属公司信息
					
					try {
						//调用mq发送模板消息
						
					} catch (Exception e) {
						logger.error(" ## 发送充值模块消息出错，result：[{}]",e);
					}
					
				});
			}
		}
	}

}
