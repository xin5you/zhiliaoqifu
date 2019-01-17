package com.ebeijia.zl.shop.service.supply.impl;

import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.MD5SignUtils;
import com.ebeijia.zl.shop.constants.PhoneValidMethod;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.dao.info.domain.TbEcomItxLogDetail;
import com.ebeijia.zl.shop.dao.info.service.ITbEcomItxLogDetailService;
import com.ebeijia.zl.shop.service.pay.IPayService;
import com.ebeijia.zl.shop.service.supply.ISupplyService;
import com.ebeijia.zl.shop.service.valid.IValidCodeService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopUtils;
import com.ebeijia.zl.shop.vo.MemberInfo;
import com.ebeijia.zl.shop.vo.PayInfo;
import com.ebeijia.zl.shop.vo.TeleReqVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;


@Service
public class SupplyService implements ISupplyService {

    @Autowired
    private ITbEcomItxLogDetailService logDetailDao;

    @Autowired
    private IValidCodeService validCodeService;

    @Autowired
    private ShopUtils shopUtils;

    @Autowired
    private IPayService payService;

    private static Logger logger = LoggerFactory.getLogger(SupplyService.class);

    @Value("${phone.charge.url:http://192.168.2.110:10701/web-api/api/recharge/mobile/payment}")
    private String phoneChargeUrl;

    @Override
    public Integer phoneCharge(String phone, Integer amount, String validCode, PayInfo payInfo, String session) {
        String dmsKey = IdUtil.getNextId();
        if (amount <= 0) {
            throw new BizException(ResultState.NOT_ACCEPTABLE, "充值金额有误");
        }

        MemberInfo memberInfo = shopUtils.getSession();
        if (memberInfo == null) {
            throw new AdviceMessenger(ResultState.NOT_ACCEPTABLE, "参数异常");
        }
        boolean valid = validCodeService.checkValidCode(PhoneValidMethod.PAY, memberInfo.getMobilePhoneNo(), validCode);
        if (!valid) {
            throw new AdviceMessenger(ResultState.NOT_ACCEPTABLE, "验证码有误");
        }
        //TODO 这里amount是分，需要注意
        checkPayment(payInfo,amount);

        //TODO INF
        String image = "手机充值";
        String descinfo = phone;
        String title = "通讯服务";
        String outId = dmsKey;

        BigDecimal decimalAmount = BigDecimal.valueOf(amount);
        String upAmount = decimalAmount.multiply(BigDecimal.valueOf(0.01D)).toString();

        //TODO DMS
        TbEcomItxLogDetail log = new TbEcomItxLogDetail();
        log.setTitle(title);
        log.setPrice(amount.longValue());
        log.setDescinfo(descinfo);
        log.setOutId(outId);
        log.setImg(image);
        log.setAmount(0);
        log.setSourceBid(SpecAccountTypeEnum.B06.getbId());
        logger.info("记录日志详情：", log);
        BaseResult baseResult = payService.payPhone(payInfo, memberInfo.getOpenId(), dmsKey, "手机充值");

        if (!baseResult.getCode().equals("00")){
            throw new BizException(ResultState.ERROR,"网络不稳定，请稍后再试");
        }
        log.setItxKey((String) baseResult.getObject());
        logDetailDao.save(log);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        TeleReqVO vo = new TeleReqVO();
        vo.setChannelId("a5a41d8e-66a7-4ebe-bac2-7c280d666666");
        paramsMap.add("channelId",vo.getChannelId());
        vo.setMethod("hkb.api.mobile.charge");
        paramsMap.add("method",vo.getMethod());
        vo.setV("1.0");
        paramsMap.add("v",vo.getV());
//       paramsMap.add("channelOrderId",vo.getChannelOrderId());
//        vo.setChannelOrderId(IdUtil.getNextId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(System.currentTimeMillis());
        vo.setTimestamp(dateTime);
        paramsMap.add("timestamp",vo.getTimestamp());
        vo.setRechargePhone(phone);
        paramsMap.add("rechargePhone",vo.getRechargePhone());
        vo.setRechargeAmount(upAmount);
        paramsMap.add("rechargeAmount",vo.getRechargeAmount());
//        vo.setOuterTid("3e7e344e-0ca9-4d4d-8aad-627daaed67eb");
        vo.setOuterTid(dmsKey);
        paramsMap.add("outerTid",vo.getOuterTid());
        vo.setCallback("http://api.happy8888.com.cn/web-api/api/recharge/notify/bmHKbCallBack");
        paramsMap.add("callback",vo.getCallback());
        vo.setSign(MD5SignUtils.genSign(vo, "key", "a5a41d8e-66a7-4ebe-bac2-7c280d888888", new String[]{"sign", "serialVersionUID"}, null));
        paramsMap.add("sign",vo.getSign());

        logger.info("手机充值VO:[{}]",vo);
        RestTemplate template = new RestTemplate();
        //TODO 测试用URL
        BaseResult postResult = template.postForObject("http://192.168.2.110:10701/web-api/api/recharge/mobile/payment", paramsMap, BaseResult.class);
        if (postResult == null) {
            throw new BizException(ResultState.ERROR, "网络不稳定，请稍后");
        }
        logger.info(String.format("充值接口返回值：%s,%s,%s",postResult.getCode(),postResult.getMsg(),postResult.getObject()));
//        log.setImg(body.getObject().toString());
//        logDetailDao.updateById(log);
        throw new AdviceMessenger(200,"已提交，根据运营商不同到账时间约5-30分钟。");
    }

    private void checkPayment(PayInfo payInfo, Integer amount) {
        Long sum = 0L;
        if (payInfo.getCostA()!=null && payInfo.getTypeA()!=null){
            sum += payInfo.getCostA();
        }
        if (payInfo.getCostB()!=null && payInfo.getTypeB()!=null){
            SpecAccountTypeEnum byBId = SpecAccountTypeEnum.findByBId(payInfo.getTypeB());
            if (byBId == SpecAccountTypeEnum.B06){
                sum += payInfo.getCostB();
            }
        }
        if (sum.intValue()!=amount){
            throw new BizException(ResultState.NOT_ACCEPTABLE,"您的支付信息有误");
        }
    }

}
