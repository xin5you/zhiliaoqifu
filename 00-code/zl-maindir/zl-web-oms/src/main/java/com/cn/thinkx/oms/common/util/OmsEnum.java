package com.cn.thinkx.oms.common.util;

public class OmsEnum {
	
	/**
	 * 订单状态
	 * @author Administrator
	 *
	 */
	public enum BatchOrderStat{
		BatchOrderStat_10("10","草稿"),
		BatchOrderStat_20("20","取消"),
		BatchOrderStat_30("30","待处理"),
		BatchOrderStat_40("40","处理中"),
		BatchOrderStat_00("00","处理成功"),
		BatchOrderStat_99("99","处理失败");

		private String code;
		private String stat;

		BatchOrderStat(String code, String stat) {
			this.code = code;
			this.stat = stat;
		}

		public static String findStat(String code){
			for (BatchOrderStat t : BatchOrderStat.values()) {
				if (t.getCode().contains(code)) {
					return t.getStat();
				}
			}
			return null;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getStat() {
			return stat;
		}

		public void setStat(String stat) {
			this.stat = stat;
		}



	}

	/**
	 * 订单类型
	 * @author Administrator
	 *
	 */
	public enum BatchOrderType{
		BatchOrderType_100("100","批量开户"),
		BatchOrderType_200("200","批量开卡"),
		BatchOrderType_300("300","批量充值");

		private String code; 
		private String type;

		BatchOrderType(String code, String type) {
			this.code = code;
			this.type = type;
		}

		public static String findType(String code){
			for (BatchOrderType t : BatchOrderType.values()) {
				if (t.getCode().contains(code)) {
					return t.getType();
				}
			}
			return null;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}
	
}
