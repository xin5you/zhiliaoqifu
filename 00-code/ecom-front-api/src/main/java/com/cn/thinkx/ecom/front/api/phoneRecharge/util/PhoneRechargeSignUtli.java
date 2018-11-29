package com.cn.thinkx.ecom.front.api.phoneRecharge.util;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.redis.core.constants.RedisConstants;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;

@Configuration
public class PhoneRechargeSignUtli {

	@Autowired
	private  JedisClusterUtils jedisClusterUtils;
	
	/**
	 * 根据传入对象生成签名
	 * 
	 * @param obj
	 * @return
	 */
	public String genSign(Object obj) {
		SortedMap<String, String> map = new TreeMap<String, String>();
		if (obj != null) {
			getProperty(map, obj, obj.getClass());// 将当前类属性及其值放入map
			if (obj.getClass().getSuperclass()!=null && obj.getClass().getGenericSuperclass() != null) {
				getProperty(map, obj, obj.getClass().getSuperclass());// 将当前类父类属性及其值放入map
				if (obj.getClass().getSuperclass().getSuperclass() !=null && obj.getClass().getGenericSuperclass().getClass().getGenericSuperclass() != null)
					getProperty(map, obj, obj.getClass().getSuperclass().getSuperclass());// 将当前类父类属性及其值放入map
			}
		}
		return sign(map, map.get("reqChannel"));
	}
	
	public void getProperty(SortedMap<String, String> map, Object obj, Class<?> clazz) {
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

	public String byteToStr(byte[] byteArray) {
		String rst = "";
		for (int i = 0; i < byteArray.length; i++) {
			rst += byteToHex(byteArray[i]);
		}
		return rst;
	}

	public String byteToHex(byte b) {
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
	public String MD5Encode(String origin) {
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
	public String sign(SortedMap<String, String> items, String channel) {
		StringBuilder forSign = new StringBuilder();
		for (String key : items.keySet()) {
			forSign.append(key).append("=").append(items.get(key)).append("&");
		}
		//forSign.setLength(forSign.length() - 1);
		String key = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, channel+"_SIGN_KEY");
		
		forSign.append("key=").append(key);
		String result = MD5Encode(forSign.toString());
		return result.toUpperCase();
	}
	
	public boolean isSignExpired(long currentTime, long txnTimestamp) {
		int signatureTimeout = Integer.parseInt(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, "SIGNATURE_TIMEOUT"));
		long valid = signatureTimeout * 60 * 1000;// 签名有效期(分钟转毫秒)
		if ((currentTime - txnTimestamp) > valid) {// 签名失效
			return true;
		}
		return false;
	}


}
