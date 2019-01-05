package com.ebeijia.zl.shop.utils;

import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.core.redis.utils.JedisUtilsWithNamespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShopScheduled {

    private Logger logger = LoggerFactory.getLogger(ShopScheduled.class);

    private static final String SIGN = UUID.randomUUID().toString();

    @Autowired
    JedisUtilsWithNamespace jedis;

    @Value("${scheduled.master:true}")
    Boolean master;
    /**
     * 定时刷新确认收货的订单数据（凌晨0点启动）
     *
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void confirmOrder() {
        if (master) {
            try {
			logger.info("## 定时刷新测试***************触发时间：[{}]",DateUtil.getCurrentDateStr(DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
            } catch (Exception e) {
                logger.error("## 自动取消订单出错", e);
            }
        }
    }


}
