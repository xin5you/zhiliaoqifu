package com.cn.thinkx.ecom.front.api.phoneRecharge.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.bm001.api.constants.BMConstants;
import com.cn.thinkx.ecom.bm001.api.service.BMOpenApiService;
import com.cn.thinkx.ecom.bm001.api.service.BMOrderService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.Constants.ChannelCode;
import com.cn.thinkx.ecom.common.constants.Constants.PhoneRechargeTransStat;
import com.cn.thinkx.ecom.common.constants.Constants.RechargeState;
import com.cn.thinkx.ecom.common.constants.Constants.SupplierType;
import com.cn.thinkx.ecom.common.constants.Constants.refundFalg;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.common.util.HttpClientUtil;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.RandomStrUtil;
import com.cn.thinkx.ecom.common.util.SignUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.phoneRecharge.constants.PhoneRechargeConstants;
import com.cn.thinkx.ecom.front.api.phoneRecharge.domain.PhoneRechargeOrder;
import com.cn.thinkx.ecom.front.api.phoneRecharge.mapper.PhoneRechargeMapper;
import com.cn.thinkx.ecom.front.api.phoneRecharge.req.PhoneRechargeGetReq;
import com.cn.thinkx.ecom.front.api.phoneRecharge.req.PhoneRechargeRefundReq;
import com.cn.thinkx.ecom.front.api.phoneRecharge.req.PhoneRechargeReq;
import com.cn.thinkx.ecom.front.api.phoneRecharge.resp.PhoneRechargeGetResp;
import com.cn.thinkx.ecom.front.api.phoneRecharge.resp.PhoneRechargeRefundResp;
import com.cn.thinkx.ecom.front.api.phoneRecharge.resp.PhoneRechargeResp;
import com.cn.thinkx.ecom.front.api.phoneRecharge.service.PhoneRechargeService;
import com.cn.thinkx.ecom.front.api.phoneRecharge.util.PhoneRechargeOrderNotifyHttpClient;
import com.cn.thinkx.ecom.redis.core.constants.RedisConstants;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.qianmi.open.api.domain.elife.OrderDetailInfo;
import com.qianmi.open.api.response.BmRechargeMobileGetPhoneInfoResponse;

@Service("phoneRechargeService")
public class PhoneRechargeServiceImpl extends BaseServiceImpl<PhoneRechargeOrder> implements PhoneRechargeService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PhoneRechargeMapper phoneRechargeMapper;

	@Autowired
	private BMOrderService bmOrderService;

	@Autowired
	private BMOpenApiService bmOpenApiService;

	@Autowired
	private PhoneRechargeOrderNotifyHttpClient notifyHttpClient;

	@Autowired
	private JedisClusterUtils jedisClusterUtils;

	@Value("${OPEN_BM_NOTIFY_URL}")
	private String OPEN_BM_NOTIFY_URL;

	@Override
	public PhoneRechargeOrder innsertPhoneRechargeOrder(PhoneRechargeOrder phoneRechargeOrder) {
		try {
			phoneRechargeOrder.setrId(getPrimaryKey());
			phoneRechargeOrder.setSupplier(SupplierType.SupplierType1001.getCode());
			phoneRechargeOrder.setTransStat(PhoneRechargeTransStat.TransStat0.getCode());
			phoneRechargeOrder.setSupplierAmt(NumberUtils.RMMultiplyToDecimal(phoneRechargeOrder.getTelephoneFace(),
					PhoneRechargeConstants.PHONE_RECHARGE_FEE));
			phoneRechargeMapper.innsertPhoneRechargeOrder(phoneRechargeOrder);
			return phoneRechargeOrder;
		} catch (Exception e) {
			logger.error(" ## 新增手机充值数据出错 ", e);
			return null;
		}
	}

	@Override
	public int updatePhoneRechargeOrder(PhoneRechargeOrder phoneRechargeOrder) {
		return phoneRechargeMapper.updatePhoneRechargeOrder(phoneRechargeOrder);
	}

	@Override
	public PhoneRechargeOrder getPhoneRechargeOrderByPrimaryKey(String primaryKey) {
		return phoneRechargeMapper.selectByPrimaryKey(primaryKey);
	}

	@Override
	public String handlePhoneRechargeOrder(PhoneRechargeReq prReq, PhoneRechargeResp resp) {

		PhoneRechargeOrder prOrder = new PhoneRechargeOrder();

		prOrder.setrId(getPrimaryKey());
		prOrder.setChannelOrderNo(prReq.getChannelOrderNo());
		prOrder.setUserId(prReq.getUserId());
		prOrder.setPhone(prReq.getPhone());
		prOrder.setTelephoneFace(prReq.getTelephoneFace());
		prOrder.setSupplierAmt(
				NumberUtils.RMMultiplyToDecimal(prReq.getTelephoneFace(), PhoneRechargeConstants.PHONE_RECHARGE_FEE));
		prOrder.setSupplier(SupplierType.SupplierType1001.getCode());
		prOrder.setTransStat(PhoneRechargeTransStat.TransStat1.getCode());
		prOrder.setTransAmt(prReq.getTransAmt());
		prOrder.setOrderType(prReq.getOrderType());
		prOrder.setReqChannel(prReq.getReqChannel());
		prOrder.setNotifyUrl(prReq.getCallBack());
		prOrder.setAttach(prReq.getAttach());
		 prOrder.setResv1(prReq.getAccessToken());

		try {
			phoneRechargeMapper.innsertPhoneRechargeOrder(prOrder);// 插入订单信息
		} catch (Exception e) {
			logger.error(" ## 添加订单信息出错", e);
			resp.setCode(ExceptionEnum.ERROR_CODE);
			resp.setMsg(ExceptionEnum.ERROR_MSG);
			return JSONObject.toJSONString(resp);
		}

		try {
			// 是否调用话费充值接口开关
			String PHONE_RECHARGE_SWITCH = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,
					PhoneRechargeConstants.PHONE_RECHARGE_SWITCH);
			if (PHONE_RECHARGE_SWITCH.equals("0")) {
				// 调用力方话费充值接口
				BaseResult result = bmOrderService.handleHbkToBMPayBill(prOrder.getPhone(), prOrder.getTelephoneFace(),
						prOrder.getrId(), OPEN_BM_NOTIFY_URL, prReq.getAccessToken());

				if (ExceptionEnum.SUCCESS_CODE.equals(result.getCode())) {
					OrderDetailInfo orderDetailInfo = (OrderDetailInfo) result.getObject();
					prOrder.setSupplierOrdeNo(orderDetailInfo.getBillId());
					prOrder.setTransStat(PhoneRechargeTransStat.TransStat4.getCode());
					resp.setCode(ExceptionEnum.SUCCESS_CODE);
					resp.setMsg(ExceptionEnum.SUCCESS_MSG);
					resp.setOrderId(prOrder.getrId());
				} else {
					prOrder.setTransStat(PhoneRechargeTransStat.TransStat3.getCode());
					resp.setCode(ExceptionEnum.ERROR_CODE);
					resp.setOrderId(prOrder.getrId());
					resp.setMsg(result.getMsg());
				}

			}
			if (PHONE_RECHARGE_SWITCH.equals("1")) {
				prOrder.setTransStat(PhoneRechargeTransStat.TransStat3.getCode());
				resp.setCode(ExceptionEnum.ERROR_CODE);
				resp.setOrderId(prOrder.getrId());
			}
			updatePhoneRechargeOrder(prOrder);// 更新订单信息

		} catch (Exception e) {
			logger.error(" ## 手机充值出错 ", e);
			resp.setCode(ExceptionEnum.ERROR_CODE);
			resp.setMsg(ExceptionEnum.ERROR_MSG);
			return JSONObject.toJSONString(resp);
		}

		return JSONObject.toJSONString(resp);
	}

	@Override
	public void doPhoneRechargeOrder(PhoneRechargeOrder phoneRechargeOrder) {
		try {

			updatePhoneRechargeOrder(phoneRechargeOrder);

		} catch (Exception e) {
			logger.error(" ##  更新手机订单出错", e);
		}

		phoneRechargeOrder = getPhoneRechargeOrderByPrimaryKey(phoneRechargeOrder.getrId());

		// 异步通知
		try {
			if (!StringUtil.isNullOrEmpty(phoneRechargeOrder.getNotifyUrl())) {
				String result = notifyHttpClient.doPhoneRechargeOrderNotifyRequest(phoneRechargeOrder);
				logger.info("话费充值订单 -->  异步回调接口返回值[{}]", result);
			}
		} catch (Exception e) {
			logger.error(" ##  异步通知异常--------》", e);
		}

	}

	private String getPrimaryKey() {
		String primaryKey = RandomStrUtil.getOrderIdByUUId("H");
		return primaryKey;
	}

	@Override
	public String doGetPhoneInfo(String phone) {

		try {
			String accessToken = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,
					BMConstants.BM_ACCESS_TOKEN);

			BmRechargeMobileGetPhoneInfoResponse response = bmOpenApiService.handleGetPhoneInfo(phone, accessToken);

			logger.info("手机信息  [{}]", JSONObject.toJSONString(response.getPhoneInfo()));

			return JSONObject.toJSONString(response.getPhoneInfo());
		} catch (Exception e) {
			logger.error(" ##  查询手机信息异常--------》", e);
		}
		return null;
	}

	@Override
	public void doPhoneRechargeRefund(PhoneRechargeOrder phoneRechargeOrder) {

		try {
			String key = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,
					Constants.PHONE_RECHARGE_REQ_KEY);
			PhoneRechargeRefundReq req = new PhoneRechargeRefundReq();
			req.setOriOrderId(phoneRechargeOrder.getrId());
			req.setRefundOrderId(phoneRechargeOrder.getrId());
			req.setRefundAmount(phoneRechargeOrder.getTransAmt());
			req.setChannel(ChannelCode.CHANNEL8.toString());
			req.setRefundFlag(refundFalg.refundFalg1.getCode());
			req.setTimestamp(System.currentTimeMillis());
			req.setSign(SignUtil.genSign(req, key));
			String url = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,
					Constants.PHONE_RECHARGE_REFUND);
			logger.info("退款请求地址：[{}], 退款请求参数：[{}]", url, JSONObject.toJSONString(req));
			String result = HttpClientUtil.sendPost(url, JSONObject.toJSONString(req));// 发起退款请求
			logger.info("退款返回参数[{}]", result);
			PhoneRechargeRefundResp resp = JSONObject.parseObject(result, PhoneRechargeRefundResp.class);
			if (ExceptionEnum.SUCCESS_CODE.equals(resp.getCode())) {
				phoneRechargeOrder.setTransStat(PhoneRechargeTransStat.TransStat5.getCode());
			} else {
				phoneRechargeOrder.setTransStat(PhoneRechargeTransStat.TransStat6.getCode());
			}
			updatePhoneRechargeOrder(phoneRechargeOrder);// 更新订单信息
		} catch (Exception e) {
			logger.error(" ## 手机充值退款出错", e);
		}

	}

	@Override
	public List<PhoneRechargeOrder> getPhoneRechargeOrderNotSuccess() {
		return phoneRechargeMapper.getPhoneRechargeOrderNotSuccess();
	}

	@Override
	public String handleGetPhoneRechargeOrder(PhoneRechargeGetReq prReq, PhoneRechargeGetResp resp) {
		PhoneRechargeOrder phoneRechargeOrder = new PhoneRechargeOrder();
		phoneRechargeOrder.setChannelOrderNo(prReq.getChannelOrderNo());
		phoneRechargeOrder.setReqChannel(prReq.getReqChannel());
		try {
			phoneRechargeOrder = phoneRechargeMapper.getPhoneRechargeOrderByChannelOrderNo(phoneRechargeOrder);
		} catch (Exception e) {
			resp.setCode(ExceptionEnum.ERROR_CODE);
			resp.setMsg(ExceptionEnum.ERROR_MSG);
		}
		if (phoneRechargeOrder == null) {
			resp.setCode(ExceptionEnum.ERROR_CODE);
			resp.setMsg("订单不存在");
		} else {
			resp.setCode(ExceptionEnum.SUCCESS_CODE);
			if (PhoneRechargeTransStat.TransStat2.getCode().equals(phoneRechargeOrder.getTransStat())) {
				resp.setRechargeStat(RechargeState.RechargeState01.getCode());
			}
			if (PhoneRechargeTransStat.TransStat3.getCode().equals(phoneRechargeOrder.getTransStat())) {
				resp.setRechargeStat(RechargeState.RechargeState09.getCode());
			}
			if (PhoneRechargeTransStat.TransStat4.getCode().equals(phoneRechargeOrder.getTransStat())
					|| PhoneRechargeTransStat.TransStat1.getCode().equals(phoneRechargeOrder.getTransStat())) {
				resp.setRechargeStat(RechargeState.RechargeState00.getCode());
			}

			resp.setOrderId(phoneRechargeOrder.getrId());
			resp.setChannelOrderNo(phoneRechargeOrder.getChannelOrderNo());
			resp.setUserId(phoneRechargeOrder.getUserId());
			resp.setPhone(phoneRechargeOrder.getPhone());
			resp.setTelephoneFace(phoneRechargeOrder.getTelephoneFace());
			resp.setOrderType(phoneRechargeOrder.getOrderType());
			resp.setAttach(phoneRechargeOrder.getAttach());
			resp.setReqChannel(phoneRechargeOrder.getReqChannel());
		}
		return JSONObject.toJSONString(resp);
	}

}
