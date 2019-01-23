package com.ebeijia.zl.payment.core.constant;

import java.util.UUID;

/**
 * 支付常量
 *
 * @author xiong.wang
 * @date 2017/7/14
 */
public class PayConstant {
    //易生支付测试链接
    public static final String PX_BETA_URL = "http://wx.dianpayer.com/gateway.do";
    //易生支付生产链接
    public static final String PX_ONLINE_URL = "https://wepay.mpay.cn/gateway.do";

    public static final String EBJ_BETA_URL = "http://pay.ebjfinance.com/payWeb/wechatcompactpay.php?";
    public static final String EBJ_ONLINE_URL = "http://pay.ebjfinance.com/wechatcompactpay.php?";
    public static final String EBJ_PAY_QUERY = "http://pay.ebjfinance.com/weixin/wechatpayquery.php";
    public static final String Y2_JSPAY_URL = "https://pay.ebjfinance.com/wechatjspay.php?";

    public transient static String noncestr = UUID.randomUUID().toString().trim().replaceAll("-", "");

    private static final String DOMAIN = "http://pay.ebjfinance.com";

    public static String PAY_WECHAT_URL = DOMAIN + "/wechatpay.php?";

    public static String PAY_ALIPAY_URL = DOMAIN + "/alipay.php?";

    public static String PAY_RESULT_URL = DOMAIN + "/weixin/wechatpayquery.php";

    public static String WXPAY = "wxpay";

    public static String COMPACT = "compact";

    public static String NORMAL = "normal";

    public static String ALIPAY = "alipay";

    public static String EBJ = "EBJ";

}
