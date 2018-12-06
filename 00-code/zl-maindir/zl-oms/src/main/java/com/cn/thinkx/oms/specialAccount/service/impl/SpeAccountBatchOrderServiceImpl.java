/*package com.cn.thinkx.oms.specialAccount.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.SendResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.oms.specialAccount.mapper.CompanyInfMapper;
import com.cn.thinkx.oms.specialAccount.mapper.SpeAccountBatchOrderListMapper;
import com.cn.thinkx.oms.specialAccount.mapper.SpeAccountBatchOrderMapper;
import com.cn.thinkx.oms.specialAccount.model.CompanyInf;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrder;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.cn.thinkx.oms.specialAccount.service.SpeAccountBatchOrderService;
import com.cn.thinkx.oms.sys.mapper.UserMapper;
import com.cn.thinkx.oms.sys.model.User;
import com.cn.thinkx.pms.base.utils.BaseConstants;
import com.cn.thinkx.pms.base.utils.BaseConstants.ChannelCode;
import com.cn.thinkx.pms.base.utils.BaseConstants.TransCode;
import com.cn.thinkx.pms.base.utils.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
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
	private UserMapper userMapper;
	
	@Autowired
	private PersonInfMapper personInfMapper;

	@Autowired
	private UserInfMapper userInfMapper;

	@Autowired
	private ChannelUserInfMapper channelUserInfMapper;
	
	@Autowired
	private CompanyInfMapper companyInfMapper;
	
	@Autowired
	@Qualifier("rocketMQProducer")
	private RocketMQProducer rocketMQProducer;
	
	@Autowired
	private CtrlSystemMapper ctrlSystemMapper;
	
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
		order.setBizType(StringUtil.nullToString(req.getParameter("bizType")));//业务类型
		order.setStartTime(StringUtil.nullToString(req.getParameter("startTime")));//开始时间
		order.setEndTime(StringUtil.nullToString(req.getParameter("endTime")));//结束时间
		List<SpeAccountBatchOrder> list = getSpeAccountBatchOrderList(order);
		PageInfo<SpeAccountBatchOrder> page = null;
		if(list!=null){
			list.forEach(batchOrder ->{
				User user1 = userMapper.getUserById(batchOrder.getCreateUser());
				batchOrder.setCreateUser(user1.getLoginname());
				User user2 = userMapper.getUserById(batchOrder.getUpdateUser());
				batchOrder.setUpdateUser(user2.getLoginname());
				batchOrder.setOrderStat(BaseConstants.OMSOrderStat.findStat(batchOrder.getOrderStat()));
				batchOrder.setBizType(batchOrder.getBizType() == null ? "" : BaseConstants.OMSOrderBizType.findType(batchOrder.getBizType()));
				if (StringUtil.isNotNull(batchOrder.getSumAmount())) {
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
		int addOrderResult = 0;
		int addOrderListResult = 0;
		try {
			addOrderResult = speAccountBatchOrderMapper.addSpeAccountBatchOrder(order);//添加订单数据
			personInfList.forEach(orderList ->{
				orderList.setOrderId(order.getOrderId());
				if (StringUtil.isNotNull(orderList.getAmount())) {
					orderList.setAmount(NumberUtils.RMBYuanToCent(orderList.getAmount()));
				}
				orderList.setOrderStat(BaseConstants.OMSOrderListStat.orderListStat_0.getCode());
				orderList.setCreateUser(order.getCreateUser());
				orderList.setUpdateUser(order.getCreateUser());
			});
			addOrderListResult = speAccountBatchOrderListMapper.addSpeAccountBatchOrderList(personInfList);//添加订单明细
			if (addOrderResult > 0 && addOrderListResult > 0) {
				return 1;
			}
		} catch (Exception e) {
			logger.error("批量开户数据提交出错a---->>{},b----->>{}", addOrderResult, addOrderListResult, e);
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
		if (BaseConstants.OMSOrderStat.orderStat_10.getCode().equals(commitStat)) {
			list = speAccountBatchOrderListMapper.getSpeAccountBatchOrderListList(orderId);
		}
		if (BaseConstants.OMSOrderStat.orderStat_90.getCode().equals(commitStat)) {
			list = speAccountBatchOrderListMapper.getSpeAccountBatchOrderListFailList(orderId);
		}
		return list;
	}

	@Override
	public void batchSpeAccountBatchOpenAccountITF(String orderId, User user, String commitStat) {
		SpeAccountBatchOrder order = speAccountBatchOrderMapper.getSpeAccountBatchOrderById(orderId);
		if (order != null && BaseConstants.OMSOrderStat.orderStat_10.getCode().equals(order.getOrderStat()) || order != null && BaseConstants.OMSOrderStat.orderStat_90.getCode().equals(order.getOrderStat())) {
			order.setOrderStat(BaseConstants.OMSOrderStat.orderStat_30.getCode());
			order.setUpdateUser(user.getId().toString());
			this.updateSpeAccountBatchOrder(order); // 修改订单状态
			
			List<SpeAccountBatchOrderList> speList = getBatchOrderList(orderId, commitStat);
			if (speList != null) {
				speList.forEach(spe ->{
					
					UserInf userInf = null;
					PersonInf person = null;
					SpeAccountBatchOrder speOrder = null;
					try {
						person = personInfMapper.getPersonInfByPhoneAndChnl(spe.getPhoneNo(), ChannelCode.CHANNEL0.toString());
						
						if(person == null){
							
							person = personInfMapper.getPersonInfByPhoneNo(spe.getPhoneNo());
							if (person != null) {
								person.setPersonalCardType(BaseConstants.CardTypeEnum.CARD_TYPE_00.getCode()); // 证件类型
								person.setPersonalCardNo(spe.getUserCardNo());// 证件信息
								person.setPersonalName(spe.getUserName());// 用户姓名
								personInfMapper.updatePersonInf(person);
							} else {
								userInf = new UserInf();
								userInf.setDataStat(BaseConstants.DataStatEnum.TRUE_STATUS.getCode());
								userInf.setUserType(BaseConstants.UserTypeEnum.OMS_TYPE.getCode());
								userInfMapper.insertUserInf(userInf);
	
								*//** 个人信息 **//*
								person = new PersonInf();
								person.setUserId(userInf.getUserId());
								person.setPersonalCardType(BaseConstants.CardTypeEnum.CARD_TYPE_00.getCode()); // 证件类型
								person.setPersonalCardNo(spe.getUserCardNo());// 证件信息
								person.setPersonalName(spe.getUserName());// 用户姓名
								person.setMobilePhoneNo(spe.getPhoneNo());
								personInfMapper.insertPersonInf(person);
							}
							*//** 用户渠道信息 **//*
							ChannelUserInf channelUserInf = new ChannelUserInf();
							channelUserInf.setUserId(person.getUserId());
							channelUserInf.setExternalId(UUID.randomUUID().toString().replace("-", ""));
							channelUserInf.setChannelCode(BaseConstants.ChannelCode.CHANNEL0.toString());
							channelUserInf.setDataStat(BaseConstants.DataStatEnum.TRUE_STATUS.getCode());
							channelUserInfMapper.insertChannelUserInf(channelUserInf);

						}
					} catch (Exception e) {
						logger.error(" ## 保存用户信息出错，姓名：[{}]，手机号：[{}]",spe.getUserName(),spe.getPhoneNo(),e);
					}
					
					CompanyInf companyInf = companyInfMapper.getCompanyInfByComCode(spe.getCompanydCode());//所属公司信息
					
					BaseMessageMQVO message = new BaseMessageMQVO();
					
					speOrder = speAccountBatchOrderMapper.getSpeAccountBatchOrderById(order.getOrderId());
					message.setOrder(speOrder);
					message.setOrderList(spe);
					if(companyInf != null){
						message.setCompanyId(companyInf.getcId());
					}
					message.setUserId(person.getUserId());
					SendResult result = null;
					try {
						result = rocketMQProducer.sendMessage(TopicEnum.OPEN_ACCOUNT, "hkb", JSONObject.toJSONString(message));
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
		if ((order != null && BaseConstants.OMSOrderStat.orderStat_10.getCode().equals(order.getOrderStat())) || (order != null && BaseConstants.OMSOrderStat.orderStat_90.getCode().equals(order.getOrderStat()))) {
			
			order.setOrderStat(BaseConstants.OMSOrderStat.orderStat_30.getCode());
			order.setUpdateUser(user.getId().toString());
			this.updateSpeAccountBatchOrder(order); // 修改订单状态
			
			
			CtrlSystem  ctrl = ctrlSystemMapper.getCtrlSystem();
			
			List<SpeAccountBatchOrderList> speList = getBatchOrderList(orderId, commitStat);
			if(speList != null){
				speList.forEach(spe ->{
					SpeAccountBatchOrder speOrder = null;
					PersonInf person = personInfMapper.getPersonInfByPhoneNo(spe.getPhoneNo());
					
					SpecialAccountOrder speAccountOrder = new SpecialAccountOrder();//生成专用账户订单参数
					speAccountOrder.setDmsRelatedKey(spe.getOrderListId());
					speAccountOrder.setTransId(TransCode.CW20.getCode());
					speAccountOrder.setTransSt("0");
					speAccountOrder.setSettleDate(ctrl.getSettleDate());
					speAccountOrder.setUserId(person.getUserId());
					speAccountOrder.setTransChnl(ChannelCode.CHANNEL0.toString());
					speAccountOrder.setOrderStat("0");
					speAccountOrder.setOrderAmt(spe.getAmount());
					
					CompanyInf companyInf = companyInfMapper.getCompanyInfByComCode(spe.getCompanydCode());//所属公司信息
					
					BaseMessageMQVO message = new BaseMessageMQVO();
					
					speOrder = speAccountBatchOrderMapper.getSpeAccountBatchOrderById(order.getOrderId());
					message.setOrder(speOrder);
					message.setOrderList(spe);
					if(companyInf != null){
						message.setCompanyId(companyInf.getcId());
					}
					message.setUserId(person.getUserId());
					message.setbId(order.getAccountType());
					message.setSpecialAccountOrder(speAccountOrder);
					
					SendResult result = null;
					try {
						result = rocketMQProducer.sendMessage(TopicEnum.OPEN_RECHARGE, "hkb", JSONObject.toJSONString(message));
					} catch (Exception e) {
						logger.error(" ## 发送充值模块消息出错，result：[{}]",result,e);
					}
					
				});
			}
		}
	}

}
*/