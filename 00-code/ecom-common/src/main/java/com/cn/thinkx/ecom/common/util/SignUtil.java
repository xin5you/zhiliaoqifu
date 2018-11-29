package com.cn.thinkx.ecom.common.util;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.SortedMap;
import java.util.TreeMap;

public class SignUtil {

	private static String SIGNATURE_TIMEOUT = "5";
	
	/**
	 * 根据传入对象生成签名
	 * 
	 * @param obj
	 * @return
	 */
	public static String genSign(Object obj,String str) {
		SortedMap<String, String> map = new TreeMap<String, String>();
		if (obj != null) {
			getProperty(map, obj, obj.getClass());// 将当前类属性及其值放入map
			if (obj.getClass().getSuperclass() != null && obj.getClass().getGenericSuperclass() != null) {
				getProperty(map, obj, obj.getClass().getSuperclass());// 将当前类父类属性及其值放入map
				if (obj.getClass().getSuperclass().getSuperclass() != null && obj.getClass().getGenericSuperclass().getClass().getGenericSuperclass() != null)
					getProperty(map, obj, obj.getClass().getSuperclass().getSuperclass());// 将当前类父类属性及其值放入map
			}
		}
		if(map.get("channel") == null)
			return signJF(map, str); 
		else
			return sign(map, map.get("channel"), str);
	}

	public static void getProperty(SortedMap<String, String> map, Object obj, Class<?> clazz) {
		// 获取f对象对应类中的所有属性域
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0, len = fields.length; i < len; i++) {
			String varName = fields[i].getName();
			// 过滤签名字段
			if ("sign".equals(varName) || "serialVersionUID".equals(varName))
				continue;
			try {
				// 获取原来的访问控制权限
				boolean accessFlag = fields[i].isAccessible();
				// 修改访问控制权限
				fields[i].setAccessible(true);
				// 获取在对象f中属性fields[i]对应的对象中的变量
				Object o = fields[i].get(obj);
				if (o != null && !StringUtil.isNullOrEmpty(o.toString()))
					map.put(varName, o.toString());
				// 恢复访问控制权限
				fields[i].setAccessible(accessFlag);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static String byteToStr(byte[] byteArray) {
		String rst = "";
		for (int i = 0; i < byteArray.length; i++) {
			rst += byteToHex(byteArray[i]);
		}
		return rst;
	}

	public static String byteToHex(byte b) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(b >>> 4) & 0X0F];
		tempArr[1] = Digit[b & 0X0F];
		String s = new String(tempArr);
		return s;
	}

	/**
	 * MD5编码
	 * 
	 * @param origin
	 *            原始字符串
	 * @return 经过MD5加密之后的结果
	 */
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(origin.getBytes("UTF-8"));
			resultString = byteToStr(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultString;
	}

	/**
	 * 组装报文签名
	 * 
	 * @param items
	 * @param channel
	 * @return
	 */
	public static String sign(SortedMap<String, String> items, String channel, String keys) {
		StringBuilder forSign = new StringBuilder();
		for (String key : items.keySet()) {
			forSign.append(key).append("=").append(items.get(key)).append("&");
		}
		forSign.append("key=").append(keys);
		String result = MD5Encode(forSign.toString());
		return result.toUpperCase();
	}
	
	public static String signJF(SortedMap<String, String> items, String keys) {
		StringBuilder forSign = new StringBuilder();
		for (String key : items.keySet()) {
			forSign.append(key).append("=").append(items.get(key)).append("&");
		}
		forSign.deleteCharAt(forSign.length()-1);
		forSign.append(keys);
		String result = MD5Encode(forSign.toString());
		return result.toLowerCase();
	}

	public static boolean isSignExpired(long currentTime, long txnTimestamp) {
		int signatureTimeout = Integer.parseInt(SIGNATURE_TIMEOUT);
		long valid = signatureTimeout * 60 * 1000;// 签名有效期(分钟转毫秒)
		if ((currentTime - txnTimestamp) > valid) {// 签名失效
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		/*String a = String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000);
		System.out.println(a);
		JFExtandJson extandParams = new JFExtandJson();
		extandParams.setPhoneNo("18036395254");
		extandParams.setUserId("78405");
		extandParams.setMchntCode("101000000290324");
		extandParams.setShopCode("00000093");
		extandParams.setEcomChnl("40006003");
		extandParams.setChannel("40004001");
		SortedMap<String, String> itemsMap = new TreeMap<String, String>();
		itemsMap.put("e_eid", "hkb_001");
		itemsMap.put("e_uid", "78405");
		itemsMap.put("extand_params", JSONArray.toJSONString(extandParams));
		itemsMap.put("sign_type", "MD5");
		itemsMap.put("timestamp", a);
		itemsMap.put("total_amount", "50");
		itemsMap.put("trade_no", "111111111111111111111114");
		StringBuilder forSign = new StringBuilder();
		for (String key : itemsMap.keySet()) {
			forSign.append(key).append("=").append(itemsMap.get(key)).append("&");
		}
		forSign.deleteCharAt(forSign.length()-1);
		forSign.append("kec437d5o3956d1023k20ec9w08a9d27");
		System.out.println("forSign ------- "+forSign.toString());
		String signs = SignUtil.MD5Encode(forSign.toString());
		System.out.println("sign = "+signs.toLowerCase());
		String ex = "";
		try {
			ex = URLEncoder.encode(itemsMap.get("extand_params"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(ex);*/
		//退款接口加密
		/*SortedMap<String, String> itemsMap = new TreeMap<String, String>();
		itemsMap.put("trade_no", "100118010331145185323220");
		itemsMap.put("old_trade_no", "100118122528516883819415");
		itemsMap.put("total_amount", "1990");
		itemsMap.put("refund_amount", "10");
		String a = String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000);
		System.out.println(a);
		itemsMap.put("timestamp", a);
		itemsMap.put("sign_type", "MD5");
		StringBuilder forSign = new StringBuilder();
		for (String key : itemsMap.keySet()) {
			forSign.append(key).append("=").append(itemsMap.get(key)).append("&");
		}
		forSign.deleteCharAt(forSign.length()-1);
		forSign.append("kec437d5o3956d1023k20ec9w08a9d27");
		System.out.println("forSign ------- "+forSign.toString());
		String signs = SignUtil.MD5Encode(forSign.toString());
		System.out.println("sign = "+signs.toLowerCase());*/
		//查单接口参数加密
		/*SortedMap<String, String> itemsMap = new TreeMap<String, String>();
		itemsMap.put("trade_no", "100118122528516883819415");
		itemsMap.put("timestamp", "1513839742");
		itemsMap.put("sign_type", "MD5");
		StringBuilder forSign = new StringBuilder();
		for (String key : itemsMap.keySet()) {
			forSign.append(key).append("=").append(itemsMap.get(key)).append("&");
		}
		forSign.deleteCharAt(forSign.length()-1);
		forSign.append("kec437d5o3956d1023k20ec9w08a9d27");
		System.out.println("forSign ------- "+forSign.toString());
		String signs = SignUtil.MD5Encode(forSign.toString());
		System.out.println("sign = "+signs.toLowerCase());*/
		//授权支付接口参数加密
		/*SortedMap<String, String> itemsMap = new TreeMap<String, String>();
		itemsMap.put("e_eid", "hkb_001");
		itemsMap.put("e_uid", "2017071109001400000089");
		itemsMap.put("extand_params", "2017071109001400000089_15575311169_oppGJ0zBVftagMTyIQqaFQ5y1sEM_101000000211715_00000045");
		itemsMap.put("sign_type", "MD5");
		itemsMap.put("timestamp", "1513839742");
		itemsMap.put("total_amount", "5");
		itemsMap.put("trade_no", "100118122528516883819415");
		StringBuilder forSign = new StringBuilder();
		for (String key : itemsMap.keySet()) {
			forSign.append(key).append("=").append(itemsMap.get(key)).append("&");
		}
		forSign.deleteCharAt(forSign.length()-1);
		forSign.append("kec437d5o3956d1023k20ec9w08a9d27");
		System.out.println("forSign ------- "+forSign.toString());
		String signs = SignUtil.MD5Encode(forSign.toString());
		System.out.println("sign = "+signs.toLowerCase());*/
		//调用知了企服参数加密
		/*SortedMap<String, String> itemsMap = new TreeMap<String, String>();
		itemsMap.put("cardNo", "oppGJ0zBVftagMTyIQqaFQ5y1sEM");
		itemsMap.put("channel", "40006001");
		itemsMap.put("innerMerchantNo", "101000000211715");
		itemsMap.put("innerShopNo", "00000045");
		itemsMap.put("oriTxnAmount", "3220");
		itemsMap.put("swtFlowNo", "2017122809501000137807");
		itemsMap.put("swtTxnDate", "20171228");
		itemsMap.put("swtTxnTime", "095319");
		itemsMap.put("txnAmount", "3220");
		itemsMap.put("timestamp", "1514425999875");
		StringBuilder forSign = new StringBuilder();
		for (String key : itemsMap.keySet()) {
			forSign.append(key).append("=").append(itemsMap.get(key)).append("&");
		}
		forSign.append("key=").append("YPEgSbuyRcoDV49yHzx4wS4ZeKHFVQA84Hv4IunjH10");
		String signs = SignUtil.MD5Encode(forSign.toString());
		System.out.println(signs);*/
	}
}
