package com.ebeijia.zl.web.user;

import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.basics.wechat.domain.MpAccount;
import com.ebeijia.zl.basics.wechat.service.MpAccountService;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.core.wechat.process.WxMemoryCacheClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

@Component
public class MyStartupRunnerService implements CommandLineRunner {
  private  Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MpAccountService mpAccountService;

    @Autowired
    private WxMemoryCacheClient wxMemoryCacheClient;

    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public void run(String... args) throws Exception {
        logger.info(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作<<<<<<<<<<<<<");
        MpAccount mpEntity=mpAccountService.getByAccount(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,"WX_HUIKABAO_LIFE_ACCOUNT"));
        com.ebeijia.zl.core.wechat.process.MpAccount mpAccount=new com.ebeijia.zl.core.wechat.process.MpAccount();
        mpAccount.setAccount(mpEntity.getAccount());
        mpAccount.setAppid(mpEntity.getAppid());
        mpAccount.setAppsecret(mpEntity.getAppsecret());
        mpAccount.setToken(mpEntity.getToken());
        mpAccount.setUrl(mpEntity.getUrl());
        mpAccount.setMsgcount(mpEntity.getMsgcount());
        wxMemoryCacheClient.addMpAccount(mpAccount);
    }
 
}