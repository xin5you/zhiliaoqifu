package com.ebeijia.zl.conf;

import com.alibaba.fescar.spring.annotation.GlobalTransactionScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: zhuqi
 * @Description  Fescar全局事务配置
 * @Date Created in 2019/1/30 10:28
 */
@Configuration
public class FescarConfig {


    /**
     * 配置全局事务扫描
     * @Param:
     * @Return:
     */
    @Bean
    public GlobalTransactionScanner globalTransactionScanner(){
        return new GlobalTransactionScanner("zl-service-account", "zl_tx_group");
    }
}
