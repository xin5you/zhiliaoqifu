package com.ebeijia.zl.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
import com.ebeijia.zl.coupon.dao.domain.TbCouponProduct;
import com.ebeijia.zl.coupon.dao.domain.TbCouponTransLog;
import com.ebeijia.zl.coupon.dao.service.ITbCouponHolderService;
import com.ebeijia.zl.coupon.dao.service.ITbCouponProductService;
import com.ebeijia.zl.coupon.dao.service.ITbCouponTransLogService;
import com.ebeijia.zl.coupon.service.ICouponService;
import com.ebeijia.zl.facade.account.req.AccountRechargeReqVo;
import com.ebeijia.zl.facade.account.req.AccountTxnVo;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.ebeijia.zl.shop.constants.PhoneValidMethod;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.service.pay.IPayService;
import com.ebeijia.zl.shop.service.valid.impl.ValidCodeService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopUtils;
import com.ebeijia.zl.shop.vo.BillingType;
import com.ebeijia.zl.shop.vo.MemberInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CouponService implements ICouponService {

    @Autowired
    private ShopUtils shopUtils;

    @Autowired
    private ITbCouponProductService couponProductDao;

    @Autowired
    private ITbCouponHolderService holderDao;

    @Autowired
    private ITbCouponTransLogService transLogDao;


    @Autowired
    private ValidCodeService validCodeService;

    @Autowired
    private AccountTransactionFacade accountTransactionFacade;

    @Autowired
    private IPayService payService;

    @Autowired
    private JedisClusterUtils jedis;

    private static Logger logger = LoggerFactory.getLogger(CouponService.class);

    @Override
    public int couponShare(String vaildCode, String couponCode, Long price, Integer amount) {
        MemberInfo memberInfo = shopUtils.getSession();
        if (memberInfo == null) {
            throw new BizException(ResultState.NOT_ACCEPTABLE, "参数异常");
        }
        String phoneNo = memberInfo.getMobilePhoneNo();
        if (!validCodeService.checkValidCode(PhoneValidMethod.PAY, phoneNo, vaildCode)) {
            throw new BizException(ResultState.NOT_ACCEPTABLE, "验证码错误");
        }
        //判断金额
        Long sumAmount = price * amount.longValue();
        if (sumAmount <= 0L) {
            throw new BizException(ResultState.NOT_ACCEPTABLE, "参数异常");
        }
        String dmsKey = IdUtil.getNextId();

        List<TbCouponHolder> holders = holderDao.couponShare(memberInfo.getMemberId(), couponCode, price, amount);

        TbCouponHolder holderExample = holders.get(0);
        String bId = SpecAccountTypeEnum.findByBId(holderExample.getBId()).getbId();

        String redisResult = jedis.hget("TB_BILLING_TYPE", bId);
        if (redisResult == null) {
            throw new BizException(ResultState.ERROR, "缓存异常");
        }
        BillingType billingType = null;
        try {
            billingType = ShopUtils.MAPPER.readValue(jedis.hget("TB_BILLING_TYPE", bId), BillingType.class);
        } catch (IOException e) {
            logger.error("缓存解析异常", e);
        }
        BigDecimal feeDecimal = billingType.getLoseFee();
        feeDecimal = BigDecimal.ONE.add(feeDecimal.negate());
        BigDecimal sumDecimal = BigDecimal.valueOf(sumAmount);
        BigDecimal txnAmt = sumDecimal.multiply(feeDecimal);



        if (txnAmt.compareTo(BigDecimal.valueOf(0.01D))<0){
            throw new BizException(ResultState.NOT_ACCEPTABLE,"您可得到的金额为0");
        }
        //记录交易请求，默认记录失败
        TbCouponHolder holder = holders.get(0);
        TbCouponTransLog transLog = new TbCouponTransLog();
        transLog.setCouponTxnId(IdUtil.getNextId());
        transLog.setDataStat("0");
        transLog.setLockVersion(0);
        transLog.setMemberId(memberInfo.getMemberId());
        transLog.setCreateTime(System.currentTimeMillis());
        transLog.setCreateUser("CouponSystem");
        //TODO DMS
        transLog.setTransResult("99");
        transLog.setCouponCode(couponCode);
        transLog.setCouponAmt(amount);
        transLog.setTransAmt(txnAmt);
        transLog.setOrgTransAmt(sumDecimal);
        transLog.setTransFee(billingType.getLoseFee());
        transLog.setTransFeeType(billingType.getbId());

        transLogDao.save(transLog);

        BaseResult baseResult = null;
        //提交事务，通讯远端账务系统
        try {

            AccountRechargeReqVo vo = new AccountRechargeReqVo();
            vo.setMobilePhone(memberInfo.getMobilePhoneNo());
            vo.setFromCompanyId("Coupon Trans");

            AccountTxnVo txnVo = new AccountTxnVo();
            txnVo.setTxnBId(bId);
            txnVo.setTxnAmt(sumDecimal);
            txnVo.setUpLoadAmt(sumDecimal);
            logger.info(String.format("提交卡券转让请求，金额%s，总计卡券数量%s，用户ID%s，卡券类型%s", txnAmt, amount, memberInfo.getMemberId(), couponCode));
            List<AccountTxnVo> transList = new ArrayList<>();
            transList.add(txnVo);

            vo.setTransList(transList);
            vo.setTransId(TransCode.CW90.getCode());
            vo.setTransChnl(TransChnl.CHANNEL9.toString());

            vo.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
            vo.setUserChnlId(memberInfo.getOpenId());
            vo.setUserType(UserType.TYPE100.getCode());
            vo.setDmsRelatedKey(dmsKey);
            vo.setPriBId(bId);
            vo.setUploadAmt(sumDecimal);
            vo.setTransAmt(sumDecimal);
            vo.setTransNumber(1);

            baseResult = accountTransactionFacade.executeRecharge(vo);
        } catch (Exception e) {
            logger.error("远端通讯异常", e);
            holderDao.couponShareRollback(holders);
            throw new BizException(ResultState.ERROR, "网络通讯异常");
        }
        //TODO DMS
        if (!baseResult.getCode().equals("00")) {
            holderDao.couponShareRollback(holders);
            throw new BizException(ResultState.BALANCE_NOT_ENOUGH, "余额不足");
        }
        //检查结果，处理异常回滚
        transLog.setTransResult(baseResult.getCode());
        transLog.setOrderId(baseResult.getObject().toString());
        transLogDao.updateById(transLog);

        return 200;
    }

    @Override
    public int buyCoupon(String vaildCode, String couponCode, Integer amount) {
        MemberInfo memberInfo = shopUtils.getSession();
        if (memberInfo == null) {
            throw new AdviceMessenger(ResultState.NOT_ACCEPTABLE, "参数异常");
        }
        String phoneNo = memberInfo.getMobilePhoneNo();
        if (!validCodeService.checkValidCode(PhoneValidMethod.PAY, phoneNo, vaildCode)) {
            throw new AdviceMessenger(ResultState.NOT_ACCEPTABLE, "验证码错误");
        }
        String dmsKey = IdUtil.getNextId();
        TbCouponProduct query = new TbCouponProduct();
        query.setCouponCode(couponCode);
        query = couponProductDao.getOne(new QueryWrapper<>(query));
        long sum = query.getPrice() * amount;
        if (sum < 0) {
            throw new AdviceMessenger(ResultState.NOT_ACCEPTABLE, "您购买的数量有误");
        }
        AccountTxnVo accountTxnVo = new AccountTxnVo();
        accountTxnVo.setUpLoadAmt(BigDecimal.valueOf(sum));
        accountTxnVo.setTxnAmt(BigDecimal.valueOf(sum));
        accountTxnVo.setTxnBId(query.getBId());

        int i = payService.payCoupon(accountTxnVo, memberInfo.getOpenId(), dmsKey, String.format("购买卡券%s", query.getCouponName()));

        if (i == 200) {
            TbCouponHolder h = new TbCouponHolder();
            h.setTransStat("0");
            h.setDataStat("0");
            h.setMemberId(memberInfo.getMemberId());
            h.setLockVersion(0);
            //TODO
            h.setCouponCode(query.getCouponCode());
            h.setCouponName(query.getCouponName());
            h.setPrice(query.getPrice());
            h.setBId(query.getBId());
            h.setCreateTime(System.currentTimeMillis());
            h.setCreateUser("CouponSystem");
            for (int k = 0; k < amount; k++) {
                h.setCouponId(IdUtil.getNextId());
                holderDao.save(h);
            }
        }
        //TODO DMS
        return 200;
    }

    @Override
    public PageInfo<TbCouponProduct> listProduct(String bId, String order, Integer start, Integer limit) {
        if (limit == null || limit > 100) {
            limit = Integer.valueOf(20);
        }
        if (start == null) {
            start = Integer.valueOf(0);
        }
        TbCouponProduct query = new TbCouponProduct();
        SpecAccountTypeEnum type = SpecAccountTypeEnum.findByBId(bId);
        if (type == null) {
            throw new AdviceMessenger(ResultState.NOT_FOUND, "没有数据");
        }
        query.setBId(type.getbId());
        query.setDataStat("0");
        PageHelper.startPage(start, limit);
        List<TbCouponProduct> list = couponProductDao.list(new QueryWrapper<>(query));

        return new PageInfo<>(list);
    }

    @Override
    public TbCouponProduct couponDetail(String couponCode) {
        TbCouponProduct query = new TbCouponProduct();
        query.setCouponCode(couponCode);
        query.setDataStat("0");
        query = couponProductDao.getOne(new QueryWrapper<>(query));
        String bId = SpecAccountTypeEnum.findByBId(query.getBId()).getbId();
        String redisResult = jedis.hget("TB_BILLING_TYPE", bId);
        if (redisResult == null) {
            throw new BizException(ResultState.ERROR, "缓存异常");
        }
        BillingType billingType = null;
        try {
            billingType = ShopUtils.MAPPER.readValue(jedis.hget("TB_BILLING_TYPE", bId), BillingType.class);
            query.setFee(billingType.getLoseFee());
        } catch (IOException e) {
            logger.error("缓存解析异常", e);
        }
        return query;
    }

    @Override
    public PageInfo<TbCouponHolder> getHolder(String bId, Integer start, Integer limit) {
        if (limit == null || limit > 100) {
            limit = Integer.valueOf(20);
        }
        if (start == null) {
            start = Integer.valueOf(0);
        }

        PageHelper.startPage(start, limit);
        MemberInfo memberInfo = shopUtils.getSession();
        if (memberInfo == null) {
            throw new AdviceMessenger(ResultState.NOT_ACCEPTABLE, "参数异常");
        }
        TbCouponHolder query = new TbCouponHolder();
        query.setMemberId(memberInfo.getMemberId());
        query.setDataStat("0");
        query.setTransStat("0");
        query.setBId(bId);
        List<TbCouponHolder> list = holderDao.listCouponHolder(query);

        return new PageInfo<>(list);
    }

    @Override
    public TbCouponHolder getHolderByCouponCode(String couponCode, Long price) {
        MemberInfo memberInfo = shopUtils.getSession();
        if (memberInfo == null) {
            throw new AdviceMessenger(ResultState.NOT_ACCEPTABLE, "参数异常");
        }
        TbCouponHolder query = new TbCouponHolder();
        query.setCouponCode(couponCode);
        query.setMemberId(memberInfo.getMemberId());
        query.setPrice(price);
        return holderDao.getCouponHolder(query);
    }
}
