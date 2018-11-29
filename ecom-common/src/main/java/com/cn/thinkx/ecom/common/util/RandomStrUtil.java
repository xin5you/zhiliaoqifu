package com.cn.thinkx.ecom.common.util;

import java.util.UUID;

public class RandomStrUtil {
	private static String PREFIX = "@jl.cn";
	
	public static String genUserName() {
		int len = 6;
		// 数组，用于存放随机字符
		char[] chArr = new char[len];
		// 为了保证必须包含数字、大写字母
		chArr[0] = (char) ('1' + StdRandom.uniform(0, 9));
		do {
			chArr[1] = (char) ('A' + StdRandom.uniform(0, 26));
		} while (chArr[1] == 'O');

		char[] codes = { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		// charArr[2..len-1]随机生成codes中的字符
		for (int i = 2; i < len; i++) {
			chArr[i] = codes[StdRandom.uniform(0, codes.length)];
		}

		// 将数组chArr随机排序
		for (int i = 0; i < len; i++) {
			int r = i + StdRandom.uniform(len - i);
			char temp = chArr[i];
			chArr[i] = chArr[r];
			chArr[r] = temp;
		}

		return new String(chArr) + PREFIX;
	}
	
	public static String getOrderIdByUUId(String machineId) {
		int hashCodeV = UUID.randomUUID().toString().hashCode();
		if (hashCodeV < 0) {// 有可能是负数
			hashCodeV = -hashCodeV;
		}
		return machineId + hashCodeV;
	}

	public static void main(String[] args) {
		System.out.println(getOrderIdByUUId("H"));
	}
}