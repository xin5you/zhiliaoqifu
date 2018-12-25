package com.ebeijia.zl.facade.account.vo;

import java.io.Serializable;
import java.math.BigDecimal;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AccountVO implements Serializable {
	
	/**
	 * 账户号
	 */
    private String accountNo;
    
    /**
     * 用户信息_id
     */
    private String userId;
    
    /**
     * 用户名称
     */
    private String userName;
    
    /**
     * 类型id
     */
    private String bId;
    
    
    /**
     * 余额明文
     */
    private BigDecimal accBal;
    
    
    /**
     * 代金券额度
     */
    private BigDecimal couponBal;
    
    
    /**
     * 个人名称
     */
    private String personalName;

}
