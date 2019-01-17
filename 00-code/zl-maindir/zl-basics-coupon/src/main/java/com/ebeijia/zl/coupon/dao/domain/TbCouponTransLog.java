package com.ebeijia.zl.coupon.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 包含卡密消费、转卖交易
 *
 * @User J
 * @Date 2019-01-05
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_coupon_trans_log")
@ApiModel("包含卡密消费、转卖交易")
public class TbCouponTransLog extends Model<TbCouponTransLog> {
 
    /**
     * 交易流水号
     */
    @TableId(value = "coupon_txn_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "交易流水号")
    private String couponTxnId;
 
    /**
     * 订单号NA
     */
    @TableField("order_id")
    @ApiModelProperty(value = "订单号NA")
    private String orderId;
 
    /**
     * W10：购买\\r\\n            W20：充值\\r\\n            W30：转卖
     */
    @TableField("trans_type")
    @ApiModelProperty(value = "W10：购买\\r\\n            W20：充值\\r\\n            W30：转卖")
    private String transType;
 
    /**
     * 用户ID
     */
    @TableField("member_id")
    @ApiModelProperty(value = "用户ID")
    private String memberId;
 
    /**
     * 产品号
     */
    @TableField("coupon_code")
    @ApiModelProperty(value = "产品号")
    private String couponCode;
 
    /**
     * 交易数量
     */
    @TableField("coupon_amt")
    @ApiModelProperty(value = "交易数量")
    private Integer couponAmt;
 
    /**
     * 转入账户NA
     */
    @TableField("tfr_in_acct_no")
    @ApiModelProperty(value = "转入账户NA")
    private String tfrInAcctNo;
 
    /**
     * 转出账户NA
     */
    @TableField("tfr_out_acct_no")
    @ApiModelProperty(value = "转出账户NA")
    private String tfrOutAcctNo;
 
    /**
     * 实际交易金额
     */
    @TableField("trans_amt")
    @ApiModelProperty(value = "实际交易金额")
    private BigDecimal transAmt;
 
    /**
     * 原交易金额
     */
    @TableField("org_trans_amt")
    @ApiModelProperty(value = "原交易金额")
    private BigDecimal orgTransAmt;
 
    /**
     * 手续费
     */
    @TableField("trans_fee")
    @ApiModelProperty(value = "手续费")
    private BigDecimal transFee;
 
    /**
     * 手续费类型NA
     */
    @TableField("trans_fee_type")
    @ApiModelProperty(value = "手续费类型NA")
    private String transFeeType;
 
    /**
     * 00：交易成功\r\n            99：交易失败
     */
    @TableField("trans_result")
    @ApiModelProperty(value = "00：交易成功\r\n            99：交易失败")
    private String transResult;
 
    /**
     * 附加信息
     */
    @TableField("additional_info")
    @ApiModelProperty(value = "附加信息")
    private String additionalInfo;
 
    /**
     * 数据状态
     */
    @TableField("data_stat")
    @ApiModelProperty(value = "数据状态")
    private String dataStat;
 
    /**
     * 备注
     */
    @TableField("remarks")
    @ApiModelProperty(value = "备注")
    private String remarks;
 
    /**
     * 创建人
     */
    @TableField("create_user")
    @ApiModelProperty(value = "创建人")
    private String createUser;
 
    /**
     * 更新人
     */
    @TableField("update_user")
    @ApiModelProperty(value = "更新人")
    private String updateUser;
 
    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
 
    /**
     * 更新时间
     */
    @TableField("update_time")
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
 
    /**
     * 乐观锁版本
     */
    @TableField("lock_version")
    @ApiModelProperty(value = "乐观锁版本")
    private Integer lockVersion;

    @TableField(exist = false)
    private String memberName;
    @TableField(exist = false)
    private String couponName;
    @TableField(exist = false)
    private String serviceFree;


    @Override
    protected Serializable pkVal() { 
        return this.couponTxnId;
    }
}
