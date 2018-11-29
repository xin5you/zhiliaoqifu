package com.cn.thinkx.ecom.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;

public class MD5Utils {

	public static final String DEFAULT_CHARSET = "UTF-8";

	private static final char[] hexadecimal = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	public final static String MD5(String s) {
		return MD5(s, DEFAULT_CHARSET);
	}

	public final static String MD5(String s, String charset) {
		if (s == null)
			return null;

		byte[] strTemp = null;
		try {
			strTemp = s.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e);
			return null;
		}
		MessageDigest mdTemp = null;
		try {
			mdTemp = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
			return null;
		}
		mdTemp.update(strTemp);
		byte[] binaryData = mdTemp.digest();

		if (binaryData.length != 16)
			return null;

		char[] buffer = new char[32];

		for (int i = 0; i < 16; i++) {
			int low = binaryData[i] & 0x0f;
			int high = (binaryData[i] & 0xf0) >> 4;
			buffer[i * 2] = hexadecimal[high];
			buffer[i * 2 + 1] = hexadecimal[low];
		}

		return new String(buffer);
	}

	public static void main(String[] args) {
		SortedMap<String, String> itemsMap = new TreeMap<String, String>();
		itemsMap.put("attach", "514729_482655_1357620");
		itemsMap.put("channel", "40006002");
		itemsMap.put("commodityName", "家乐宝订单");
		itemsMap.put("commodityNum", "3");
		itemsMap.put("innerMerchantNo", "101000000211715");
		itemsMap.put("innerShopNo", "00000045");
		itemsMap.put("notify_url", "https://cs.jialebao.me/huiCardBaoPayNotify");
		itemsMap.put("orderId", "1505194643495");
		itemsMap.put("redirect_type", "1");
		itemsMap.put("redirect_url", "https://cs.jialebao.me/weichat/activity/ac150915/empty_jump.html?reward_type=u_4_o");
		itemsMap.put("txnAmount", "1");
		itemsMap.put("userId", "2017071109001400000089");
		StringBuilder forSign = new StringBuilder();
		for (String key : itemsMap.keySet()) {
			forSign.append(key).append("=").append(itemsMap.get(key)).append("&");
		}
		forSign.append("key=").append("LEPp3reCNRsd0J9jBwOJ6rxgUjbab1hHqOKKi3bE03r");
		String signs = MD5Utils.MD5(forSign.toString());
		System.out.println("forSign.toString--->" + forSign.toString());
		System.out.println(" signs--->" + signs.toUpperCase());
		
	}
}