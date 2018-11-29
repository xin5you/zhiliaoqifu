package com.cn.thinkx.ecom.front.api.returns.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.basics.order.domain.EcomExpressConfirm;
import com.cn.thinkx.ecom.basics.order.domain.EcomRefundAddress;
import com.cn.thinkx.ecom.basics.order.domain.ReturnsInf;
import com.cn.thinkx.ecom.basics.order.domain.ReturnsOrder;
import com.cn.thinkx.ecom.basics.order.domain.SellbackGoodslist;
import com.cn.thinkx.ecom.basics.order.service.EcomExpressConfirmService;
import com.cn.thinkx.ecom.basics.order.service.EcomRefundAddressService;
import com.cn.thinkx.ecom.basics.order.service.ReturnsOrderService;
import com.cn.thinkx.ecom.basics.order.service.SellbackGoodslistService;
import com.cn.thinkx.ecom.common.constants.Constants.ApplyReturnState;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsEcomCodeType;
import com.cn.thinkx.ecom.common.constants.Constants.ReturnsStatus;
import com.cn.thinkx.ecom.common.constants.Constants.ReturnsType;
import com.cn.thinkx.ecom.common.constants.Constants.returnType;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.front.api.returns.service.ReturnsGoodsService;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.YXOpenApiService;
import com.cn.thinkx.ecom.you163.api.vo.ExpressInfoVO;
import com.cn.thinkx.ecom.you163.api.vo.apply.ApplyInfoVO;
import com.cn.thinkx.ecom.you163.api.vo.apply.ApplySkuReason;
import com.cn.thinkx.ecom.you163.api.vo.apply.ApplySkuVO;
import com.cn.thinkx.ecom.you163.api.vo.apply.ApplyUserVO;
import com.cn.thinkx.ecom.you163.api.vo.apply.OrderRefundApplyResponseVO;
import com.cn.thinkx.ecom.you163.api.vo.apply.OrderRefundApplySkuResponseVO;

@Service("returnsGoodsService")
public class ReturnsGoodsServiceImpl implements ReturnsGoodsService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ReturnsOrderService returnsOrderService;
	
	@Autowired
	private SellbackGoodslistService sellbackGoodslistService;
	
	@Autowired
	private YXOpenApiService yxOpenApiService;
	
	@Autowired
	private EcomRefundAddressService ecomRefundAddressService;
	
	@Autowired
	private EcomExpressConfirmService ecomExpressConfirmService;

	@Override
	public BaseResult<Object> handleReturnsOrder(ReturnsInf returns, String reason, String reasonDesc) {
		BaseResult<Object> returnsResult = new BaseResult<Object>();
		ReturnsOrder returnsOrder = new ReturnsOrder();
		try {
			returnsOrder.setsOrderId(returns.getsOrderId());
			returnsOrder.setMemberId(returns.getMemberId());
			returnsOrder.setReturnsStatus(ReturnsStatus.RS0.getCode());
			returnsOrder.setReturnsType(ReturnsType.RT1.getCode());
			returnsOrder.setApplyReasonType(reason);
			returnsOrder.setApplyReason(reasonDesc);
			returnsOrder.setResv1(String.valueOf(Integer.valueOf(returns.getProductNum())*Integer.valueOf(returns.getProductPrice())));
			int i = returnsOrderService.insert(returnsOrder);
			if (i != 1) {
				logger.error("添加退换货申请数据出错", new RuntimeException());
				returnsResult.setCode(ExceptionEnum.ERROR_CODE);
				returnsResult.setMsg(ExceptionEnum.ERROR_MSG);
				return returnsResult;
			}
			
			SellbackGoodslist sellBack = new SellbackGoodslist();
			
			sellBack.setsOrderId(returns.getsOrderId());
			sellBack.setReturnsId(returnsOrder.getReturnsId());
			sellBack.setoItemId(returns.getoItemId());
			sellBack.setReturnNum(returns.getProductNum());
			sellBack.setApplyReturnState(ApplyReturnState.ARS0.getCode());
			
			int j = sellbackGoodslistService.insert(sellBack);
			
			if (j != 1) {
				logger.error("添加退换货商品数据出错", new RuntimeException());
				returnsResult.setCode(ExceptionEnum.ERROR_CODE);
				returnsResult.setMsg(ExceptionEnum.ERROR_MSG);
				return returnsResult;
			}
			
		} catch (Exception e) {
			logger.error("申请退货异常", e);
			returnsResult.setCode(ExceptionEnum.ERROR_CODE);
			returnsResult.setMsg(ExceptionEnum.ERROR_MSG);
			return returnsResult;
		}
		
		if (GoodsEcomCodeType.ECOM01.getCode().equals(returns.getEcomCode())) {
			try {
				//售后申请人信息
				ApplyUserVO applyUser = new ApplyUserVO();
				applyUser.setName(returns.getShipName());
				applyUser.setMobile(returns.getShipMobile());
				//退换货原因
				ApplySkuReason applySkuReason = new ApplySkuReason();
				applySkuReason.setReason(returnType.findByCode(reason).getValue());
				applySkuReason.setReasonDesc(reasonDesc);
				//申请售后的sku信息
				ApplySkuVO applySku = new ApplySkuVO();
				applySku.setPackageId(returns.getPackageNo());
				applySku.setSkuId(returns.getSkuCode());
				applySku.setCount(Integer.valueOf(returns.getProductNum()));
				applySku.setApplySkuReason(applySkuReason);
				applySku.setOriginalPrice(NumberUtils.RMBCentToYuanForDecimal(returns.getGoodsCost()));
				applySku.setSubtotalPrice(NumberUtils.RMBCentToYuanForDecimal(returns.getProductPrice()));
				//售后申请请求信息
				ApplyInfoVO applyInfo = new ApplyInfoVO();
				applyInfo.setRequestId(returnsOrder.getReturnsId());
				applyInfo.setOrderId(returnsOrder.getsOrderId());
				applyInfo.setApplyUser(applyUser);
				applyInfo.setApplySku(applySku);
				
				JsonResult result = yxOpenApiService.handleOrderRefundApply(applyInfo);
				
				ReturnsOrder rOrder = new ReturnsOrder();
				if ("200".equals(result.getCode())) {
					OrderRefundApplyResponseVO resp = JSONObject.parseObject(result.getResult().toString(), OrderRefundApplyResponseVO.class);
					OrderRefundApplySkuResponseVO refundSku = resp.getApplySkuList().get(0);
					
					rOrder.setReturnsId(returnsOrder.getReturnsId());
					rOrder.setApplyId(resp.getApplyId());
					
					returnsOrderService.updateByPrimaryKey(rOrder);//编辑申请单编号
					
					if (refundSku.getReturnStoreHouse() != null) {
						EcomRefundAddress address = new EcomRefundAddress();
						address.setReturnsId(resp.getApplyId());
						address.setProvinceName(refundSku.getReturnStoreHouse().getProvinceName());
						address.setCityName(refundSku.getReturnStoreHouse().getCityName());
						address.setDistrictName(refundSku.getReturnStoreHouse().getDistrictName());
						address.setAddress(refundSku.getReturnStoreHouse().getAddress());
						address.setFullAddress(refundSku.getReturnStoreHouse().getFullAddress());
						address.setZipCode(refundSku.getReturnStoreHouse().getZipCode());
						address.setName(refundSku.getReturnStoreHouse().getName());
						address.setMobile(refundSku.getReturnStoreHouse().getMobile());
						
						ecomRefundAddressService.saveEcomRefundAddress(address);
					}
				} else {
					rOrder.setReturnsId(returnsOrder.getReturnsId());
					rOrder.setReturnsStatus(ReturnsStatus.RS1.getCode());
					rOrder.setRefuseReason(result.getMessage());
					returnsOrderService.updateByPrimaryKey(rOrder);

					SellbackGoodslist newSellBack = new SellbackGoodslist();
					
					newSellBack.setReturnsId(returnsOrder.getReturnsId());
					newSellBack.setApplyReturnState(ApplyReturnState.ARS9.getCode());
					
					sellbackGoodslistService.updateSellbackGoodslistByReturnsId(newSellBack); //编辑退货商品表状态
				}
				
				returnsResult.setCode(ExceptionEnum.SUCCESS_CODE);
				returnsResult.setMsg(ExceptionEnum.SUCCESS_MSG);
				returnsResult.setObject(returnsOrder.getReturnsId());
				
			} catch (Exception e) {
				returnsResult.setCode(ExceptionEnum.ERROR_CODE);
				returnsResult.setMsg(ExceptionEnum.ERROR_MSG);
				return returnsResult;
			}
		}
		return returnsResult;
	}


	@Override
	public BaseResult<Object> handleOfferExpress(ReturnsOrder returnsOrder, EcomExpressConfirm confirm) throws Exception {
		BaseResult<Object> returnsResult = new BaseResult<Object>();
		
		try {
			int i = ecomExpressConfirmService.insert(confirm);
			if (i != 1) {
				returnsResult.setCode(ExceptionEnum.ERROR_CODE);
				returnsResult.setMsg(ExceptionEnum.ERROR_MSG);
				return returnsResult;
			}
		} catch (Exception e) {
			logger.error(" ## 添加退货退货物流信息出错 ", e);
			returnsResult.setCode(ExceptionEnum.ERROR_CODE);
			returnsResult.setMsg(ExceptionEnum.ERROR_MSG);
			return returnsResult;
		}
		
		
		try{
			List<ExpressInfoVO> expressInfo = new ArrayList<ExpressInfoVO>();
			ExpressInfoVO express = new ExpressInfoVO();
			express.setTrackingCompany(confirm.getTrackingCompany());
			express.setTrackingNum(confirm.getTrackingNum());
			expressInfo.add(express);
			
			JsonResult result = yxOpenApiService.handleOrderRefundOfferExpress(returnsOrder.getApplyId(), expressInfo);
			
			if ("200".equals(result.getCode())) {
				
				SellbackGoodslist newSellBack = new SellbackGoodslist();
				
				newSellBack.setReturnsId(returnsOrder.getReturnsId());
				newSellBack.setApplyReturnState(ApplyReturnState.ARS2.getCode());
				
				sellbackGoodslistService.updateSellbackGoodslistByReturnsId(newSellBack); //编辑退货商品表状态
				
				returnsResult.setCode(ExceptionEnum.SUCCESS_CODE);
				returnsResult.setMsg(ExceptionEnum.SUCCESS_MSG);
				
			} 
			
		} catch(Exception e){
			logger.error(" ## 绑定退货物流信息出错 ", e);
			returnsResult.setCode(ExceptionEnum.ERROR_CODE);
			returnsResult.setMsg(ExceptionEnum.ERROR_MSG);
			return returnsResult;
		}
		
		
		return returnsResult;
	}
	
	@Override
	public BaseResult<Object> handleCancelReturns(ReturnsOrder returnsOrder) {
		BaseResult<Object> returnsResult = new BaseResult<Object>();
		try {
			JsonResult result = yxOpenApiService.handleOrderRefundCancel(returnsOrder.getApplyId());
			if ("200".equals(result.getCode())) {
				returnsOrder.setReturnsStatus(ReturnsStatus.RS4.getCode());
				returnsOrderService.updateByPrimaryKey(returnsOrder);
				returnsResult.setCode(ExceptionEnum.SUCCESS_CODE);
				returnsResult.setMsg(ExceptionEnum.SUCCESS_MSG);
			}
			
		} catch (Exception e) {
			logger.error(" ## 调用取消售后服务请求出错 ", e);
			returnsResult.setCode(ExceptionEnum.ERROR_CODE);
			returnsResult.setMsg(ExceptionEnum.ERROR_MSG);
			return returnsResult;
		}
		return returnsResult;
	}

	@Override
	public BaseResult<Object> handleQueryReturnsOrder(String applyId) {
		
		BaseResult<Object> returnsResult = new BaseResult<Object>();
		try {
			JsonResult result = yxOpenApiService.handleGetOrderRefundDetail(applyId);
			if ("200".equals(result.getCode())) {
				returnsResult.setCode(ExceptionEnum.SUCCESS_CODE);
				returnsResult.setMsg(ExceptionEnum.SUCCESS_MSG);
				returnsResult.setObject(result.getResult());
			}
			
		} catch (Exception e) {
			logger.error(" ## 调用取消售后服务请求出错 ", e);
			returnsResult.setCode(ExceptionEnum.ERROR_CODE);
			returnsResult.setMsg(ExceptionEnum.ERROR_MSG);
			return returnsResult;
		}
		return returnsResult;
	}

}
