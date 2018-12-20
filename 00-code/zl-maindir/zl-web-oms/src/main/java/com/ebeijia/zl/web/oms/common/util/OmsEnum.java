package com.ebeijia.zl.web.oms.common.util;

public class OmsEnum {

	//字典表
	public static final String TB_BASE_DICT = "TB_BASE_DICT";

	//平台手续费
	public static final String PLATFORM_FEE = "PLATFORM_FEE";

	/**
	 * 订单状态
	 * @author Administrator
	 *
	 */
	public enum BatchOrderStat{
		BatchOrderStat_10("10","草稿"),
		BatchOrderStat_20("20","取消"),
		BatchOrderStat_30("30","处理中"),
		BatchOrderStat_40("40","部分成功"),
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

}
