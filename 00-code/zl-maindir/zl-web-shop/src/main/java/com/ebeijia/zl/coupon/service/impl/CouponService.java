package com.ebeijia.zl.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
import com.ebeijia.zl.coupon.dao.domain.TbCouponProduct;
import com.ebeijia.zl.coupon.dao.domain.TbCouponTransFee;
import com.ebeijia.zl.coupon.dao.domain.TbCouponTransLog;
import com.ebeijia.zl.coupon.dao.service.ITbCouponHolderService;
import com.ebeijia.zl.coupon.dao.service.ITbCouponProductService;
import com.ebeijia.zl.coupon.dao.service.ITbCouponTransFeeService;
import com.ebeijia.zl.coupon.dao.service.ITbCouponTransLogService;
import com.ebeijia.zl.coupon.service.ICouponService;
import com.ebeijia.zl.shop.constants.PhoneValidMethod;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.service.valid.impl.ValidCodeService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopUtils;
import com.ebeijia.zl.shop.vo.MemberInfo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CouponService implements ICouponService {


    /**
     * Mooc数据
     */
    private static TbCouponHolder holder;
    private static TbCouponProduct product;
    private static TbCouponTransLog log;

    static {
        String union = IdUtil.getNextId();
        String code = IdUtil.getNextId();
        holder = new TbCouponHolder();
        holder.setCouponCode(code);
        holder.setAmount(20);
        holder.setCouponId("");
        holder.setCouponName("示例卡券100元");
        holder.setBId("B01");
        holder.setCouponDesc("可以用于兑换指定类型的商品");
        holder.setIconImage("/image/demo");
        holder.setPrice(10000L);
        holder.setTagAmount(10000L);
        holder.setTagUnit("元");
        product = new TbCouponProduct();
        product.setAvailableNum(100);
        product.setBId("B01");
        product.setCouponCode(code);
        product.setCouponName("示例卡券100元");
        product.setCouponDesc("可以用于兑换指定类型的商品");
        product.setIconImage("/image/demo");
        product.setPrice(10000L);
        product.setTagAmount(10000L);
        product.setTagUnit("元");
    }

    @Autowired
    private ShopUtils shopUtils;

    @Autowired
    private ITbCouponProductService couponProductDao;

    @Autowired
    private ITbCouponHolderService holderDao;

    @Autowired
    private ITbCouponTransLogService transLogDao;

    @Autowired
    private ITbCouponTransFeeService feeDao;

    @Autowired
    private ValidCodeService validCodeService;

    @Override
    public int couponShare(String vaildCode, String couponCode, Long price, Integer amount) {
        MemberInfo memberInfo = shopUtils.getSession();
        if (memberInfo == null) {
            throw new BizException(ResultState.NOT_ACCEPTABLE, "参数异常");
        }
        String phoneNo = memberInfo.getMobilePhoneNo();
        if (!validCodeService.checkValidCode(PhoneValidMethod.PAY, phoneNo, vaildCode)) {
            throw new BizException(ResultState.NOT_ACCEPTABLE, "验证码无效");
        }
        //判断金额
        TbCouponHolder query = new TbCouponHolder();
        query.setPrice(price);
        query.setCouponCode(couponCode);
        query.setDataStat("0");
        query.setTransStat("0");
        List<TbCouponHolder> holders = holderDao.list(new QueryWrapper<>(query));
        if (amount.compareTo(holders.size()) > 0) {
            throw new BizException(ResultState.BALANCE_NOT_ENOUGH, "您的卡券数量不足");
        }
        List<TbCouponHolder> targetHolders = holders.subList(0, amount);
        targetHolders.forEach(h -> {
            h.setLockVersion(h.getLockVersion() + 1);
            h.setTransStat("1");
        });
        //循环遍历，更新数据
        //提交事务，通讯远端账务系统
        //检查结果，处理异常回滚
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
            throw new AdviceMessenger(ResultState.NOT_ACCEPTABLE, "验证码无效");
        }
        return 0;
    }

    @Override
    public PageInfo<TbCouponProduct> listProduct(String bId, String order, Integer start, Integer limit) {
        List<TbCouponProduct> list = new LinkedList<>();
        list.add(product);

        TbCouponProduct query = new TbCouponProduct();
        SpecAccountTypeEnum type = SpecAccountTypeEnum.findByBId(bId);
        if (type == null) {
            throw new AdviceMessenger(ResultState.NOT_FOUND, "没有数据");
        }
        query.setBId(type.getbId());
        query.setDataStat("0");
        List<TbCouponProduct> list1 = couponProductDao.list(new QueryWrapper<>(query));

        return new PageInfo<>(list);
    }

    @Override
    public TbCouponProduct couponDetail(String couponCode) {
        TbCouponProduct query = new TbCouponProduct();
        query.setCouponCode(couponCode);
        query.setDataStat("0");
        query = couponProductDao.getOne(new QueryWrapper<>(query));
        TbCouponTransFee queryFee = new TbCouponTransFee();
        queryFee.setBId(query.getBId());
        TbCouponTransFee fee = feeDao.getOne(new QueryWrapper<>(queryFee));
        query.setFee(fee.getFee());
        return query;
    }

    @Override
    public PageInfo<TbCouponHolder> getHolder(String bId) {
        List<TbCouponHolder> list = new LinkedList<>();
        list.add(holder);

//        MemberInfo session = shopUtils.getSession();

        TbCouponHolder query = new TbCouponHolder();
//        query.setMemberId(session.getMemberId());
        query.setMemberId("9");
        query.setBId(bId);
        List<TbCouponHolder> list1 = holderDao.listCouponHolder(query);
        System.out.println(list1);

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
