package com.ebeijia.zl.service.control.quartz;

import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.basics.billingtype.service.BillingTypeService;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

import java.util.List;

@DisallowConcurrentExecution
public class SpecAccountTypeBizJob implements Job {

	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BillingTypeService billingTypeService;


	@Autowired
	private JedisCluster jedisCluster;


	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("定时任务SpecAccountTypeBizJob执行开始，时间[{}]", System.currentTimeMillis());
		List<BillingType> billingTypeList = null;
		BillingType billingType = null;
		try {
			billingTypeList = billingTypeService.list();
			if (billingTypeList != null && billingTypeList.size() > 0) {
				for (int i = 0; i < billingTypeList.size(); i++) {
					billingType = billingTypeList.get(i);
					if (billingType != null) {
						jedisCluster.hset(RedisConstants.REDIS_HASH_TABLE_TB_BILLING_TYPE,
								billingType.getBId(), JSONArray.toJSONString(billingType));
					}
				}
			}
		} catch (Exception e) {
			logger.error("## 定时任务doRefreshBaseDictKV执行异常", e);
		}
	}
}
