package com.cn.thinkx.wecard.centre.module.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信 jsToken 定时更新 Job
 *
 */
@DisallowConcurrentExecution
public class JsTokenBizJob implements Job{

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 刷新微信公众号jsToken
	 * 
	 * @throws Exception
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("定时任务doRefreshJsToken执行开始，时间[{}]",System.currentTimeMillis());

	}

}