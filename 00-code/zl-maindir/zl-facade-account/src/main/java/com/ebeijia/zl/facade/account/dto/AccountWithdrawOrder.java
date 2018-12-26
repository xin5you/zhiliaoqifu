package com.ebeijia.zl.facade.account.dto;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 账户交易流水
 *
 * @User zhuqi
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_account_withdraw_order")
public class AccountWithdrawOrder extends Model<AccountWithdrawOrder> {
 
    /**
     * 批次号
     */
    @TableId(value = "batch_no" ,type = IdType.UUID)
    private String batchNo;


    /**
     * 交易流水号
     */
    @TableId(value = "txn_primary_key")
    private String txnPrimaryKey;
 
    /**
     * YFB:苏宁易付宝
     */
    @TableField("w_type")
    private String wType;
 
    /**
     * 付款商户号
     */
    @TableField("merchant_no")
    private String merchantNo;
 
    /**
     * 数据源
     */
    @TableField("datasource")
    private String datasource;
 
    /**
     * 交易笔数
     */
    @TableField("total_num")
    private Integer totalNum;
 
    /**
     * 交易总金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;
 
    /**
     * 成功笔数
     */
    @TableField("success_num")
    private Integer successNum;
 
    /**
     * 成功总金额
     */
    @TableField("success_amount")
    private BigDecimal successAmount;
 
    /**
     * 失败笔数 
     */
    @TableField("fail_num")
    private Integer failNum;
 
    /**
     * 失败金额
     */
    @TableField("fail_amount")
    private BigDecimal failAmount;
 
    /**
     * 总手续费
     */
    @TableField("poundage")
    private BigDecimal poundage;
 
    /**
     * 错误码
     */
    @TableField("error_code")
    private String errorCode;
 
    /**
     * errorMsg
     */
    @TableField("error_msg")
    private String errorMsg;
 
    /**
     * 交易状态
     */
    @TableField("status")
    private String status;
 
    /**
     * 数据状态
     */
    @TableLogic
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
        return this.batchNo;
    }
}
