package com.cn.thinkx.ecom.activemq.core.util;

import java.util.TreeMap;

public class WXTemplateUtil {

	public static TreeMap<String, TreeMap<String, String>> setRefundAddressData(String productName, String name,
			String mobile, String fullAddress) {
		TreeMap<String, TreeMap<String, String>> params = new TreeMap<String, TreeMap<String, String>>();
		params.put("first", TemplateMessage.item("您的退货申请商家已通过\n", "#000000"));
		params.put("keyword1", TemplateMessage.item(productName, "#000000"));
		params.put("keyword2", TemplateMessage.item(name, "#000000"));
		params.put("keyword3", TemplateMessage.item(mobile, "#000000"));
		params.put("keyword4", TemplateMessage.item(fullAddress, "#228B22"));
		params.put("remark", TemplateMessage.item("感谢您的使用，如有疑问请致电021-64189869", "#FF0000"));
		return params;
	}

	
	public static TreeMap<String, TreeMap<String, String>> setRefundRejectData(String returnsId, String sOrderId,
			String refuseReason, String productName) {
		TreeMap<String, TreeMap<String, String>> params = new TreeMap<String, TreeMap<String, String>>();
		params.put("first", TemplateMessage.item("您的退货申请已被拒绝\n", "#000000"));
		params.put("keyword1", TemplateMessage.item(returnsId, "#000000"));
		params.put("keyword2", TemplateMessage.item(sOrderId, "#000000"));
		params.put("keyword3", TemplateMessage.item(refuseReason, "#000000"));
		params.put("keyword4", TemplateMessage.item(productName, "#228B22"));
		params.put("remark", TemplateMessage.item("感谢您的使用，如有疑问请致电021-64189869", "#FF0000"));
		return params;
	}
}
