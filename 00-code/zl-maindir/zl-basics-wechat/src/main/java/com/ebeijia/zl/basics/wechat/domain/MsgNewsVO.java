package com.ebeijia.zl.basics.wechat.domain;

import java.util.ArrayList;
import java.util.List;

import com.ebeijia.zl.basics.wechat.domain.MsgNews;

/**
 * 图文消息
 * 
 *
 */
public class MsgNewsVO implements java.io.Serializable {
	
	private static final long serialVersionUID = -3833338129247083605L;
	
	private String createTimeStr;
	
	private List<MsgNews> msgNewsList = new ArrayList<MsgNews>();

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public List<MsgNews> getMsgNewsList() {
		return msgNewsList;
	}

	public void setMsgNewsList(List<MsgNews> msgNewsList) {
		this.msgNewsList = msgNewsList;
	}

}