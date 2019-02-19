package com.ebeijia.zl.facade.account.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=false)
public class AccountLogVO implements Serializable {

    /**
     * 交易日志主键
     */
   private String txnPrimaryKey;

    /**
     * 交易日期
     */
    private String txnDate;

    /**
     * 交易时间
     */
    private String txnTime;
    /**
     * 交易类型 0：开户 1：加款 2：减款
     */
    private String accType;

    /**
     * 交易金额
     */
    private BigDecimal txnAmt;

    /**
     * 交易后余额
     */
    private BigDecimal accTotalBal;

    /**
     * 交易描述
     */
    private String transDesc;

    /**
     * 交易数量
     */
    private String transNumber;

    /**
     * 交易类型
     */
    private String transId;

    /**
     * 专项类型
     */
    private String priBId;

    /**
     * 专项账户名称
     */
    private String bName;

    /**
     * 商户主键
     */
    private String mchntCode;

    /**
     * 商户名称
     */
    private String mchntName;

   /**
    * 交易渠道
    */
   private String transChnl;

    /***
    * 用户类型
    */
   private String userType;

 /**
  * 交易流水
  */
 private String itfPrimaryKey;

    /***
     * 专项类型分类
     */
    private String code;

}
