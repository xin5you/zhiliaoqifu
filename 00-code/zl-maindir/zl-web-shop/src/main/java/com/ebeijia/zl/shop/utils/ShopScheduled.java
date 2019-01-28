package com.ebeijia.zl.shop.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.core.redis.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomOrderProductItem;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPayOrder;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfShopOrder;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomOrderProductItemService;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPayOrderService;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPlatfOrderService;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPlatfShopOrderService;
import com.ebeijia.zl.shop.service.goods.IProductService;
import com.ebeijia.zl.shop.service.pay.IPayService;
import com.ebeijia.zl.shop.vo.PayDealInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Component
public class ShopScheduled {

    private Logger logger = LoggerFactory.getLogger(ShopScheduled.class);

    private static final String SIGN = UUID.randomUUID().toString();

    @Autowired
    private JedisUtilsWithNamespace jedis;

    @Autowired
    private ITbEcomPlatfShopOrderService shopOrderDao;

    @Autowired
    private ITbEcomPlatfOrderService platfOrderDao;

    @Autowired
    private ITbEcomOrderProductItemService orderProductItemDao;

    @Autowired
    private ITbEcomPayOrderService payOrderDao;

    @Autowired
    private IPayService payService;

    @Autowired
    private IProductService productService;

    @Value("${scheduled.master:true}")
    Boolean master;

    /**
     * 定时刷新确认收货的订单数据（凌晨0点启动）
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    @ShopTransactional
    public void confirmOrder() {
        if (master) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, -30);
                long endTime = calendar.getTimeInMillis();

                calendar.add(Calendar.MINUTE, -30);
                long startTime = calendar.getTimeInMillis();

                TbEcomPlatfOrder order = new TbEcomPlatfOrder();
                order.setPayStatus("0");
                QueryWrapper<TbEcomPlatfOrder> queryWrapper = new QueryWrapper<>(order);
                queryWrapper.between("create_time", startTime, endTime);
                List<TbEcomPlatfOrder> list = platfOrderDao.list(queryWrapper);
                logger.info("## 定时任务获取到[{}]个订单", list.size());
                if (list == null || list.size()==0) {
                    return;
                }
                list.stream().forEach(o ->
                {
                    o.setPayStatus("8");
                    TbEcomPlatfShopOrder query = new TbEcomPlatfShopOrder();
                    query.setOrderId(o.getOrderId());
                    TbEcomPlatfShopOrder updataInf = new TbEcomPlatfShopOrder();
                    updataInf.setSubOrderStatus("27");
                    shopOrderDao.update(updataInf, new QueryWrapper<>(query));
                    updataInf = shopOrderDao.getOne(new QueryWrapper<>(query));
                    TbEcomOrderProductItem queryItem = new TbEcomOrderProductItem();
                    queryItem.setSOrderId(updataInf.getSOrderId());
                    TbEcomOrderProductItem productItem = orderProductItemDao.getOne(new QueryWrapper<>(queryItem));
                    productService.productStoreRecover(productItem.getProductId(), productItem.getProductNum());

                });
                platfOrderDao.updateBatchById(list);
                logger.info("## 定时取消订单***************触发时间：[{}]", DateUtil.getCurrentDateStr(DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
            } catch (Exception e) {
                logger.error("## 自动取消订单出错", e);
                throw new BizException("过期订单处理异常");
            }
        }
    }



    /**
     * 定时查询确认收货的订单数据（凌晨0点启动）
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    @ShopTransactional
    public void checkWxPayment() {
        if (master) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, -5);
                long endTime = calendar.getTimeInMillis();

                calendar.add(Calendar.MINUTE, -6);
                long startTime = calendar.getTimeInMillis();
                QueryWrapper<TbEcomPayOrder> queryWrapper = new QueryWrapper<>();
                queryWrapper.between("create_time", startTime, endTime);
                List<TbEcomPayOrder> list = payOrderDao.list(queryWrapper);
                logger.info("## 定时任务获取到[{}]个支付请求", list.size());
                if (list == null || list.size()==0) {
                    return;
                }
                list.stream().forEach(o ->
                {
                    if (o.getDmsRelatedKey().length()==16&&o.getOutTransNo()==null){
                        try {
                            PayDealInfo deal = payService.getDeal(o.getDmsRelatedKey());
                            if (deal==null){
                                throw new BizException(ResultState.ERROR,"状态未知");
                            }
                            o.setResv4(deal.getPayStatus());
                        }catch (BizException e){
                            o.setResv4("1");
                            o.setResv5(e.getMsg());
                        }
                    }
                });
                payOrderDao.updateBatchById(list);
                logger.info("## 定时查询支付状态***************触发时间：[{}]", DateUtil.getCurrentDateStr(DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
            } catch (Exception e) {
                logger.error("## 定时查询支付状态出错", e);
                throw new BizException("定时查询支付状态异常");
            }
        }
    }
}
