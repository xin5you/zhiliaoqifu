package com.cn.thinkx.ecom.basics.order.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;
/**
 * 商城异常信息
 * 
 * @author kpplg
 *
 */
public class ExceptionInf extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String primaryKey;	//流水号包括：订单id、交易流水号等
	private String inputParameter;	//传入参数：调用接口的传入参数
	private String exceptionType;	//类型
	private String exceptionDesc;	//描述
	private String exceptionStatc;	//异常状态
	private String processTimes;	//处理次数
	
	public String getId() {
		return id;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public String getInputParameter() {
		return inputParameter;
	}
	public String getExceptionType() {
		return exceptionType;
	}
	public String getExceptionDesc() {
		return exceptionDesc;
	}
	public String getExceptionStatc() {
		return exceptionStatc;
	}
	public String getProcessTimes() {
		return processTimes;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public void setInputParameter(String inputParameter) {
		this.inputParameter = inputParameter;
	}
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}
	public void setExceptionDesc(String exceptionDesc) {
		this.exceptionDesc = exceptionDesc;
	}
	public void setExceptionStatc(String exceptionStatc) {
		this.exceptionStatc = exceptionStatc;
	}
	public void setProcessTimes(String processTimes) {
		this.processTimes = processTimes;
	}
	
}
