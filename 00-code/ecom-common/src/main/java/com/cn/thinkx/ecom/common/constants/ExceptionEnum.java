package com.cn.thinkx.ecom.common.constants;

public class ExceptionEnum {

	public static final String SUCCESS_CODE = "00";
	public static final String SUCCESS_MSG = "操作成功";
	
	public static final String ERROR_CODE = "999";
	public static final String ERROR_MSG = "系统故障，请稍后再试";

	public enum loginNews {
		LN01("01", "会话过期，请重新登录"), 
		LN02("02", "验证码不正确，请重新输入"), 
		LN03("03", "用户名或密码错误，请重新输入"), 
		LN04("04", "登录失败，系统出错"),
		LN05("05", "无权限");

		private String code;
		private String msg;

		private loginNews(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public static loginNews findByCode(String code) {
			for (loginNews t : loginNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	public enum userNews {
		UN01("01", "新增用户出错"), 
		UN02("02", "编辑用户出错"), 
		UN03("03", "删除用户出错"), 
		UN04("04", "查询用户列表信息出错"),
		UN05("05", "用户名已存在，请重新输入"),
		UN06("06", "新增用户角色信息出错"),
		UN07("07", "两次密码输入不一致，请重新输入"),
		UN08("08", "请输入正确的旧密码"),
		UN09("09", "修改密码失败，请重新提交"),
		UN10("10", "请至少选中一项");

		private String code;
		private String msg;

		private userNews(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public static userNews findByCode(String code) {
			for (userNews t : userNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	public enum roleNews {
		REN01("01", "新增角色出错"), 
		REN02("02", "编辑角色出错"), 
		REN03("03", "删除角色出错"), 
		REN04("04", "查询角色列表信息出错"),
		REN05("05", "该角色名称已存在，请重新输入"),
		REN06("06", "新增角色资源信息出错");
		

		private String code;
		private String msg;

		private roleNews(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public static roleNews findByCode(String code) {
			for (roleNews t : roleNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	public enum resourceNews {
		RN01("01", "新增资源出错"), 
		RN02("02", "编辑资源出错"), 
		RN03("03", "删除资源出错"), 
		RN04("04", "查询资源列表信息出错"),
		RN05("05", "该资源信息已存在，请重新输入");

		private String code;
		private String msg;

		private resourceNews(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public static resourceNews findByCode(String code) {
			for (resourceNews t : resourceNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	public enum eshopInfNews {
		ES01("01", "新增商城出错"), 
		ES02("02", "编辑商城出错"), 
		ES03("03", "删除商城出错"), 
		ES04("04", "查询商城列表信息出错"),
		ES05("05", "该商城信息已存在，请重新输入");

		private String code;
		private String msg;

		private eshopInfNews(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public static eshopInfNews findByCode(String code) {
			for (eshopInfNews t : eshopInfNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	public enum routesNews {
		RO01("01", "新增电商入口出错"), 
		RO02("02", "编辑电商入口出错"), 
		RO03("03", "删除电商入口出错"), 
		RO04("04", "查询电商入口列表信息出错"),
		RO05("05", "该电商名称已存在，请重新输入"),
		RO06("06", "新增商城电商入口中间表出错"),
		RO07("07", "编辑商城电商入口中间表出错"),
		RO08("08", "该商城与电商信息已存在，请重新输入"),
		RO09("09", "商城LOGO图片上传返回地址失败"),
		RO10("10", "商城LOGO图片上传失败");

		private String code;
		private String msg;

		private routesNews(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public static routesNews findByCode(String code) {
			for (routesNews t : routesNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	public enum bannerNews {
		BA01("01", "新增Banner出错"), 
		BA02("02", "编辑Banner出错"), 
		BA03("03", "删除Banner出错"), 
		BA04("04", "查询Banner列表信息出错"),
		BA05("05", "该Banner信息已存在，请重新输入"),
		BA06("06", "新增商城Banner中间表出错"),
		BA07("07", "编辑商城Banner中间表出错"),
		BA08("08", "该商城与Banner信息已存在，请重新输入"),
		BA09("09", "Banner上传的图片为空，请重新选择"),
		BA10("10", "Banner图片上传返回地址失败"),
		BA11("11", "Banner图片上传失败"),
		BA12("12", "删除商城Banner失败");

		private String code;
		private String msg;

		private bannerNews(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public static bannerNews findByCode(String code) {
			for (bannerNews t : bannerNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	
	public enum floorNews {
		FN01("01", "新增电商楼层出错"), 
		FN02("02", "编辑电商楼层出错"), 
		FN03("03", "删除电商楼层出错"), 
		FN04("04", "查询电商楼层列表信息出错"),
		FN05("05", "该电商楼层信息已存在，请重新输入"),
		FN06("06", "添加电商楼层商品出错");

		private String code;
		private String msg;

		private floorNews(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public static floorNews findByCode(String code) {
			for (floorNews t : floorNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	public enum memberNews {
		M01("01", "添加收货地址异常"), 
		M02("02", "收货地址最多只能添加五个"),
		M03("03", "修改收货地址异常"),
		M04("04", "该用户已是会员"),
		M05("05", "添加商城会员异常"),
		M06("06", "删除收货地址失败"),
		M07("07", "修改默认收货地址失败");

		private String code;
		private String msg;

		private memberNews(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public static memberNews findByCode(String code) {
			for (memberNews t : memberNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 添加购物车异常信息
	 * 
	 * @author kpplg
	 *
	 */
	public enum cartNews {
		C01("01", "添加商品不存在"), 
		C02("02", "新增购物车表信息出错"),
		C03("03", "库存不足"),
		C04("04", "该商品下架喽"),
		C05("05", "购买的数量有误"),;

		private String code;
		private String msg;

		private cartNews(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public static cartNews findByCode(String code) {
			for (cartNews t : cartNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * 商城异常类型
	 * 
	 * @author kpplg
	 *
	 */
	public enum exceptionType {
		ET1000("1000", "网易严选下订单"), 
		ET1001("1001", "网易严选 取消订单"),
		ET1002("1002", "网易严选 订单确认收货"),
		ET1003("1003", "网易严选 订单信息查询"),
		ET1004("1004", "网易严选 获取物流轨迹信息"),
		ET1005("1005", "订单取消回调"),
		ET1006("1006", "订单包裹物流绑单回调"),
		ET1007("1007", "订单异常回调"),
		ET1008("1008", "SKU库存划拨回调"),
		ET1009("1009", "SKU库存校准回调"),
		ET1010("1010", "SKU低库存预警通知"),
		ET1011("1011", "SKU再次开售通知"),
		ET1012("1012", "退货地址回调"),
		ET1013("1013", "严选拒绝退货回调"),
		ET1014("1014", "退货包裹确认收货回调"),
		ET1015("1015", "严选系统取消退货回调"),
		ET1016("1016", "退款结果回调"),
		ET1017("1017", "发起售后服务请求"),
		ET1018("1018", "取消售后服务请求"),
		ET1019("1019", "绑定售后寄回物流单号"),
		ET1020("1020", "查询售后申请详情");

		private String code;
		private String msg;

		private exceptionType(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public static exceptionType findByCode(String code) {
			for (exceptionType t : exceptionType.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	public enum placeOrderNews {
		PON01("01", "重复下单失败"), 
		PON02("02", "订单取消失败");

		private String code;
		private String msg;

		private placeOrderNews(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public static placeOrderNews findByCode(String code) {
			for (placeOrderNews t : placeOrderNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
	
	
	public enum EcomOrderRefundNews {
		PON01("01", "订单退款失败");

		private String code;
		private String msg;

		private EcomOrderRefundNews(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public static EcomOrderRefundNews findByCode(String code) {
			for (EcomOrderRefundNews t : EcomOrderRefundNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
}
