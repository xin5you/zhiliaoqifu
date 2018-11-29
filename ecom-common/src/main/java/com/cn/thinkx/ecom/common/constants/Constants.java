package com.cn.thinkx.ecom.common.constants;

public class Constants {
	
	/**
	 * 用户的openId  session key
	 */
	public static final String OPENID_SESSION_KEY ="SESSION_OPENDID_LIFE";
	
	/**
	 * front 用户的session key
	 */
	public static final String FRONT_MEMBER_ID_KEY= "FRONT_MEMBER_ID_KEY";

	public static final String SESSION_USER = "sessionUser";
	
	// 日切允许状态
	public static final String TRANS_FLAG_YES = "1";
	
	//货币类型
	public static final String TRANS_CURR_CD = "156";
	
	//知了企服返回交易成功状态
	public static final String HKB_SUCCESS = "SUCCESS";
	
	//知了企服返回交易失败状态
	public static final String HKB_FAIL = "FAIL";
	
	// 知了企服通卡信息
	public static final String ACC_HKB_MCHNT_NO = "ACC_HKB_MCHNT_NO";
	public static final String ACC_HKB_PROD_NO = "ACC_HKB_PROD_NO";
	public static final String ACC_HKB_INS_CODE = "ACC_HKB_INS_CODE";
	public static final String ACC_HKB_SHOP_NO = "ACC_HKB_SHOP_NO";
	public static final String ACC_ITF = "ACC_ITF";
	public static final String ACC_HKB = "ACC_HKB";
	
	/** 海豚通通兑*/
	public static final String HTTTD_3DES_KEY = "HTTTD_3DES_KEY";//加密密钥
	public static final String HTTTD_VALUES_URL = "HTTTD_VALUES_URL";//获取海豚通通兑进入商城前获取参数链接
	public static final String HTTTD_HTTP_URL = "HTTTD_HTTP_URL";//跳转海豚通通兑商城链接
	public static final String HTTTD_APPID = "HTTTD_APPID";//海豚通通兑appId参数
	public static final String HTTTD_INSTITUTIONNO = "HTTTD_INSTITUTIONNO";//海豚通通兑机构号
	
	/** 鼎驰 */
	public static final String DINGCHI_HTTP_URL = "DINGCHI_HTTP_URL";
	public static final String DINGCHI_BUY_URL = "DINGCHI_BUY_URL";
	public static final String DINGCHI_QUERY_URL = "DINGCHI_QUERY_URL";
	public static final String DINGCHI_QUERYBAL_URL = "DINGCHI_QUERYBAL_URL";
	
	//手机充值退款加密key
	public static final String PHONE_RECHARGE_REQ_KEY = "PHONE_RECHARGE_REQ_KEY";
	public static final String PHONE_RECHARGE_REFUND = "TRANS_ORDER_REFUND";
	
	/** 自建电商退款*/
	public static final String HKB_STORE_REFUND_KEY = "MYESHOP_SIGN_KEY";
	public static final String HKB_STORE_REFUND_URL = "ECOM_REFUND_URL";
	
	/**
	 * foot菜单枚举
	 * 
	 * @author xiaomei
	 *
	 */
	public enum FootCodeType {

		INDEX(1, "INDEX", "首页"),
		CATE(2, "CATE", "分类"),
		CART(3, "CART", "购物车"),
		MY(4, "MY", "我的");

		private int id;
		private String code;
		private String name;

		FootCodeType(int id, String code, String name) {
			this.id = id;
			this.code = code;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public int getId() {
			return id;
		}

		public String getCode() {
			return code;
		}
	}
	
	/**
	 * 随机码类型
	 * 
	 * @author xiaomei
	 *
	 */
	public enum RandomCodeType {

		LOGIN(1, "random_code_login", "登录");

		private int id;
		private String code;
		private String name;

		RandomCodeType(int id, String code, String name) {
			this.id = id;
			this.code = code;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public int getId() {
			return id;
		}

		public String getCode() {
			return code;
		}

		public static RandomCodeType findByCode(int id) {
			for (RandomCodeType value : RandomCodeType.values()) {
				if (value.id == id) {
					return value;
				}
			}
			return null;
		}
	}

	/**
	 * 正常停用状态
	 * 
	 * @author xiaomei
	 *
	 */
	public enum PrmStat {
		PS0("0", "正常"),
		PS1("1", "停用");

		private String code;
		private String value;

		PrmStat(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static PrmStat findByCode(String code) {
			for (PrmStat t : PrmStat.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}

	/**
	 * 图片上传应用种类
	 * 
	 * @author xiaomei
	 *
	 */
	public enum ImageType {
		IE01("01", "banner"), 
		IE02("02", "logo");

		private String code;
		private String value;

		ImageType(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static ImageType findByCode(String code) {
			for (ImageType t : ImageType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	
	/**
	 * 商城 接入渠道
	 * 
	 * @author xiaomei
	 *
	 */
	public enum GoodsEcomCodeType {
		ECOM00("66661000", "知了企服商城"),
		ECOM01("66661001", "网易严选"),
		ECOM02("66661002", "苏宁易购"),
		ECOM03("66661003", "京东商城");
		
		private String code;
		private String value;
		
		GoodsEcomCodeType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static GoodsEcomCodeType findByCode(String code) {
			for (GoodsEcomCodeType t : GoodsEcomCodeType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	public enum GoodsSkuSyncType {
		SKU01("0", "未同步"),
		SKU02("1", "已同步");
		
		private String code;
		private String value;
		
		GoodsSkuSyncType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static GoodsSkuSyncType findByCode(String code) {
			for (GoodsSkuSyncType t : GoodsSkuSyncType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	public enum GoodsCategoryListShowType{
		ListShow00("0","未显示"),
		ListShow01("1","显示");
		
		private String code;
		private String value;
		
		GoodsCategoryListShowType(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public static GoodsCategoryListShowType findByCode(String code) {
			for (GoodsCategoryListShowType t : GoodsCategoryListShowType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
		
	}
	
	/**
	 * 一级门店支付状态
	 * 
	 * @author kpplg
	 *
	 */
	public enum PlatfOrderPayStat {
		PayStat00("0","未付款"),
		PayStat01("1","已付款待确认"),
		PayStat02("2","已付款"),
		PayStat03("3","已退款"),
		PayStat04("4","部分退款"),
		PayStat05("5","部分付款"),
		PayStat08("8","已取消"),
		PayStat09("9","已完成");
		
		private String code;
		private String value;
		
		PlatfOrderPayStat(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
		public static PlatfOrderPayStat findByCode(String code) {
			for (PlatfOrderPayStat t : PlatfOrderPayStat.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
		
	}
	
	
	public enum GoodsProductType {
		Product00("0", "可售"),
		Product01("1", "不可售");
		
		private String code;
		private String value;
		
		GoodsProductType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static GoodsProductType findByCode(String code) {
			for (GoodsProductType t : GoodsProductType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	public enum GoodsMarketType {
		Market01("0", "已下架"),
		Market02("1", "已上架");
		
		private String code;
		private String value;
		
		GoodsMarketType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static GoodsMarketType findByCode(String code) {
			for (GoodsMarketType t : GoodsMarketType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}

	/**
	 * 渠道号与电商名称
	 * 
	 * @author xiaomei
	 *
	 */
	public enum ChannelEcomType {
		CEU01("40006001", "海易通商城"),
		CEU02("40006002", "家乐宝商城"),
		CEU03("40006003", "京东商城"),
		CEU04("40006004", "美团"),
		CEU05("40006005", "大众点评"),
		CEU06("40006006", "自营商城"),
		CEU07("40006007", "海豚通商城"),
		CEU08("40007001", "手机充值"),
		CEU09("40008001", "卡券集市");
		
		private String code;
		private String value;
		
		ChannelEcomType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static ChannelEcomType findByCode(String code) {
			for (ChannelEcomType t : ChannelEcomType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 订单状态
	 * 
	 * @author xiaomei
	 *
	 */
	public enum OrderType {
		OT00("0", "未支付"),
		OT01("1", "支付失败"),
		OT02("2", "支付完成"),
		OT03("3", "退款失败"),
		OT04("4", "退款成功");
		
		private String code;
		private String value;
		
		OrderType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static OrderType findByCode(String code) {
			for (OrderType t : OrderType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	
	/**
	 * 订单类型
	 * 
	 * @author xiaomei
	 *
	 */
	public enum PhoneRechargeOrderType {
		OrderType1("1", "话费充值"),
		OrderType2("2", "流量充值");
		
		private String code;
		private String value;
		
		PhoneRechargeOrderType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static PhoneRechargeOrderType findByCode(String code) {
			for (PhoneRechargeOrderType t : PhoneRechargeOrderType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	public enum SupplierType{
		SupplierType1001("S1001","力方"),
		SupplierType1002("S1002","鼎驰");
		
		private String code;
		private String value;
		
		SupplierType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static SupplierType findByCode(String code) {
			for (SupplierType t : SupplierType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 手机充值交易状态
	 * 
	 * @author xiaomei
	 *
	 */
	public enum PhoneRechargeTransStat {
		TransStat0("0", "未付款"),
		TransStat1("1", "充值中"),
		TransStat2("2", "充值成功"),
		TransStat3("3", "充值失败"),
		TransStat4("4", "受理成功"),
		TransStat5("5", "退款成功"),
		TransStat6("6", "退款失败");
		
		private String code;
		private String value;
		
		PhoneRechargeTransStat(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static PhoneRechargeTransStat findByCode(String code) {
			for (PhoneRechargeTransStat t : PhoneRechargeTransStat.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	public enum RechargeState{
		RechargeState00("0","充值中"),
		RechargeState01("1","充值成功"),
		RechargeState09("9","充值失败");
		
		private String code;
		private String value;
		
		RechargeState(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static RechargeState findByCode(String code) {
			for (RechargeState t : RechargeState.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
		
	}
	
	public enum PhoneRechargeChannelType{
		PRC1001("P1001","知了企服余额"),
		PRC1002("P1002","卡密充值"),
		PRC1003("P1003","API对接"),
		PRC1004("P1004","分销商");
		
		private String code;
		private String value;
		
		PhoneRechargeChannelType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static PhoneRechargeChannelType findByCode(String code) {
			for (PhoneRechargeChannelType t : PhoneRechargeChannelType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}

	
	 /**
	 *
	 * 交易类型
	 *
	 */
	public enum TransCode {
		MB80("B80", "商户开户"), 
		MB10("B10", "商户提现"), 
		MB20("B20", "商户沉淀资金账户充值"),
		MB30("B30", "商户网站退货交易"),
		CW80("W80", "客户开户"),
		CW81("W81", "密码重置"),
		CW10("W10", "客户消费"),
		CW20("W20", "客户充值"),
		CW71("W71", "快捷支付"),
		CW11("W11", "客户交易撤销"),
		CW74("W74", "快捷支付交易撤销");
		
		private String code;
		private String value;
		
		TransCode(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static TransCode findByCode(String code) {
			for (TransCode t : TransCode.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 渠道类型
	 * 
	 * @author xiaomei
	 *
	 */
	public enum ChannelCode {
//		CHANNEL1("40001001"),// 商户开户、客户开户、密码重置、消费 (从微信公众号发起)
		CHANNEL1("40001010"),
		CHANNEL2("40002001"),// 充值、商户提现  (从微信公众号发起)
		CHANNEL3("40003001"),// 充值、商户提现  (从支付宝发起)
		CHANNEL4("40004001"),// 充值、商户提现  (从嘉福平台发起)
		CHANNEL5("40005001"),// 充值、商户提现  (从网银向本系统发起)
		CHANNEL6("40006001"),//电商平台发起
		CHANNEL8("40007001");//手机充值(话费充值)
		
		private final String value;

		ChannelCode(String value) {
			this.value = value;
		};
		
		@Override
		public String toString() {
			return this.value;
		}
		
		public static ChannelCode findByCode(String code) {
			for (ChannelCode t : ChannelCode.values()) {
				if (t.value.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 渠道授权支付，退款，查单错误码
	 * 
	 * @author xiaomei
	 *
	 */
	public enum transType {
		T00("0000", "交易成功"),
		T10("0010", "交易重复"),
		T08("0008", "余额不足"),
		T09("0009", "交易不存在"),
		T99("9999", "交易失败");
		
		private String code;
		private String value;
		
		transType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static transType findByCode(String code) {
			for (transType t : transType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 知了企服交易接口返回码
	 * 
	 * @author xiaomei
	 *
	 */
	public enum MiddlewareType {
		MT00("00", "交易成功"),
		MT96("96", "重复交易"),
		MT51("51", "余额不足"),
		MT99("99", "系统故障");
		
		private String code;
		private String value;
		
		MiddlewareType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static MiddlewareType findByCode(String code) {
			for (MiddlewareType t : MiddlewareType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 电商类型
	 * 
	 * @author kpplg
	 *
	 */
	public enum EcomType {
		ET01("1000", "第三方商城"),
		ET02("2000", "自建商城");
		
		private String code;
		private String value;
		
		EcomType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static EcomType findByCode(String code) {
			for (EcomType t : EcomType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 楼层显示隐藏
	 * 
	 * @author kpplg
	 *
	 */
	public enum FloorIsDisplay {
		ID00("0", "显示"),
		ID01("1", "隐藏");
		
		private String code;
		private String value;
		
		FloorIsDisplay(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static FloorIsDisplay findByCode(String code) {
			for (FloorIsDisplay t : FloorIsDisplay.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 楼层类型
	 * 
	 * @author kpplg
	 *
	 */
	public enum FloorType {
		ID00("0", "首页"),
		ID01("1", "电商"),
		ID02("2", "个人信息");
		
		private String code;
		private String value;
		
		FloorType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static FloorType findByCode(String code) {
			for (FloorType t : FloorType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 电商平台包裹状态
	 * 
	 * @author 朱秋友
	 *
	 */
	public enum PackageStatus{
		PACK_STAT00("00", "待发货"),
		PACK_STAT10("10", "已出库"),
		PACK_STAT20("20", "已签收"),
		PACK_STAT90("90", "已完成");
		
		private String code;
		private String value;
		
		PackageStatus(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static PackageStatus findByCode(String code) {
			for (PackageStatus t : PackageStatus.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	/**
	 * 电商平台门店订单中：订单状态
	 * 
	 * @author kpplg
	 *
	 */
	public enum SubOrderStatus {
		SOS00("00","未付款"),
		SOS10("10", "待发货"),
		SOS11("11", "已出库"),
		SOS12("12", "已发货"),
		SOS13("13", "已收货"),
		SOS14("14", "已完成"),
		SOS15("15", "作废"),
		SOS20("20", "申请换货"),
		SOS21("21", "已换货"),
		SOS22("22", "申请退货"),
		SOS23("23", "已退货"),
		SOS24("24", "换货被拒绝"),
		SOS25("25", "退货被拒绝"),
		SOS26("26", "申请取消"),
		SOS27("27", "已取消"),
		SOS28("28", "取消被拒"),
		SOS44("44", "外部渠道发货失败");
		private String code;
		private String value;
		
		SubOrderStatus(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static SubOrderStatus findByCode(String code) {
			for (SubOrderStatus t : SubOrderStatus.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 退款状态
	 * 
	 * @author kpplg
	 *
	 */
	public enum RefundStatus {
		RFS0("0", "申请中"),
		RFS1("1", "已退款"),
		RFS2("2", "退款失败");
		
		private String code;
		private String value;
		
		RefundStatus(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static RefundStatus findByCode(String code) {
			for (RefundStatus t : RefundStatus.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	/**
	 * 订单退换货申请表
	 * 审核状态
	 * @author kpplg
	 *
	 */
	public enum ReturnsStatus {
		RS0("0", "申请中"),
		RS1("1", "已拒绝"),
		RS2("2", "已同意"),
		RS3("3", "已删除"),
		RS4("4", "已取消"),
		RS9("9", "已完成");
		
		private String code;
		private String value;
		
		ReturnsStatus(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static ReturnsStatus findByCode(String code) {
			for (ReturnsStatus t : ReturnsStatus.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 订单退换货申请表
	 * 退换货类型
	 * @author kpplg
	 *
	 */
	public enum ReturnsType {
		RT1("1", "退货"),
		RT2("2", "渠道取消"),
		RT3("3", "用户申请取消"),
		RT4("4", "换货"),
		RT5("5", "退款"),
		RT6("6", "平台取消");
		
		private String code;
		private String value;
		
		ReturnsType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static ReturnsType findByCode(String code) {
			for (ReturnsType t : ReturnsType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 订单包裹物流信息
	 * 是否签收标识
	 * 
	 * @author kpplg
	 *
	 */
	public enum IsSign {
		IS0("0", "未签收"),
		IS1("1", "已签收");
		
		private String code;
		private String value;
		
		IsSign(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static IsSign findByCode(String code) {
			for (IsSign t : IsSign.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
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
		templateMsgRefund0("40001010", "退款成功"),
		templateMsgRefund1("40006001", "海镱通商城购物异常"), 
		templateMsgRefund2("40006002", "家乐宝商城购物异常"), 
		templateMsgRefund3("40006003", "京东商城购物异常"), 
		templateMsgRefund4("40006004", "美团购物异常"), 
		templateMsgRefund5("40006005", "大众点评购物异常"), 
		templateMsgRefund6("40006006", "自营商城购物异常"), 
		templateMsgRefund7("40006007", "海豚通商城购物异常"), 
		templateMsgRefund8("40007001", "话费充值异常"), 
		TemplateMsgRefund9("40008001", "卡券购买异常"); 

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
	
	/**
	 * 模板消息 显示支付渠道信息
	 * 
	 * @author xiaomei
	 *
	 */
	public enum templateMsgPayment{
		templateMsgPayment1("40006001", "海易通商城"), 
		templateMsgPayment2("40006002", "家乐宝商城"),
		templateMsgPayment3("40006003", "京东商城"),
		templateMsgPayment4("40006004", "美团"),
		templateMsgPayment5("40006005", "大众点评"),
		templateMsgPayment6("40006006", "自营商城"),
		templateMsgPayment7("40006007", "海豚通商城"),
		templateMsgPayment8("40007001", "手机充值"),
		templateMsgPayment9("40008001", "卡券集市");

		private String code;
		private String value;

		templateMsgPayment(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static templateMsgPayment findByCode(String code) {
			for (templateMsgPayment t : templateMsgPayment.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 退款接口 标志
	 * @author xiaomei
	 *
	 */
	public enum refundFalg{
		refundFalg1("1", "系统退款"), 
		refundFalg2("2", "用户端退款");

		private String code;
		private String value;

		refundFalg(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static refundFalg findByCode(String code) {
			for (refundFalg t : refundFalg.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 交易返回码信息
	 * 
	 * @author xiaomei
	 *
	 */
	public enum transRespCode{
		transRespCode1("00", "交易成功"), 
		transRespCode2("96", "网络异常，请稍后再试"),
		transRespCode3("99", "交易失败");

		private String code;
		private String value;

		transRespCode(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

		public static transRespCode findByCode(String code) {
			for (transRespCode t : transRespCode.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 退货商品表  or  订单货品明细  - 退货状态标识
	 * 
	 * @author kpplg
	 *
	 */
	public enum ApplyReturnState {
		ARS0("0", "未退货"),
		ARS1("1", "待发货"),
		ARS2("2", "待收货"),
		ARS3("3", "已收货"),
		ARS4("4", "已退款"),
		ARS5("5", "取消退货(用户取消)"),
		ARS6("6", "取消退货(渠道取消)"),
		ARS9("9", "拒接退货");
		
		private String code;
		private String value;
		
		ApplyReturnState(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static ApplyReturnState findByCode(String code) {
			for (ApplyReturnState t : ApplyReturnState.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 退货包裹确认收货信息表-退货包裹状态
	 * 
	 * @author kpplg
	 *
	 */
	public enum ConfirmReturnsStatus {
		RS10("10", "已发货"),
		RS20("20", "已签收");
		
		private String code;
		private String value;
		
		ConfirmReturnsStatus(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static ConfirmReturnsStatus findByCode(String code) {
			for (ConfirmReturnsStatus t : ConfirmReturnsStatus.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	public enum returnType{
		returnType01("1", "无理由"),
		returnType02("2", "质量问题");
		
		private String code;
		private String value;
		
		returnType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getValue() {
			return value;
		}
		
		public static returnType findByCode(String code) {
			for (returnType t : returnType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
}
