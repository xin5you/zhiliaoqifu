package com.ebeijia.zl.common.utils.constants;

public class ExceptionEnum {

	public static final String SUCCESS_CODE = "00";
	public static final String SUCCESS_MSG = "操作成功";

	public static final String ERROR_CODE = "999";
	public static final String ERROR_MSG = "系统故障，请稍后再试";

	public enum LoginNews {
		LN01("01", "会话过期，请重新登录"),
		LN02("02", "验证码不正确，请重新输入"),
		LN03("03", "用户名或密码错误，请重新输入"),
		LN04("04", "登录失败，系统出错"),
		LN05("05", "无权限");

		private String code;
		private String msg;

		private LoginNews(String code, String msg) {
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

		public static LoginNews findByCode(String code) {
			for (LoginNews t : LoginNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}

	public enum UserNews {
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

		private UserNews(String code, String msg) {
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

		public static UserNews findByCode(String code) {
			for (UserNews t : UserNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}

	public enum RoleNews {
		REN01("01", "新增角色出错"),
		REN02("02", "编辑角色出错"),
		REN03("03", "删除角色出错"),
		REN04("04", "查询角色列表信息出错"),
		REN05("05", "该角色名称已存在，请重新输入"),
		REN06("06", "新增角色资源信息出错"),
		REN07("07", "该角色序号已存在，请重新输入"),
		REN08("08", "该角色已有关联关系，删除失败");


		private String code;
		private String msg;

		private RoleNews(String code, String msg) {
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

		public static RoleNews findByCode(String code) {
			for (RoleNews t : RoleNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}

	public enum ResourceNews {
		RN01("01", "新增资源出错"),
		RN02("02", "编辑资源出错"),
		RN03("03", "删除资源出错"),
		RN04("04", "查询资源列表信息出错"),
		RN05("05", "该资源信息已存在，请重新输入");

		private String code;
		private String msg;

		private ResourceNews(String code, String msg) {
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

		public static ResourceNews findByCode(String code) {
			for (ResourceNews t : ResourceNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}

	/**
	 * 订单管理异常枚举
	 *
	 * @author xiaomei
	 *
	 */
	public enum PlatfOrderNewsEnum {

		PlatfOrderNews_01("01","订单不存在"),
		PlatfOrderNews_02("02","订单发货失败"),
		PlatfOrderNews_03("03","订单货品信息不存在"),
		PlatfOrderNews_04("02","订单发货,更新物流信息失败");

		private String code;
		private String msg;

		PlatfOrderNewsEnum(String code, String msg) {
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

		public void setValue(String msg) {
			this.msg = msg;
		}

		public static PlatfOrderNewsEnum findByCode(String code) {
			for (PlatfOrderNewsEnum t : PlatfOrderNewsEnum.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}

	/*
		图片上传枚举值
	 */
	public enum ImageNews {
		ImageNews01("01", "图片上传返回地址失败"),
		ImageNews02("02", "图片上传异常"),
		ImageNews03("03", "请选择图片"),
		ImageNews04("04", "删除图片异常");

		private String code;
		private String msg;

		private ImageNews(String code, String msg) {
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

		public static ImageNews findByCode(String code) {
			for (ImageNews t : ImageNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}

	/**
	 * 商品规格枚举
	 */
	public enum GoodsSpecNews {
		GoodsSpecNews01("01", "新增商品规格失败"),
		GoodsSpecNews02("02", "编辑商品规格失败"),
		GoodsSpecNews03("03", "删除商品规格失败"),
		GoodsSpecNews04("04", "新增商品规格值失败"),
		GoodsSpecNews05("05", "编辑商品规格值失败"),
		GoodsSpecNews06("06", "删除商品规格值失败"),
		GoodsSpecNews07("07", "新增商品信息失败"),
		GoodsSpecNews08("08", "编辑商品信息失败"),
		GoodsSpecNews09("09", "删除商品信息失败"),
		GoodsSpecNews10("10", "更新上下架状态失败"),
		GoodsSpecNews11("11", "新增商品相册信息失败"),
		GoodsSpecNews12("12", "编辑商品相册信息失败"),
		GoodsSpecNews13("13", "删除商品相册信息失败"),
		GoodsSpecNews14("14", "排序号已存在，请重新输入"),
		GoodsSpecNews15("15", "新增商品Sku失败"),
		GoodsSpecNews16("16", "编辑商品Sku失败"),
		GoodsSpecNews17("17", "删除商品Sku失败"),
		GoodsSpecNews18("18", "SkuCode已存在，请重新输入");

		private String code;
		private String msg;

		private GoodsSpecNews(String code, String msg) {
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

		public static GoodsSpecNews findByCode(String code) {
			for (GoodsSpecNews t : GoodsSpecNews.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}
			return null;
		}
	}
}