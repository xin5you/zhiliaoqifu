package com.cn.thinkx.wecard.centre.module.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信 accessToken 定时更新 Job
 *
 */
@DisallowConcurrentExecution
public class AccessTokenBizJob implements Job{

	private Logger logger = LoggerFactory.getLogger(getClass());
	

	/**
	 * 刷新微信公众号AccountToken
	 * 
	 * @throws Exception
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("定时任务doRefreshAccessToken执行开始，时间[{}]", System.currentTimeMillis());
		
//		List<MpAccount> accountList = null;
//		try {
//			accountList = WxApiClient.getAllMpAccountList();
//			if (accountList != null && accountList.size() > 0) {
//				MpAccount mpAccount = null;
//				for (int i = 0; i < accountList.size(); i++) {
//					mpAccount = accountList.get(i);
//					WxApiClient.doRefreshAccessToken(mpAccount); // 刷新公众号的accesstoken
//				}
//			}
//		} catch (Exception e) {
//			logger.error("## 定时任务doRefreshAccessToken执行异常", e);
//		}
	}

}