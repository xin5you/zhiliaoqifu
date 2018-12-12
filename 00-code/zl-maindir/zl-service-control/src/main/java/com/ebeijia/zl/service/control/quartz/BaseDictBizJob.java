package com.ebeijia.zl.service.control.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class BaseDictBizJob implements Job {

	
	private Logger logger = LoggerFactory.getLogger(getClass());



	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		logger.info("定时任务doRefreshBaseDictKList执行开始，时间[{}]", System.currentTimeMillis());
//		List<BaseDict> baseDictList = null;
//		BaseDict baseDict = null;
//		try {
//			baseDictList = baseDictService.getBaseDictByKList();
//			if (baseDictList != null && baseDictList.size() > 0) {
//				for (int i = 0; i < baseDictList.size(); i++) {
//					baseDict = baseDictList.get(i);
//					if (baseDict != null) {
//						redisCacheChannelInfService.hset(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KLIST,
//								baseDict.getDictCode(), baseDict);
//					}
//				}
//			}
//		} catch (Exception e) {
//			logger.error("## 定时任务doRefreshBaseDictKList执行异常", e);
//		}
	}

	public void doRefreshBaseDictKV() throws Exception {
//		logger.info("定时任务doRefreshBaseDictKV执行开始，时间[{}]", DateUtil.getCurrentDateTimeStr());
//		List<BaseDict> baseDictList = null;
//		BaseDict baseDict = null;
//		try {
//			baseDictList = baseDictService.getAllBaseDictByKey();
//			if (baseDictList != null && baseDictList.size() > 0) {
//				for (int i = 0; i < baseDictList.size(); i++) {
//					baseDict = baseDictList.get(i);
//					if (baseDict != null) {
//						redisCacheChannelInfService.hsetString(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,
//								baseDict.getDictCode(), baseDict.getDictValue());
//					}
//				}
//			}
//		} catch (Exception e) {
//			logger.error("## 定时任务doRefreshBaseDictKV执行异常", e);
//		}
	}
}
