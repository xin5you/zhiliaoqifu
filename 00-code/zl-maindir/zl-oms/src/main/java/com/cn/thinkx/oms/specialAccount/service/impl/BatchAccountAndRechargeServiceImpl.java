/*package com.cn.thinkx.oms.specialAccount.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.oms.specialAccount.mapper.SpeAccountBatchOrderListMapper;
import com.cn.thinkx.oms.specialAccount.mapper.SpeAccountBatchOrderMapper;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrder;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.cn.thinkx.oms.specialAccount.service.BatchAccountAndRechargeService;
import com.cn.thinkx.pms.base.domain.BaseResp;
import com.cn.thinkx.pms.base.utils.BaseConstants;

@Service("batchAccountAndRechargeService")
public class BatchAccountAndRechargeServiceImpl implements BatchAccountAndRechargeService {


	Logger logger = LoggerFactory.getLogger(BatchAccountAndRechargeServiceImpl.class);
	
	@Autowired
	private SpeAccountBatchOrderListMapper speAccountBatchOrderListMapper;
	
	@Autowired
	private SpeAccountBatchOrderMapper speAccountBatchOrderMapper;
	
	@Autowired
	private SpecialAccountTxnFacade hkbSpecialAccountTxnFacade;
	
	@Autowired
	private SpecialAccountOrderFacade hkbSpecialAccountOrderFacade;
	
	@Override
	public boolean batchOpenAccount(BaseMessageMQVO message) {
		
		boolean flag = true; 
		BaseResp resp = null;
		String result = null;
		SpeAccOpeningRequest req = new SpeAccOpeningRequest();
		try {
			req.setCompanyId(message.getCompanyId());
			req.setUserId(message.getUserId());
			
			result = hkbSpecialAccountTxnFacade.batchSpecialAccountOpeningITF(req);
			resp = JSONObject.parseObject(result, BaseResp.class);
		} catch (Exception e) {
			logger.error("  ## 调用专用账户开户接口出错，请求参数：[{}]，返回参数，[{}]",JSONObject.toJSONString(req),result,e);
			flag = false;
			return flag;
		}
		if(result != null){
			SpeAccountBatchOrder order = new SpeAccountBatchOrder();
			SpeAccountBatchOrderList orderList = new SpeAccountBatchOrderList();
			if(BaseConstants.RESPONSE_SUCCESS_CODE.equals(resp.getCode())){//成功更新订单、订单明细
				orderList.setOrderListId(message.getOrderList().getOrderListId());
				orderList.setOrderStat(BaseConstants.OMSOrderListStat.orderListStat_00.getCode());
				orderList.setLockVersion(message.getOrderList().getLockVersion()); 
				
				order.setOrderId(message.getOrder().getOrderId());
				order.setOrderStat(BaseConstants.OMSOrderStat.orderStat_40.getCode());
				order.setLockVersion(message.getOrder().getLockVersion());
			}else{//失败更新订单、订单明细
				orderList.setOrderListId(message.getOrderList().getOrderListId());
				orderList.setOrderStat(BaseConstants.OMSOrderListStat.orderListStat_99.getCode());
				orderList.setRemarks(resp.getInfo());
				orderList.setLockVersion(message.getOrderList().getLockVersion());
				
				order.setOrderId(message.getOrder().getOrderId());
				order.setOrderStat(BaseConstants.OMSOrderStat.orderStat_90.getCode());
				order.setLockVersion(message.getOrder().getLockVersion());
			}
			if(speAccountBatchOrderListMapper.updateSpeAccountBatchOrderList(orderList)<1){
				logger.error(" ## 更新订单明细出错 ,订单明细id：[{}]", orderList.getOrderListId());
				flag = false;
			}
			if(speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order)<1){
				logger.error(" ## 更新订单出错 ,订单id：[{}]", order.getOrderId());
				flag = false;
			}
		} else {
			flag = false;
		}
		
		return flag;
	}

	@Override
	public boolean batchRecharge(BaseMessageMQVO message) {
		boolean flag = true; 
		BaseResp resp = null;
		String result = null;
		String payOrderId = null;
		SpeAccRechargeRequest req = new SpeAccRechargeRequest();
		try {
			payOrderId = hkbSpecialAccountOrderFacade.saveSpecialAccountOrder(message.getSpecialAccountOrder());
		} catch (Exception e) {
			logger.error("  ## 生成专用账户订单出错，请求参数：[{}]，返回参数，[{}]",JSONObject.toJSONString(message.getSpecialAccountOrder()),result,e);
			flag = false;
			return flag;
		}
		try {
			
			req.setCompanyId(message.getCompanyId());
			req.setUserId(message.getUserId());
			req.setbId(message.getbId());
			
			req.setPayOrderId(payOrderId);
			req.setTxnAmount(message.getOrderList().getAmount());
			
			result = hkbSpecialAccountTxnFacade.doSpecialAccountRechargeITF(req);
			resp = JSONObject.parseObject(result, BaseResp.class);
		} catch (Exception e) {
			logger.error("  ## 调用专用账户充值接口出错，请求参数：[{}]，返回参数，[{}]",JSONObject.toJSONString(req),result,e);
			flag = false;
			return flag;
		}
		
		
		if(result != null){
			SpeAccountBatchOrder order = new SpeAccountBatchOrder();
			SpeAccountBatchOrderList orderList = new SpeAccountBatchOrderList();
			if(BaseConstants.RESPONSE_SUCCESS_CODE.equals(resp.getCode())){//成功更新订单、订单明细
				orderList.setOrderListId(message.getOrderList().getOrderListId());
				orderList.setOrderStat(BaseConstants.OMSOrderListStat.orderListStat_00.getCode());
				orderList.setLockVersion(message.getOrderList().getLockVersion()); 
				orderList.setRemarks(resp.getInfo());
				
				order.setOrderId(message.getOrder().getOrderId());
				order.setOrderStat(BaseConstants.OMSOrderStat.orderStat_40.getCode());
				order.setLockVersion(message.getOrder().getLockVersion());
			}else{//失败更新订单、订单明细
				orderList.setOrderListId(message.getOrderList().getOrderListId());
				orderList.setOrderStat(BaseConstants.OMSOrderListStat.orderListStat_99.getCode());
				orderList.setRemarks(resp.getInfo());
				orderList.setLockVersion(message.getOrderList().getLockVersion());
				
				order.setOrderId(message.getOrder().getOrderId());
				order.setOrderStat(BaseConstants.OMSOrderStat.orderStat_90.getCode());
				order.setLockVersion(message.getOrder().getLockVersion());
			}
			if(speAccountBatchOrderListMapper.updateSpeAccountBatchOrderList(orderList)<1){
				logger.error(" ## 更新订单明细出错 ,订单明细id：[{}]", orderList.getOrderListId());
				flag = false;
			}
			if(speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order)<1){
				logger.error(" ## 更新订单出错 ,订单id：[{}]", order.getOrderId());
				flag = false;
			}
		} else {
			flag = false;
		}
		
		return flag;
	}

}
*/