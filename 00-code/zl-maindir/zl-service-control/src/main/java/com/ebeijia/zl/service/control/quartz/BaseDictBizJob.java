package com.ebeijia.zl.service.control.quartz;

import com.ebeijia.zl.basics.billingtype.service.BaseDictService;
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
public class BaseDictBizJob implements Job {

	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BaseDictService baseDictService;


	@Autowired
	private JedisCluster jedisCluster;


	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("定时任务doRefreshBaseDictKV执行开始，时间[{}]", System.currentTimeMillis());
		List<BaseDict> baseDictList = null;
		BaseDict baseDict = null;
		try {
			baseDictList = baseDictService.list();
			if (baseDictList != null && baseDictList.size() > 0) {
				for (int i = 0; i < baseDictList.size(); i++) {
					baseDict = baseDictList.get(i);
					if (baseDict != null) {
						jedisCluster.hset(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,
								baseDict.getDictCode(), baseDict.getDictValue());
					}
				}
			}
		} catch (Exception e) {
			logger.error("## 定时任务doRefreshBaseDictKV执行异常", e);
		}
	}
}
