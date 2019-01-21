package com.ebeijia.zl.shop.service.pay.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.core.redis.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.facade.account.enums.WithDrawReceiverTypeEnum;
import com.ebeijia.zl.facade.account.req.*;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.ebeijia.zl.facade.account.vo.AccountLogVO;
import com.ebeijia.zl.facade.account.vo.AccountVO;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.dao.info.domain.TbEcomItxLogDetail;
import com.ebeijia.zl.shop.dao.info.service.ITbEcomItxLogDetailService;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomPayCard;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomPayCardService;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPayOrder;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPayOrderDetails;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomDmsRelatedDetailService;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPayOrderDetailsService;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPayOrderService;
import com.ebeijia.zl.shop.service.pay.IPayService;
import com.ebeijia.zl.shop.service.valid.impl.ValidCodeService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import com.ebeijia.zl.shop.utils.ShopUtils;
import com.ebeijia.zl.shop.vo.MemberInfo;
import com.ebeijia.zl.shop.vo.PayInfo;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static com.ebeijia.zl.common.utils.enums.TransCode.*;
import static com.ebeijia.zl.facade.account.exceptions.AccountBizException.ACCOUNT_AVAILABLEBALANCE_IS_NOT_ENOUGH;
import static com.ebeijia.zl.facade.account.exceptions.AccountBizException.ACCOUNT_COUPONBAL_IS_NOT_ENOUGH;

@Service
public class PayService implements IPayService {

    @Autowired
    private AccountQueryFacade accountQueryFacade;

    @Autowired
    private ShopUtils shopUtils;

    @Autowired
    private ITbEcomDmsRelatedDetailService dmsRelatedDetailDao;

    @Autowired
    private ITbEcomPayOrderService payOrderDao;

    @Autowired
    private ITbEcomPayOrderDetailsService payOrderDetailsDao;

    @Autowired
    private AccountTransactionFacade accountTransactionFacade;

    @Autowired
    private ValidCodeService validCodeService;

    @Autowired
    private ITbEcomPayCardService payCardDao;

    @Autowired
    private JedisUtilsWithNamespace jedis;

    @Autowired
    private ITbEcomItxLogDetailService logDetailDao;


    Logger logger = LoggerFactory.getLogger(PayService.class);

    @Override
    public int transferToCard(Long dealInfo, String validCode, Double session) {
        String dmsKey = IdUtil.getNextId();
        if (dealInfo <= 0) {
            throw new BizException(ResultState.NOT_ACCEPTABLE, "提现金额有误");
        }
        MemberInfo memberInfo = shopUtils.getSession();
        if (memberInfo == null) {
            throw new AdviceMessenger(ResultState.NOT_ACCEPTABLE, "参数异常");
        }
//        boolean valid = validCodeService.checkValidCode(PhoneValidMethod.PAY, memberInfo.getMobilePhoneNo(), validCode);
//        if (!valid) {
        //TODO
//            throw new AdviceMessenger(ResultState.NOT_ACCEPTABLE, "验证码有误");
//        }
        validCodeService.checkSession("transferToCard", session.toString());
        TbEcomPayCard payCard = new TbEcomPayCard();
        payCard.setMemberId(memberInfo.getMemberId());
        payCard = payCardDao.getOne(new QueryWrapper<>(payCard));
        if (payCard == null || StringUtils.isAnyEmpty(payCard.getBankCode(), payCard.getCardNumber(), payCard.getMobile(), payCard.getIdCard())) {
            throw new BizException(ResultState.NOT_ACCEPTABLE, "您绑定的卡信息有误");
        }
        AccountWithDrawReqVo req = new AccountWithDrawReqVo();
        req.setBankCode(payCard.getBankCode());
        req.setBankCity(payCard.getCity());
        req.setBankName(payCard.getBankName());
        req.setBankProvince(payCard.getProvince());
        req.setOrderName("转账");
        req.setReceiverCardNo(payCard.getCardNumber());
        req.setReceiverName(payCard.getUserName());
        req.setReceiverType(WithDrawReceiverTypeEnum.PERSON.getCode());
        //构造员工信息
        req.setUserChnlId(memberInfo.getOpenId());
        req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        req.setUserType(UserType.TYPE100.getCode());
        //构造操作渠道信息
        req.setTransChnl(TransChnl.CHANNEL9.toString());
        req.setTransAmt(BigDecimal.valueOf(dealInfo));
        req.setUploadAmt(BigDecimal.valueOf(dealInfo));
        req.setTransId(CW91.getCode());
        req.setDmsRelatedKey(dmsKey);
        BaseResult baseResult = null;
        try {
            baseResult = accountTransactionFacade.executeWithDraw(req);
        } catch (Exception e) {
            logger.error("支付失败", e);
            throw new BizException(ResultState.ERROR, "连接异常，请稍后再试");
        }

        //TODO INF
        String title = payCard.getUserName();
        String descinfo = payCard.getCardNumber();
        String image = "";
        String outId = dmsKey;
        String itxKey = (String) baseResult.getObject();


        //TODO DMS
        TbEcomItxLogDetail log = new TbEcomItxLogDetail();
        log.setMemberId(memberInfo.getMemberId());
        log.setTitle(title);
        log.setPrice(dealInfo);
        log.setDescinfo(descinfo);
        log.setOutId(outId);
        log.setItxKey(itxKey);
        log.setAmount(0);
        log.setImg(image);
        log.setSourceBid(SpecAccountTypeEnum.A01.getbId());
        logDetailDao.save(log);

        //判断result
        if (!baseResult.getCode().equals("00")) {
            if (baseResult.getCode().equals("99")) {
                throw new BizException(ResultState.BALANCE_NOT_ENOUGH, "余额不足");
            } else {
                throw new BizException(ResultState.BALANCE_NOT_ENOUGH, baseResult.getMsg());
            }
        }
        throw new AdviceMessenger(ResultState.OK, "成功！信息已提交");
    }

    @Override
    @ShopTransactional(propagation = Propagation.REQUIRES_NEW)
    public BaseResult payOrder(PayInfo payInfo, String openId, String dmsRelatedKey, String desc,String mchntCode) {

        //请求支付
        String result = "";
        List<AccountTxnVo> txnList = buildTxnVo(payInfo);
        BaseResult baseResult = executeConsume(txnList, openId, dmsRelatedKey, desc,mchntCode);
        Object object = baseResult.getObject();
        if (object instanceof String) {
            result = (String) object;
        } else {
            logger.error("支付失败,参数%s,%s,%s,%s", payInfo, openId, dmsRelatedKey, desc);
            throw new BizException(ResultState.BALANCE_NOT_ENOUGH, "账户余额不足");
        }
        //判断result
        logger.info(String.format("支付成功,参数%s,%s,%s,%s,结果%s", payInfo, openId, dmsRelatedKey, desc, result));

        //构造payOrder对象
        String memberId = shopUtils.getSession().getMemberId();
        String payOrderId = IdUtil.getNextId();
        TbEcomPayOrder pay = initPayOrderObject();
        pay.setMemberId(memberId);
        pay.setDmsRelatedKey(dmsRelatedKey);
        pay.setPayOrderId(payOrderId);
        pay.setOutTransNo(result);
        payOrderDao.save(pay);

        //构造payOrderDetail对象
        for (AccountTxnVo v : txnList) {
            TbEcomPayOrderDetails payOrderDetails = initPayOrderDetailObject();
            payOrderDetails.setDebitAccountCode(v.getTxnBId());
            payOrderDetails.setDebitAccountType(v.getTxnBId().substring(0, 1));
            payOrderDetails.setDebitPrice(v.getTxnAmt().longValue());
            payOrderDetails.setDmsRelatedKey(dmsRelatedKey);
            payOrderDetails.setOutOrderId(payInfo.getOrderId());
            payOrderDetails.setPayStatus("2");
            payOrderDetailsDao.save(payOrderDetails);
        }

        return baseResult;
    }


    private TbEcomPayOrderDetails initPayOrderDetailObject() {
        TbEcomPayOrderDetails payOrderDetails = new TbEcomPayOrderDetails();
        payOrderDetails.setPayDetailsId(IdUtil.getNextId());
        payOrderDetails.setCreateTime(System.currentTimeMillis());
        payOrderDetails.setCreateUser("ShopSystem");
        payOrderDetails.setDataStat("0");
        payOrderDetails.setLockVersion(0);
        return payOrderDetails;
    }

    private TbEcomPayOrder initPayOrderObject() {
        TbEcomPayOrder pay = new TbEcomPayOrder();
        pay.setCreateTime(System.currentTimeMillis());
        pay.setCreateUser("ShopSystem");
        pay.setDataStat("0");
        return pay;
    }

    @Override
    @ShopTransactional(propagation = Propagation.REQUIRES_NEW)
    public BaseResult payCoupon(AccountTxnVo vo, String openId, String dmsRelatedKey, String desc) {
        //请求支付
        String result = "";
        List<AccountTxnVo> txnList = new LinkedList<>();
        txnList.add(vo);

        AccountConsumeReqVo req = new AccountConsumeReqVo();
        //交易与渠道
        req.setTransId(CW20.getCode());
        req.setTransChnl(TransChnl.CHANNEL9.toString());
        //消费端用户识别
        req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        req.setUserChnlId(openId);
        req.setUserType(UserType.TYPE100.getCode());
        req.setTransList(txnList);
        req.setDmsRelatedKey(dmsRelatedKey);
        if (desc == null) {
            desc = "商城消费";
        }
        req.setTransDesc(desc);
        //TODO 添加收款方代码
        BaseResult baseResult = null;
        try {
            baseResult = accountTransactionFacade.executeConsume(req);
        } catch (Exception e) {
            logger.error("支付失败", e);
            logger.error("支付失败,参数%s,%s,%s,%s,%s", vo.getTxnAmt(), vo.getTxnBId(), openId, dmsRelatedKey, desc);
            throw new BizException(ResultState.ERROR, "连接异常，请稍后再试");
        }
        //判断result
        if (baseResult.getCode().equals(ACCOUNT_COUPONBAL_IS_NOT_ENOUGH.getCode())) {
            logger.info(String.format("支付失败,参数%s,%s,%s,%s,%s,结果%s", vo.getTxnAmt(), vo.getTxnBId(), openId, dmsRelatedKey, desc, result));
            throw new BizException(ResultState.BALANCE_NOT_ENOUGH, "支付失败，账户余额中可购买代金券的余额不足了");
        } else if (!baseResult.getCode().equals("00")) {
            throw new BizException(ResultState.BALANCE_NOT_ENOUGH, "支付失败，余额不足了");

        }
        logger.info(String.format("支付成功,参数%s,%s,%s,%s,%s,结果%s", vo.getTxnAmt(), vo.getTxnBId(), openId, dmsRelatedKey, desc, result));
        return baseResult;
    }


    @Override
    @ShopTransactional(propagation = Propagation.REQUIRES_NEW)
    public BaseResult payPhone(PayInfo vo, String openId, String dmsRelatedKey, String desc) {
        //请求支付
        String result = "";
        List<AccountTxnVo> accountTxnVos = buildTxnVo(vo);

        AccountConsumeReqVo req = new AccountConsumeReqVo();
        //交易与渠道
        req.setTransId(CW10.getCode());
        req.setTransChnl(TransChnl.CHANNEL8.toString());
        //消费端用户识别
        req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        req.setUserChnlId(openId);
        req.setUserType(UserType.TYPE100.getCode());
        req.setTransList(accountTxnVos);
        req.setDmsRelatedKey(dmsRelatedKey);
        if (desc == null) {
            desc = "商城消费";
        }
        req.setTransDesc(desc);
        BaseResult baseResult = null;
        try {
            baseResult = accountTransactionFacade.executeConsume(req);
        } catch (Exception e) {
            logger.error("支付失败", e);
            logger.error("支付失败,参数%s,%s,%s,%s,%s", vo.getCostA() + vo.getTypeA(), vo.getCostB() + vo.getTypeB(), openId, dmsRelatedKey, desc);
            throw new BizException(ResultState.ERROR, "连接异常，请稍后再试");
        }
        //判断result
        if (baseResult.getCode().equals(ACCOUNT_AVAILABLEBALANCE_IS_NOT_ENOUGH.getCode())) {
            logger.info(String.format("支付失败,参数%s,%s,%s,%s,%s,结果%s", vo.getCostA() + vo.getTypeA(), vo.getCostB() + vo.getTypeB(), openId, dmsRelatedKey, desc, result));
            throw new BizException(ResultState.BALANCE_NOT_ENOUGH, "支付失败，余额不足");
        } else if (!baseResult.getCode().equals("00")) {
            throw new BizException(ResultState.BALANCE_NOT_ENOUGH, baseResult.getMsg());

        }
        logger.info(String.format("支付成功,参数%s,%s,%s,%s,%s,结果%s", vo.getCostA() + vo.getTypeA(), vo.getCostB() + vo.getTypeB(), openId, dmsRelatedKey, desc, result));
        return baseResult;
    }


    @Override
    public PageInfo<AccountLogVO> listDeals(String range, String type, String method, String start, String limit) {

        MemberInfo memberInfo = shopUtils.getSession();
        String openId = memberInfo.getOpenId();

        AccountQueryReqVo req = new AccountQueryReqVo();
        req = setQueryDateRange(req, range);
        SpecAccountTypeEnum bId = SpecAccountTypeEnum.findByBId(type);

        if (bId != null) {
            req.setBId(bId.getbId());
        }

        req.setUserType(UserType.TYPE100.getCode());
        req.setUserChnlId(openId);
        req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        int startNum = 0;
        int pageSize = 20;
        try {
            startNum = Integer.valueOf(start);
            startNum = startNum < 0 ? 0 : startNum;
            pageSize = Integer.valueOf(limit);
            pageSize = pageSize > 100 ? 100 : pageSize;
        } catch (Exception ignore) {
        }
        //简单处理流水请求A、B分类
        if ("B".equals(type)) {
            req.setBId(null);
            PageInfo<AccountLogVO> result = accountQueryFacade.getAccountLogPage(0, 2000, req);
            List<AccountLogVO> list = result.getList();
            if (list == null) {
                list.removeIf(accountLogVO -> accountLogVO.getPriBId().charAt(0) == 'A');
            }
            //扩大查询范围
            return result;
        }
        if (method != null) {
            switch (method) {
                //交易类型 0：开户 1：加款 2：减款
                case "0":
                case "1":
                    req.setAccType(method);
                    break;
                case "2":
                    req.setTransIds(new String[]{CW10.getCode(), CW20.getCode(), CW71.getCode(), MB10.getCode()});
                    break;
                case "3":
                    req.setTransIds(new String[]{CW40.getCode(), MB40.getCode(), CW91.getCode()});
                default:
            }
            logger.info("交易类型筛选流水[{}]", req);
        }
        PageInfo<AccountLogVO> accountLogPage = accountQueryFacade.getAccountLogPage(startNum, pageSize, req);
//        accountLogPage.
        //TODO
        return accountLogPage;
    }

    private AccountQueryReqVo setQueryDateRange(AccountQueryReqVo req, String range) {
        if (range == null) {
            return req;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Long startDate = null;
        switch (range) {
            case "1":
                break;
            case "2":
                calendar.add(Calendar.MONTH, -1);
                break;
            case "3":
                calendar.add(Calendar.MONTH, -3);
                break;
            case "4":
                calendar.add(Calendar.MONTH, -6);
                break;
            case "5":
                calendar.add(Calendar.YEAR, -1);
                break;
            default:
                return req;
        }
        startDate = calendar.getTimeInMillis();
        req.setSDate(startDate);
        req.setEDate(System.currentTimeMillis());
        logger.info("时间筛选流水[{}]", req);
        return req;
    }


    @Override
    public List<AccountVO> listAccountDetail(String openId, String session) {
        //TODO 判断session
        return listAccountDetail(openId);
    }

    @Override
    public List<TbEcomPayOrderDetails> getDeal(String dms) {
        TbEcomPayOrderDetails details = new TbEcomPayOrderDetails();
        details.setDmsRelatedKey(dms);
        return payOrderDetailsDao.list(new QueryWrapper<>(details));
    }

    @Override
    public void phoneChargeReturn(PayInfo payInfo, TbEcomItxLogDetail log, String dmsKey) {
        AccountRefundReqVo vo = new AccountRefundReqVo();
        vo.setOrgItfPrimaryKey(log.getItxKey());
        vo.setDmsRelatedKey(dmsKey);
        vo.setTransChnl(TransChnl.CHANNEL8.getValue());
        vo.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        vo.setUserChnlId(shopUtils.getSession().getOpenId());
        vo.setTransId(TransCode.CW10.getCode());
        vo.setUserType(UserType.TYPE100.getCode());
        vo.setTransDesc(String.format("手机%s充值失败退款", log.getDescinfo()));
        vo.setTransList(buildTxnVo(payInfo));
        logger.info("手机充值退款开始：[{}]", vo);
        //TODO
        try {
            BaseResult baseResult = accountTransactionFacade.executeRefund(vo);
            if (baseResult==null){
                throw new BizException(ResultState.ERROR, "参数异常");
            }
            if (!baseResult.getCode().equals("00")) {
                throw new BizException(ResultState.ERROR, "参数异常");
            }
            logger.info("退款返回值：[{}]",baseResult.getObject());
            //INF
            log.setItxKey((String) baseResult.getObject());
            //TODO INF
            log = new TbEcomItxLogDetail();
            log.setTitle("充值失败退款");
            log.setMemberId(shopUtils.getSession().getMemberId());
            log.setOutId(payInfo.getOrderId());
            logDetailDao.save(log);
        } catch (Exception e) {
            logger.error("手机充值退款失败：[{}]", e);
        }

    }


    private List<AccountVO> listAccountDetail(String openId) {
        AccountQueryReqVo req = new AccountQueryReqVo();
        req.setUserType(UserType.TYPE100.getCode());
        req.setUserChnlId(openId);
        req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        List<AccountVO> list = accountQueryFacade.getAccountInfList(req);
        return list;
    }

    /**
     * 商城消费
     *
     * @param consumeList
     * @param openId
     * @param dmsRelatedKey
     * @param desc
     * @return
     * @throws Exception
     */
    private BaseResult executeConsume(List<AccountTxnVo> consumeList, String openId, String dmsRelatedKey, String desc,String mchntCode) {
        AccountConsumeReqVo req = new AccountConsumeReqVo();
        //交易与渠道
        req.setTransId(CW10.getCode());
        req.setTransChnl(TransChnl.CHANNEL6.toString());
        //消费端用户识别
        req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        req.setUserChnlId(openId);
        req.setUserType(UserType.TYPE100.getCode());
        req.setTransList(consumeList);
        req.setDmsRelatedKey(dmsRelatedKey);
        if (desc == null) {
            desc = "商城消费";
        }
        req.setTransDesc(desc);
        BaseResult result = null;
        try {
            result = accountTransactionFacade.executeConsume(req);
        } catch (Exception e) {
            logger.error("远端连接异常", e);
            throw new BizException(500, "服务器连接中断，请稍后再试");
        }
        return result;
    }

    /**
     * 简化的支付信息拆解
     *
     * @param payInfo
     * @return
     */
    private List<AccountTxnVo> buildTxnVo(PayInfo payInfo) {
        ArrayList<AccountTxnVo> result = new ArrayList<>();
        SpecAccountTypeEnum typeA = SpecAccountTypeEnum.findByBId(payInfo.getTypeA());
        if (typeA != null && payInfo.getCostA() != null && payInfo.getCostA() != 0) {
            AccountTxnVo accountTxnVo = new AccountTxnVo();
            accountTxnVo.setTxnBId(typeA.getbId());
            accountTxnVo.setUpLoadAmt(BigDecimal.valueOf(payInfo.getCostA()));
            accountTxnVo.setTxnAmt(BigDecimal.valueOf(payInfo.getCostA()));
            result.add(accountTxnVo);
        }
        SpecAccountTypeEnum typeB = SpecAccountTypeEnum.findByBId(payInfo.getTypeB());
        if (typeB != null && payInfo.getCostB() != null && payInfo.getCostB() != 0) {
            AccountTxnVo accountTxnVo = new AccountTxnVo();
            accountTxnVo.setTxnBId(typeB.getbId());
            accountTxnVo.setUpLoadAmt(BigDecimal.valueOf(payInfo.getCostB()));
            accountTxnVo.setTxnAmt(BigDecimal.valueOf(payInfo.getCostB()));
            result.add(accountTxnVo);
        }

        return result;
    }
}
