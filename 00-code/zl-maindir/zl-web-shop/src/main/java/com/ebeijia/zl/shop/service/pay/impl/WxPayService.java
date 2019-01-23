package com.ebeijia.zl.shop.service.pay.impl;

import com.alibaba.fastjson.JSON;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.payment.core.constant.PayConstant;
import com.ebeijia.zl.payment.core.entity.EBJPushOrder;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.controller.PayController;
import com.ebeijia.zl.shop.service.pay.IWxPayService;
import com.ebeijia.zl.shop.vo.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

@Service
public class WxPayService implements IWxPayService {

    private Logger logger = LoggerFactory.getLogger(PayController.class);

    /**
     * 支付成功回调HTML页面
     *
     * @param success
     * @return
     */
    public String wxPaySuccess(@Valid WxPaySuccessDTO success) {
        String order = "";
        if (order == null) {
            throw new BizException(ResultState.NOT_FOUND, "没有订单");
        }
        String url = null;

//        //有第四方订单号就是第四方平台的
//        if (order.getOutTradeNo() != null && order.getOutTradeNo().length() > 0) {
//            String successUrl = order.getSuccessUrl();
//            if (StringUtils.isEmpty(successUrl)) {
//                throw new BizException(ResultState.NOT_FOUND, "没有URL");
//            }
//            {//拼接参数回掉
//                Map param = new HashMap();
//                param.put("out_trade_no", "");
//                param.put("mch_id", "");
//                url = getUrlParamsByMap("", param);
//                logger.info("第四方支付成功回调地址和参数:" + url);
//            }
//        } else {
//            //回掉自己平台
//            Map param = new HashMap();
//            param.put("merid", success.getMerid());
//            param.put("merchantOutOrderNo", success.getMerchantOutOrderNo());
//            param.put("payType", success.getPayType());
//            param.put("orderId", "");
//            param.put("mchtId", "");
//            url = getUrlParamsByMap(DeployEnactment.deploy().getPayDomainUrl() + "/paySuccess", param);
//            logger.info("回调自己平台！" + url);
//        }
        return url;
    }


    /**
     * 支付成回掉-分发
     *
     * @param back
     * @return
     */
    public String wxPayBack(@Valid WxPayBackDTO back) {
        String orderId = back.getMerchantOutOrderNo();
//        Order order = orderRepository.findByPrepayId();
        if (orderId == null) {
            throw new BizException(ResultState.NOT_FOUND, "");
        }

        //效验参数
        WxPayBackMsgDTO msgDTO = checkData(0, back);

        //没有第四方订单号就是我们自己平台的
        if (true) {

            //支付成功更新订单+增加流水
            if (back.getPayResult() != null && back.getPayResult().equalsIgnoreCase("true"))
//                yunPayOrderService.paySuccess(order.getOrderId(), order.getMchtId(), Double.parseDouble(msgDTO.getPayMoney()), msgDTO.getTradeDate());
                //回掉通知第四方支付
                logger.info("回掉通知第四方支付");
//            return yunPayBack("order.getNotifyUrl()", order, msgDTO, back);
        }
        //自己平台
        logger.info("自己平台回调");
        return callback(back.getPayResult(), back.getMerchantOutOrderNo(), back.getOrderNo());
    }


    /**
     * 申请支付
     *
     * @param payInfo  支付信息
     * @param mchtSett 商户支付密钥
     */
    public String applyPay(WxPayReqDTO payInfo, String mchtSett) {
        //生成系统订单
//        Order order = new Order();
//        {
//            String orderId = payInfo.getMch_id() + String.valueOf(System.currentTimeMillis());
//            order.setOrderId(orderId);
//            order.setMchtId(payInfo.mch_id);
//            //申请芸付默认用户ID为0
//            order.setUsrId(0);
//            //已下单
//            order.setOrderState(OrderStatusEnum.Placed.toString());
//            order.setOrderDate(payInfo.getOrder_time());
//            order.setSuccessUrl(payInfo.getSuccess_url());
//            order.setNotifyUrl(payInfo.getNotify_url());
//            order.setAmt(payInfo.total_fee / 100.00);
//            order.setNoSlae(order.getAmt());
//            order.setOutTradeNo(payInfo.getOut_trade_no());
//            order.setOrderDate(payInfo.getOrder_time());
//            order.setCommState(OrderCommentEnum.Normal.toString());
//            order.setTxnCd(TxnCdEnum.Order.toString());
//            order.setPrepayId(String.valueOf(Snowflake.getInstance(0).generateID()));
//            order.setTrl("ebj");
//            order.setPayType("wechat");
//            logger.info("保存库里的订单信息:" + order.toString());
//            orderRepository.save(order);
//        }
        //申请支付
        //TODO 数据
        EBJPushOrder epo = new EBJPushOrder.Builder("mchId", mchtSett)
                .merchantOutOrderNo("orderId")
                .noncestr(PayConstant.noncestr)
                .orderMoney("price")
                .orderTime("time")
                .build();
        return PayConstant.Y2_JSPAY_URL + epo.toString();
    }


    /**
     * 查询一码通付订单-更新本地订单
     *
     * @param orderID
     * @param mchtID
     * @param merID
     * @param merKey
     */
    private boolean queryYiMaTongFuOrderMsg(String orderID, int mchtID, String merID, String merKey) {
        RequestWxQueryDTO requestWxQueryDTO = weChatPayQuery(orderID, merID, merKey);
        if (requestWxQueryDTO.getPayResult() != null && requestWxQueryDTO.getPayResult().equalsIgnoreCase("true")) {
//            yunPayOrderService.paySuccess(orderID, mchtID, Double.parseDouble(requestWxQueryDTO.getOrderMoney()), requestWxQueryDTO.getPayTime());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 回调我们自己的芸券付
     *
     * @param payResult 支付状态
     * @param dmsKey    支付订单号
     * @param orderNo   订单号
     * @return
     */
    public String callback(String payResult, String dmsKey, String orderNo) {
        logger.info("payResult = [" + payResult + "], dmsKey = [" + dmsKey + "], orderNo = [" + orderNo + "]");
        final String status = "true";
        // 回调成功
        if (status.equals(payResult)) {
            try {
//                processingService.orderProcessing(dmsKey, orderNo);
                logger.info("pay success");
                return "success";
            } catch (Exception e) {
                logger.info("pay fail");
                logger.error("支付失败状态改变失败", e);
                return "";
            }
        }
        return "";
    }

    /**
     * 效验参数-失败抛出异常
     *
     * @param mchtId
     * @param back
     */
    public WxPayBackMsgDTO checkData(int mchtId, WxPayBackDTO back) {
        //解密数据
//        MchtSett mchtSett = mchtSettService.getById(mchtId);
//        if (mchtSett==null){
//            throw new BizException(01,"解密失败");
//        }
        //TODO 根据不同商户处理不同的key
        String mchtSettKey = "";
        //拼接字符串
        String paramMD5 = back.getSignMD5(mchtSettKey);
        if (!paramMD5.equalsIgnoreCase(back.getSign())) {
            throw new BizException(01, "参数校验错误");
        }
        WxPayBackMsgDTO msgDTO = JSON.parseObject(back.getMsg(), WxPayBackMsgDTO.class);
        if (msgDTO == null) {
            throw new BizException(01, "Msg参数错误");
        }
        return msgDTO;
    }


    /**
     * 将map转换成url
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(String url, Map<String, Object> map) {
        if (map == null || map.size() < 1)
            return url;
        StringBuffer sb = new StringBuffer(url + "?");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        return StringUtils.substringBeforeLast(sb.toString(), "&");
    }


    /**
     * 查询支付状态
     *
     * @param merchantOutOrderNo 订单号码
     * @param merid              支付商户ID
     * @param key                支付商户密钥
     * @return 失败返回NULL
     */
    public RequestWxQueryDTO weChatPayQuery(String merchantOutOrderNo, String merid, String key) {
        StringBuilder param = new StringBuilder();
        param.append("merchantOutOrderNo" + "=" + merchantOutOrderNo);
        param.append("&merid=" + merid);
        param.append("&noncestr=" + UUID.randomUUID().toString().replaceAll("-", ""));

        {//计算MD5
            String paramURL = param.toString() + "&key=" + key;
            String md5 = DigestUtils.md5Hex(paramURL);

            param.append("&sign=" + md5);
        }
        String url = PayConstant.EBJ_PAY_QUERY + "?" + param.toString();

        RequestWxQueryDTO requestWxQueryDTO = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            requestWxQueryDTO = restTemplate.postForObject(url, null, RequestWxQueryDTO.class);
        } catch (Exception e) {
            requestWxQueryDTO = null;
        }
        return requestWxQueryDTO;
    }

    /**
     * 第四方支付成功回掉
     *
     * @param url    地址
     * @param order  订单
     * @param msgDTO 支付消息
     * @param back   主要参数
     * @return
     */
//    public String yunPayBack(String url, Order order, WxPayBackMsgDTO msgDTO, WxPayBackDTO back) {
//        try {
//
//            Map<String, String> param = new HashMap<String, String>();
//            param.put("out_trade_no", order.getOutTradeNo());
//            param.put("mch_id", String.valueOf(order.getMchtId()));
//
//            int pay_money = (int) (order.getAmt() * 100);
//            param.put("pay_money", String.valueOf(pay_money));
//            param.put("pay_time", msgDTO.getTradeDate());
//            param.put("order_time", order.getOrderDate());
//            param.put("noncestr", PayConstant.noncestr);
//            param.put("order_no", order.getOrderId());
//            param.put("pay_result", back.getPayResult());
//
//
//            //拼接url
//            String urlParam = CalculateUtil.sortSplice(param);
//            //生成MD5
//            String md5 = DigestUtils.md5Hex(urlParam);
//            //拼接MD5 钱ing
//
//            StringBuilder reqUrl = new StringBuilder(url);
//            reqUrl.append("?");
//            reqUrl.append(urlParam);
//            reqUrl.append("&sign=" + md5);
//
//            RestTemplate restTemplate = new RestTemplate();
//            return restTemplate.getForObject(reqUrl.toString(), String.class);
//        } catch (Exception e) {
//            logger.error("请求第四方支付失败请稍后再试", e);
//            return "PAY-ERROR";
//        }
//
//    }

}
