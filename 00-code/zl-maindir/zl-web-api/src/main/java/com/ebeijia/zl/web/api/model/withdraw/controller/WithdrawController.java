package com.ebeijia.zl.web.api.model.withdraw.controller;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.web.api.model.withdraw.vo.BatchDataNotify;
import com.ebeijia.zl.web.api.model.withdraw.vo.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * 汇卡宝用户提现控制类<br><br>
 * 
 * 功能 [批量代付至用户银行卡]<br>
 * 描述 [根据代付渠道区分请求路径，所有代付路径只支持POST请求方式，返回JSON格式数据]
 * 
 * @author pucker
 *
 */
@Controller
@RequestMapping("/api/withdraw")
public class WithdrawController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 代付异步回调
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/suning-yfb/notify")
	@ResponseBody
	public boolean welfareResellNotify(HttpServletRequest request) {
		String contentJson = request.getParameter("content");
		String sign = request.getParameter("sign");
		String sign_type = request.getParameter("sign_type");
		String vk_version = request.getParameter("vk_version");
		Content content = JSONArray.parseObject(contentJson, Content.class);
		logger.info("苏宁YFB异步通知返回：{}",contentJson);
		BatchDataNotify batchData = new BatchDataNotify();
		batchData.setContent(content);
		batchData.setSign(sign);
		batchData.setSign_type(sign_type);
		batchData.setVk_version(vk_version);
		
		logger.info("易付宝回调请求参数{}", JSONArray.toJSONString(batchData));
		//验证易付宝请求参数，更新出款订单表，新增出款订单明细表
		return true;
	}
	
	@RequestMapping(value = "/suning-yfb/withdrawQuery", method = RequestMethod.POST)
	@ResponseBody
	public String withdrawQuery(HttpServletRequest request) {
		String batchNo = request.getParameter("batchNo");
		String payMerchantNo = request.getParameter("payMerchantNo");
		String result = null;
		logger.info("代付查询接口返回==========>[{}]", result);
		return result;
	}
	

	
}
