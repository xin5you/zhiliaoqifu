package com.ebeijia.zl.service.control.quartz;

import ch.qos.logback.core.util.StringCollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.api.bm001.api.constants.BMConstants;
import com.ebeijia.zl.api.bm001.api.service.BMOpenApiService;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.http.HttpClientUtil;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.MD5SignUtils;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;
import com.ebeijia.zl.facade.telrecharge.resp.TeleReqVO;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespVO;
import com.ebeijia.zl.facade.telrecharge.service.ProviderOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianmi.open.api.response.BmOrderCustomGetResponse;
import com.qianmi.open.api.response.BmRechargeOrderInfoResponse;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 话费充值订单查询订单
 * @author zhuqiuyou
 *
 */
@DisallowConcurrentExecution
public class TelePhoneRechargeBizJob implements Job{

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProviderOrderInfFacade providerOrderInfFacade;

	@Autowired
	private RetailChnlOrderInfFacade retailChnlOrderInfFacade;

	@Autowired
	private RetailChnlInfFacade retailChnlInfFacade;

	@Autowired
	private BMOpenApiService bmOpenApiService;

	@Autowired
	private JedisClusterUtils jedisClusterUtils;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("定时任务TelePhoneRechargeBizJob执行开始，时间[{}]", System.currentTimeMillis());

		ProviderOrderInf providerOrderInf = new ProviderOrderInf();
		providerOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_0.getCode());

		List<ProviderOrderInf> list = providerOrderInfFacade.getListByTimer(providerOrderInf);
		logger.info("定时任务查询充值中订单笔数：[{}]", list.size());

		if(list != null && list.size() > 0){
			for(ProviderOrderInf  t : list){
				if(t == null){
					continue;
				}
				try {
					String accessToken = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, BMConstants.BM_SERVER_URL);
					logger.info("手机充值--->话费充值状态查询接口，请求参数outerTid--->{},accessToken--->{}", t.getRegOrderId(), accessToken);

					BmOrderCustomGetResponse customOrderResp = bmOpenApiService.handleGetCustomOrder(t.getRegOrderId(), accessToken);
					logger.info("话费充值调用立方查询接口返回数据-->{}", JSONArray.toJSONString(customOrderResp));
					//System.out.println("话费充值状态查询返回数据-->"+JSONArray.toJSONString(customOrderResp));
					if(customOrderResp != null){
						if (!StringUtil.isNullOrEmpty(customOrderResp.getOrderDetailInfo().getRechargeState())) {
							if (!TeleConstants.ProviderRechargeState.RECHARGE_STATE_0.getCode().equals(customOrderResp.getOrderDetailInfo().getRechargeState())) {
								t.setRechargeState(customOrderResp.getOrderDetailInfo().getRechargeState());
								RetailChnlOrderInf retailChnlOrderInf = retailChnlOrderInfFacade.getRetailChnlOrderInfById(t.getChannelOrderId());
								RetailChnlInf retailChnlInf = retailChnlInfFacade.getRetailChnlInfById(retailChnlOrderInf.getChannelId());
								//回调通知分銷商
								retailChnlOrderInfFacade.doTelRechargeBackNotify(retailChnlInf, retailChnlOrderInf, t);
							}
						}
					} else {
						logger.error("## 调用话费充值查询接口，返回参数为空，reg_order_id--->{}", t.getRegOrderId());
					}
				} catch (Exception e) {
					logger.error("##定时任务doRefreshRechargeState执行异常", e);
				}
			}
		}
	}

}
