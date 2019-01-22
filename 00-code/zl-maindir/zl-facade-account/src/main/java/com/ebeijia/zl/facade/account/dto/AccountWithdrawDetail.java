package com.ebeijia.zl.facade.account.dto;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 账户提现记录明细
 *
 * @User zhuqi
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_account_withdraw_detail")
public class AccountWithdrawDetail extends Model<AccountWithdrawDetail> {
 
    /**
     * 流水号
     */
    @TableId(value = "serial_no" ,type = IdType.UUID)
    private String serialNo;

    /**
     * 退款交易流水号
     */
    @TableId(value = "ref_primary_key")
    private String refPrimaryKey;

    /**
     * 批次号
     */
    @TableField("batch_no")
    private String batchNo;
 
    /**
     * 用户Id
     */
    @TableField("user_id")
    private String userId;
 
    /**
     * 收款方卡号
     */
    @TableField("receivercard_no")
    private String receivercardNo;
 
    /**
     * 收款方姓名
     */
    @TableField("receiver_name")
    private String receiverName;
 
    /**
     * 收款方类型（PERSON：个人，CORP：企业） 
     */
    @TableField("receiver_type")
    private String receiverType;
 
    /**
     * 收款方币种
     */
    @TableField("receiver_currency")
    private String receiverCurrency;
 
    /**
     * 开户行名称
     */
    @TableField("bank_name")
    private String bankName;
 
    /**
     * 开户行CODE
     */
    @TableField("bank_code")
    private String bankCode;
 
    /**
     * 开户行省
     */
    @TableField("bank_province")
    private String bankProvince;
 
    /**
     * 开户城市
     */
    @TableField("bank_city")
    private String bankCity;
 
    /**
     * 联行号（12位数字组成） 
     */
    @TableField("payee_bank_lines_no")
    private String payeeBankLinesNo;
 
    /**
     * 上送金额
     */
    @TableField("upload_amount")
    private BigDecimal uploadAmount;
 
    /**
     * 实际转账金额
            必须小于等于上送金额
     */
    @TableField("trans_amount")
    private BigDecimal transAmount;
 
    /**
     * 订单名称
     */
    @TableField("order_name")
    private String orderName;
 
    /**
     * 外部支付流水号 
     */
    @TableField("dms_pay_no")
    private String dmsPayNo;
 
    /**
     * 外部手续费费
     */
    @TableField("poundage")
    private BigDecimal poundage;
 
    /**
     * 转账完成时间
     */
    @TableField("pay_time")
    private String payTime;
 
    /**
     * true:成功；false：失 败，processing：处理中 
     */
    @TableField("success")
    private String success;
 
    /**
     * 失败原因
     */
    @TableField("err_message")
    private String errMessage;
 
    /**
     * Y：已退票；N 未退票 
     */
    @TableField("refund_ticket")
    private String refundTicket;
 
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
        return this.serialNo;
    }
}
