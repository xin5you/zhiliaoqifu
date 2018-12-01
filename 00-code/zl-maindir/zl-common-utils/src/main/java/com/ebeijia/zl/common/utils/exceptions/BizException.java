package com.ebeijia.zl.common.utils.exceptions;

 /**
	* 
	* 
	* @ClassName: BizException.java
	* @Description: 所有业务异常都必须继承于此异常
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月29日 下午4:34:48 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月29日     zhuqi           v1.0.0
  */
public class BizException extends RuntimeException {

	private static final long serialVersionUID = -5875371379845226068L;


	/**
	 * 异常信息
	 */
	protected String msg;

	/**
	 * 具体异常码
	 */
	protected int code;

	public BizException(int code, String msgFormat, Object... args) {
		super(String.format(msgFormat, args));
		this.code = code;
		this.msg = String.format(msgFormat, args);
	}

	public BizException() {
		super();
	}

	public String getMsg() {
		return msg;
	}

	public int getCode() {
		return code;
	}

	/**
	 * 实例化异常
	 * 
	 * @param msgFormat
	 * @param args
	 * @return
	 */
	public BizException newInstance(String msgFormat, Object... args) {
		return new BizException(this.code, msgFormat, args);
	}

	public BizException(String message, Throwable cause) {
		super(message, cause);
	}

	public BizException(Throwable cause) {
		super(cause);
	}

	public BizException(String message) {
		super(message);
	}
}
