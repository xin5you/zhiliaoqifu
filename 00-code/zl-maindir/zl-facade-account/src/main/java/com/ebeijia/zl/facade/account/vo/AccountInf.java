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
 * 用户账户信息
 *
 * @User zhuqi
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_account_inf")
public class AccountInf extends Model<AccountInf> {
 
    /**
     * 主账户号
     */
    @TableId(value = "account_no" ,type = IdType.UUID)
    private String accountNo;
 
    /**
     * 用户信息_id
     */
    @TableField("user_id")
    private String userId;
 
    /**
     * 类型id
     */
    @TableField("b_id")
    private String bId;
 
    /**
     * 账户类型
     */
    @TableField("account_type")
    private String accountType;
 
    /**
     * 账户状态
     */
    @TableField("account_stat")
    private String accountStat;
 
    /**
     * 余额明文
     */
    @TableField("acc_bal")
    private BigDecimal accBal;
 
    /**
     * 余额密文
     */
    @TableField("acc_bal_code")
    private String accBalCode;
 
    /**
     * 单笔Pos交易限额
     */
    @TableField("max_txn_amt1")
    private BigDecimal maxTxnAmt1;
 
    /**
     * 每日Pos交易限额
     */
    @TableField("max_day_txn_amt1")
    private BigDecimal maxDayTxnAmt1;
 
    /**
     * 当天Pos交易总金额
     */
    @TableField("day_total_amt1")
    private BigDecimal dayTotalAmt1;
 
    /**
     * 单笔Web交易限额
     */
    @TableField("max_txn_amt2")
    private BigDecimal maxTxnAmt2;
 
    /**
     * 每日Web交易限额
     */
    @TableField("max_day_txn_amt2")
    private BigDecimal maxDayTxnAmt2;
 
    /**
     * 当天Web交易总金额
     */
    @TableField("day_total_amt2")
    private BigDecimal dayTotalAmt2;
 
    /**
     * 冻结金额
     */
    @TableField("freeze_amt")
    private BigDecimal freezeAmt;
 
    /**
     * 最近交易日期
     */
    @TableField("last_txn_date")
    private String lastTxnDate;
 
    /**
     * 最近交易时间
     */
    @TableField("last_txn_time")
    private String lastTxnTime;
 
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
     * 修改人
     */
    @TableField("update_user")
    private String updateUser;
 
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;
 
    /**
     * 修改时间
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
        return this.accountNo;
    }
    

}
