package com.ebeijia.zl.facade.account.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
* 
* @Description: 账户信息查询列表
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年12月14日 下午2:07:50 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年12月14日     zhuqi           v1.0.0
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class AccountQueryReqVo implements Serializable {

	private static final long serialVersionUID = -5097911751512172022L;

	/**
	 * 100：企业员工账户
	 * 200：企业账户
	 * 300：供应商账户
	 * 400：分销商账户
	 */
	
	private String userType;
	
	/**
	 * 用户外部渠道标识
	 */
	private String userChnlId;
	
	/**
	 * 用户所属渠道
	 */
	private String userChnl;


	/** 账单查询业务字段 begin*/

	/**
	 *  账户日志主键
	 */
	private String actPrimaryKey;
	/**
	 * 专项账户Id
	 */
	private String bId;

	/**
	 * 开始时间 时间戳
	 */
	private Long sDate;

	/**
	 * 结束时间 时间戳
	 */
	private Long eDate;
}
