package com.ebeijia.zl.common.utils.tools;

import java.math.BigDecimal;

/**
 * 
* 
* @ClassName: AmountUtil.java
* @Description: 金额操作的工具类
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年11月30日 下午7:47:49 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年11月30日     zhuqi           v1.0.0
 */
public abstract class AmountUtil {

	private AmountUtil() {

	}

	/**
	 * 加法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
		return b1.add(b2);
	}

	/**
	 * 减法运算
	 * 
	 * @param v1被减数
	 * @param v2减数
	 * @return
	 */
	public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
		return v1.subtract(v2);
	}

	/**
	 * 乘法运算
	 * 
	 * @param v1被乘数
	 * @param v2乘数
	 * @return
	 */
	public static double mul(BigDecimal v1, BigDecimal v2) {
		return v1.multiply(v2).doubleValue();
	}

	/**
	 * 
	 * 除法运算，当发生除不尽的情况时，精确到小数点以后2位，以后的数字四舍五入
	 * 
	 * @param v1被除数
	 * @param v2除数
	 * @return
	 */
	public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
		return div(v1, v2, 4);
	}

	/**
	 * 
	 * 除法运算，当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
	 * 
	 * @param v1被除数
	 * @param v2除数
	 * @param scale精确到小数点以后几位
	 * @return
	 */
	public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {

		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		return v1.divide(v2, scale, BigDecimal.ROUND_HALF_UP);
	}


	/**
	 * 判断 a 与 b 是否相等
	 * 
	 * @param a
	 * @param b
	 * @return a==b 返回true, a!=b 返回false
	 */
	public static boolean equal(double a, double b) {
		BigDecimal v1 = BigDecimal.valueOf(a);
		BigDecimal v2 = BigDecimal.valueOf(b);
		if (v1.compareTo(v2) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断 a 是否大于等于 b
	 * 
	 * @param a
	 * @param b
	 * @return a&gt;=b 返回true, a&lt;b 返回false
	 */
	public static boolean greaterThanOrEqualTo(double a, double b) {
		BigDecimal v1 = BigDecimal.valueOf(a);
		BigDecimal v2 = BigDecimal.valueOf(b);
		if (v1.compareTo(v2) >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断 a 是否大于 b
	 * 
	 * @param a
	 * @param b
	 * @return a&gt;b 返回true, a&lt;=b 返回 false
	 */
	public static boolean bigger(double a, double b) {
		BigDecimal v1 = BigDecimal.valueOf(a);
		BigDecimal v2 = BigDecimal.valueOf(b);
		if (v1.compareTo(v2) == 1) {
			return true;
		}
		return false;
	}

	/**
	 * 判断 a 是否小于 b
	 * 
	 * @param a
	 * @param b
	 * @return a&lt;b 返回true, a&gt;=b 返回 false
	 */
	public static boolean lessThan(double a, double b) {
		BigDecimal v1 = BigDecimal.valueOf(a);
		BigDecimal v2 = BigDecimal.valueOf(b);
		if (v1.compareTo(v2) == -1) {
			return true;
		}
		return false;
	}

	/**
	 * 四舍五入保留小数点后两位
	 * 
	 * @param num
	 * @return
	 */
	public static double roundDown(double num) {
		return Double.valueOf(String.format("%.2f", num));
		//return new BigDecimal(num).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static void main(String[] args) {
		
//		Double s = AmountUtil.sub(a, b);
//		System.out.println(AmountUtil.roundDown(s));
//		System.out.println(AmountUtil.div(101.1D, 1D, 2));
	}
}
