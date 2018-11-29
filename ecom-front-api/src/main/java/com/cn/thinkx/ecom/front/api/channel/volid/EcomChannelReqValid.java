package com.cn.thinkx.ecom.front.api.channel.volid;

import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.channel.model.ChannelIdent;
import com.cn.thinkx.ecom.front.api.channel.util.EcomChannelSignUtil;

/**
 * 渠道接入
 * @author zqy
 *
 */
public class EcomChannelReqValid {

	/**
	 * apValid参数校验
	 * 
	 * @param ChannelIdent
	 * @param BaseResult 返回信息
	 * @return
	 */
	public static boolean apValid(ChannelIdent req,BaseResult resp) {
		if (StringUtil.isNullOrEmpty(req.getChannelCode())) {
			resp.setMsg("请输入channelCode");
			return false;
		}
		if (StringUtil.isNullOrEmpty(req.getService())) {
			resp.setMsg("请输入service");
			return false;
		}
		if (StringUtil.isNullOrEmpty(req.getcUserId())) {
			resp.setMsg("请输入cUserId");
			return false;
		}
		if (StringUtil.isNullOrEmpty(req.getVersion())) {
			resp.setMsg("请输入version");
			return false;
		}
		if (StringUtil.isNullOrEmpty(req.getTimestamp())) {
			resp.setMsg("请输入timestamp");
			return false;
		}
		if (StringUtil.isNullOrEmpty(req.getSign())) {
			resp.setMsg("请输入sign");
			return false;
		}
		String genSign=EcomChannelSignUtil.genSign(req, "YPEgSbuyRcoDV49yHzx4wS4ZeKHFVQA84Hv4IunjHT7");
		if(!genSign.equals(req.getSign().toUpperCase())){
			resp.setMsg("签名失败");
			return false;
		}
		return true;
	}
	
}
