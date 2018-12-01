package com.ebeijia.zl.facade.account.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * 账户交易日志
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_account_log")
public class AccountLog extends Model<AccountLog> {
 
    /**
     * 账户处理日志
     */
    @TableId(value = "act_primary_key" ,type = IdType.UUID)
    private String actPrimaryKey;
 
    /**
     * 主账户号
     */
    @TableField("account_no")
    private String accountNo;
 
    /**
     * 交易流水号
     */
    @TableField("txn_primary_key")
    private String txnPrimaryKey;
 
    /**
     * 清算日期
     */
    @TableField("settle_date")
    private String settleDate;
 
    /**
     * 批量交易的批次号
     */
    @TableField("bat_no")
    private String batNo;
 
    /**
     * 交易日期
     */
    @TableField("txn_date")
    private String txnDate;
 
    /**
     * 交易时间
     */
    @TableField("txn_time")
    private String txnTime;
 
    /**
     * 交易类型
     */
    @TableField("trans_id")
    private String transId;
 
    /**
     * 交易渠道
     */
    @TableField("trans_chnl")
    private BigDecimal transChnl;
 
    /**
     * 账务处理金额
     */
    @TableField("txn_real_amt")
    private BigDecimal txnRealAmt;
 
    /**
     * 账务处理类型
     */
    @TableField("txn_real_amt_type")
    private String txnRealAmtType;
 
    /**
     * 交易金额
     */
    @TableField("txn_amt")
    private BigDecimal txnAmt;
 
    /**
     * 交易手续费
     */
    @TableField("txn_fee")
    private BigDecimal txnFee;
 
    /**
     * 账户类型
     */
    @TableField("acc_type")
    private String accType;
 
    /**
     * 账户总余额
     */
    @TableField("acc_total_bal")
    private BigDecimal accTotalBal;
 
    /**
     * 账户有效余额
     */
    @TableField("acc_valid_bal")
    private BigDecimal accValidBal;
 
    /**
     * 撤销标志
     */
    @TableField("cancel_flag")
    private String cancelFlag;
 
    /**
     * 冲正标志
     */
    @TableField("revsal_flag")
    private String revsalFlag;
 
    /**
     * 退货标志
     */
    @TableField("return_flag")
    private String returnFlag;
 
    /**
     * 退货金额
     */
    @TableField("return_amt")
    private BigDecimal returnAmt;
 
    /**
     * 调账标志
     */
    @TableField("adjust_flag")
    private String adjustFlag;
 
    /**
     * 调账金额
     */
    @TableField("adjust_amt")
    private BigDecimal adjustAmt;
 
    /**
     * 错误号
     */
    @TableField("err_no")
    private String errNo;
 
    /**
     * 原交易账户流水号
     */
    @TableField("org_act_primary_key")
    private String orgActPrimaryKey;
 
    /**
     * 原交易交易流水号
     */
    @TableField("org_txn_primary_key")
    private String orgTxnPrimaryKey;
 
    /**
     * 原交易批次号
     */
    @TableField("org_bat_no")
    private String orgBatNo;
 
    /**
     * 返回码
     */
    @TableField("resp_code")
    private String respCode;
 
    /**
     * 数据状态
     */
    @TableField("data_stat")
    private String dataStat;
 
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
 
    /**
     * 创建人
     */
    @TableField("create_user")
    private String createUser;
 
    /**
     * 更新人
     */
    @TableField("update_user")
    private String updateUser;
 
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;
 
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Long updateTime;
 
    /**
     * 乐观锁版本
     */
    @Version
    @TableField("lock_version")
    private Integer lockVersion;


    @Override
    protected Serializable pkVal() { 
        return this.actPrimaryKey;
    }
}
