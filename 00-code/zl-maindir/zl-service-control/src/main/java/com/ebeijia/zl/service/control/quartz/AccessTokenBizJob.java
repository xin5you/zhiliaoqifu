package com.ebeijia.zl.service.control.quartz;

import com.ebeijia.zl.basics.wechat.domain.MpAccount;
import com.ebeijia.zl.basics.wechat.service.MpAccountService;
import com.ebeijia.zl.core.wechat.process.WxApiClient;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 微信 accessToken 定时更新 Job
 *
 */
@DisallowConcurrentExecution
public class AccessTokenBizJob implements Job{

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MpAccountService  mpAccountService;

	@Autowired
	private WxApiClient wxApiClient;

	/**
	 * 刷新微信公众号AccountToken
	 * 
	 * @throws Exception
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("定时任务doRefreshAccessToken执行开始，时间[{}]", System.currentTimeMillis());
		
		List<MpAccount> accountList = mpAccountService.list();
		try {

			if (accountList != null && accountList.size() > 0) {
				com.ebeijia.zl.core.wechat.process.MpAccount mpAccount = null;
				for (MpAccount mpEntity:accountList) {
					mpAccount=new com.ebeijia.zl.core.wechat.process.MpAccount();
					mpAccount.setAccount(mpEntity.getAccount());
					mpAccount.setAppid(mpEntity.getAppid());
					mpAccount.setAppsecret(mpEntity.getAppsecret());
					mpAccount.setToken(mpEntity.getToken());
					mpAccount.setUrl(mpEntity.getUrl());
					mpAccount.setMsgcount(mpEntity.getMsgcount());
					wxApiClient.doRefreshAccessToken(mpAccount); // 刷新公众号的accesstoken
				}
			}
		} catch (Exception e) {
			logger.error("## 定时任务doRefreshAccessToken执行异常", e);
		}
	}

}