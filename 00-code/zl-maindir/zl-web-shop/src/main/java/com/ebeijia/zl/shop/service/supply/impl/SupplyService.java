package com.ebeijia.zl.shop.service.supply.impl;

import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.shop.constants.PhoneValidMethod;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.dao.info.domain.TbEcomItxLogDetail;
import com.ebeijia.zl.shop.dao.info.service.ITbEcomItxLogDetailService;
import com.ebeijia.zl.shop.service.supply.ISupplyService;
import com.ebeijia.zl.shop.service.valid.IValidCodeService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopUtils;
import com.ebeijia.zl.shop.vo.MemberInfo;
import com.ebeijia.zl.shop.vo.PayInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SupplyService implements ISupplyService {

    @Autowired
    private ITbEcomItxLogDetailService logDetailDao;

    @Autowired
    private IValidCodeService validCodeService;

    @Autowired
    private ShopUtils shopUtils;

    private static Logger logger = LoggerFactory.getLogger(SupplyService.class);

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


        //TODO INF
        String title = "手机充值";
        String descinfo = phone;
        String image = "";
        String outId = dmsKey;
        String itxKey = IdUtil.getNextId();

        //TODO DMS
        TbEcomItxLogDetail log = new TbEcomItxLogDetail();
        log.setTitle(title);
        log.setPrice(amount.longValue());
        log.setDescinfo(descinfo);
        log.setOutId(outId);
        log.setItxKey(itxKey);
        log.setAmount(amount);
        log.setImg(image);
        log.setSourceBid(SpecAccountTypeEnum.B06.getbId());
        logDetailDao.save(log);
        logger.info("记录日志详情：", log);

        return null;
    }

}
