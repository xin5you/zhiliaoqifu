package com.cn.thinkx.ecom.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESUtil {

	private static final String Algorithm = "AES";

	// 初始化向量
	public static String getParam() {
		Date dt = new Date();
		SimpleDateFormat matter = new SimpleDateFormat("yyyyMMdd");
		String iv = MD5Utils.MD5(matter.format(dt)).substring(0, 16);
		return iv;
	}

	/**
	 * AES加密
	 * 
	 * @param encryptstr
	 * @param iv
	 * @return
	 */
	public static String jzEncrypt(String encryptstr, String sKey) {
		return byte2hex(encrypt(encryptstr, getParam(), sKey));
	}

	/**
	 * 字符串转换成十六进制字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String byte2hex(String str) {
		char[] chars = "0123456789abcdef".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString().trim();
	}

	/**
	 * AES系统中的CBC模式加密
	 * 
	 * @param sSrc
	 * @param ivParameter
	 * @return
	 */
	public static String encrypt(String sSrc, String ivParameter, String sKey) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] raw = sKey.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, Algorithm);
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
			return new BASE64Encoder().encode(encrypted);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * AES解密
	 * 
	 * @param decryptstr
	 * @param sKey
	 * @return
	 */
	public static String jzDecrypt(String decryptstr, String sKey) {
		return decrypt(pack(decryptstr), getParam(), sKey);
	}

	/**
	 * 数据装入一个二进制字符串
	 * 
	 * @param str
	 *            待转换的ASCII字符串
	 * @return
	 */
	public static String pack(String str) {
		byte[] baKeyword = new byte[str.length() / 2];
		try {
			for (int i = 0; i < baKeyword.length; i++) {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16));
			}
			str = new String(baKeyword, "UTF-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return str;
	}

	public static void main(String[] args) {
	String sKey = "litj49jk4op589o4";
	String str= "2017092215481600000867_18501713931_oppGJ0zT1fpN6ZqjOgW8Uzd3w6-c_101000000211715_00000045";
	String result = jzEncrypt(str, sKey);
	System.out.println(result);

	/*String s = "2017062606345600000042";
	String r = jzEncrypt(s,sKey);
	System.out.println(r);
	
	String t = String.valueOf(System.currentTimeMillis());
	String results = jzEncrypt(t,sKey);
	System.out.println(results);
	
	String ss = "6a4e577a42524257424e6c445a6a39584f7441314d3937414c6b7079544243693769736177564b626930553d";
	String a = jzDecrypt(ss,sKey);
	System.out.println(a);*/
}

	/**
	 * AES（算法-128）系统中的CBC模式解密。
	 * 
	 * @param sSrc
	 * @param ivParameter
	 * @param sKey
	 * @return
	 */
	public static String decrypt(String sSrc, String ivParameter, String sKey) {
		try {
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "utf-8");
			return originalString;
		} catch (Exception ex) {
			return null;
		}
	}

}
