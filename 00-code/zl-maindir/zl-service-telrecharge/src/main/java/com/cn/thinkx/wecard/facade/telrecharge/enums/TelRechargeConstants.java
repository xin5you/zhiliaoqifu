package com.cn.thinkx.wecard.facade.telrecharge.enums;

public class TelRechargeConstants {

	
	public enum OperatorType {
		OperatorType1("1","移动"),
		OperatorType2("2","联通"),
		OperatorType3("3","电信");

		private String code;
		private String value;

		OperatorType(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static String findByCode(String code) {
			for (OperatorType t : OperatorType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t.getValue();
				}
			}
			return null;
		}
	}
	
	public enum ShopType{
		ShopType1("1","话费"),
		ShopType2("2","流量");

		private String code;
		private String value;

		ShopType(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static String findByCode(String code) {
			for (ShopType t : ShopType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t.getValue();
				}
			}
			return null;
		}
	}
	
	public enum ChannelReserveType{
		ChannelReserveType1("1","追加"),
		ChannelReserveType2("2","撤销");

		private String code;
		private String value;

		ChannelReserveType(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static String findByCode(String code) {
			for (ChannelReserveType t : ChannelReserveType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t.getValue();
				}
			}
			return null;
		}
	}
	

	
	/**
	 * 退款 模板消息文案
	 * 
	 * @author xiaomei
	 *
	 */
	public enum templateMsgRefund{
		templateMsgRefund0("10001001", "退款成功"), 
		templateMsgRefund1("40001010", "退款成功"), 
		templateMsgRefund2("40002001", "退款成功"), 
		templateMsgRefund3("40003001", "退款成功"), 
		templateMsgRefund4("40004001", "退款成功"), 
		templateMsgRefund5("40005001", "退款成功"), 
		templateMsgRefund6("40006001", "商城购物异常"), 
		templateMsgRefund7("40001002", "退款成功"), 
		templateMsgRefund8("40007001", "手机充值异常"), 
		TemplateMsgRefund9("40008001", "卡券购买异常"), 
		templateMsgRefund10("40007002", "手机充值异常");

		private String code;
		private String value;

		templateMsgRefund(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static templateMsgRefund findByCode(String code) {
			for (templateMsgRefund t : templateMsgRefund.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	public enum ShopUnitType{
		ShopUnitType01("1","1","元");
		
		private String code;
		private String typeCode;
		private String value;
		
		ShopUnitType(String code, String typeCode, String value) {
			this.code = code;
			this.typeCode = typeCode;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getTypeCode() {
			return typeCode;
		}

		public String getValue() {
			return value;
		}
		
		public static String findByCode(String code) {
			for (ShopUnitType t : ShopUnitType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t.getValue();
				}
			}
			return null;
		}
		
	}
	
	/**
	 * 供应商信息-默认路由
	 * 
	 * @author kpplg
	 *
	 */
	public enum providerDefaultRoute {
		DefaultRoute0("0","是"),
		DefaultRoute1("1","否");

		private String code;
		private String value;

		providerDefaultRoute(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static String findByCode(String code) {
			for (providerDefaultRoute t : providerDefaultRoute.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t.getValue();
				}
			}
			return null;
		}
	}
	
	/**
	 * 供应商订单-充值状态
	 * 
	 * @author kpplg
	 *
	 */
	public enum providerOrderRechargeState {
		RechargeState0("0","充值中"),
		RechargeState1("1","充值成功"),
		RechargeState3("3","充值失败"),
		RechargeState8("8","待充值"),
		RechargeState9("9","撤销");

		private String code;
		private String value;

		providerOrderRechargeState(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static String findByCode(String code) {
			for (providerOrderRechargeState t : providerOrderRechargeState.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t.getValue();
				}
			}
			return null;
		}
	}
	
	/**
	 * 供应商订单-支付状态
	 * 
	 * @author kpplg
	 *
	 */
	public enum providerOrderPayState {
		PayState1("1","已扣款"),
		PayState2("2","已退款");

		private String code;
		private String value;

		providerOrderPayState(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static String findByCode(String code) {
			for (providerOrderPayState t : providerOrderPayState.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t.getValue();
				}
			}
			return null;
		}
	}
	
	/**
	 * 分销商订单-订单状态
	 * 
	 * @author kpplg
	 *
	 */
	public enum ChannelOrderStat {
		ChannelOrderStat0("0","待扣款"),
		ChannelOrderStat1("1","已扣款"),
		ChannelOrderStat2("2","已退款"),
		ChannelOrderStat5("5","退款中");

		private String code;
		private String value;

		ChannelOrderStat(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static String findByCode(String code) {
			for (ChannelOrderStat t : ChannelOrderStat.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t.getValue();
				}
			}
			return null;
		}
	}
	/**
	 * 分销商订单-通知状态
	 * 
	 * @author kpplg
	 *
	 */
	public enum ChannelOrderNotifyStat {
		ChannelOrderNotifyStat1("1","处理中"),
		ChannelOrderNotifyStat2("2","处理失败"),
		ChannelOrderNotifyStat3("3","处理成功");

		private String code;
		private String value;

		ChannelOrderNotifyStat(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static String findByCode(String code) {
			for (ChannelOrderNotifyStat t : ChannelOrderNotifyStat.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t.getValue();
				}
			}
			return null;
		}
	}
	
	/**
	 * 分销商充值产品-是否区分地区
	 * 
	 * @author kpplg
	 *
	 */
	public enum ChannelProductAreaFlag {
		ChannelProductAreaFlag0("0","区分"),
		ChannelProductAreaFlag1("1","不区分");

		private String code;
		private String value;

		ChannelProductAreaFlag(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static String findByCode(String code) {
			for (ChannelProductAreaFlag t : ChannelProductAreaFlag.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t.getValue();
				}
			}
			return null;
		}
	}
	
	/**
	 * 分销商充值产品-产品类型
	 * 
	 * @author kpplg
	 *
	 */
	public enum ChannelProductProType {
		ChannelProductProType1("1","话费"),
		ChannelProductProType2("2","流量"),
		ChannelProductProType3("3","其他");

		private String code;
		private String value;

		ChannelProductProType(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static String findByCode(String code) {
			for (ChannelProductProType t : ChannelProductProType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t.getValue();
				}
			}
			return null;
		}
	}
	
	public static void main(String[] args) {
		for (OperatorType t : OperatorType.values()) {
			if (t.code.equalsIgnoreCase("1")) {
				System.out.println(t.getValue());
			}
		}
	}
	
	
}
