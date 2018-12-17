package com.ebeijia.zl.basics.coupon.domain;

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

/**
 *
 * 包含卡密消费、转卖交易
 *
 * @User J
 * @Date 2018-12-13
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_coupon_trans_log")
@ApiModel("包含卡密消费、转卖交易")
public class TbCouponTransLog extends Model<TbCouponTransLog> {
 
    /**
     * 附加信息
     */
    @TableField("additional_info")
    @ApiModelProperty(value = "附加信息")
    private String additionalInfo;
 
    /**
     * 卡密
     */
    @TableField("card_key")
    @ApiModelProperty(value = "卡密")
    private String cardKey;
 
    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
 
    /**
     * 创建人
     */
    @TableField("create_user")
    @ApiModelProperty(value = "创建人")
    private String createUser;
 
    /**
     * 数据状态
     */
    @TableField("data_stat")
    @ApiModelProperty(value = "数据状态")
    private String dataStat;
 
    /**
     * 乐观锁版本
     */
    @TableField("lock_version")
    @ApiModelProperty(value = "乐观锁版本")
    private Integer lockVersion;
 
    /**
     * 订单号
     */
    @TableField("order_id")
    @ApiModelProperty(value = "订单号")
    private String orderId;
 
    /**
     * 原交易金额
     */
    @TableField("org_trans_amt")
    @ApiModelProperty(value = "原交易金额")
    private String orgTransAmt;
 
    /**
     * 产品号
     */
    @TableField("product_code")
    @ApiModelProperty(value = "产品号")
    private String productCode;
 
    /**
     * 备注
     */
    @TableField("remarks")
    @ApiModelProperty(value = "备注")
    private String remarks;
 
    /**
     * 转入账户
     */
    @TableField("tfr_in_acct_no")
    @ApiModelProperty(value = "转入账户")
    private String tfrInAcctNo;
 
    /**
     * 转出账户
     */
    @TableField("tfr_out_acct_no")
    @ApiModelProperty(value = "转出账户")
    private String tfrOutAcctNo;
 
    /**
     * 实际交易金额
     */
    @TableField("trans_amt")
    @ApiModelProperty(value = "实际交易金额")
    private String transAmt;
 
    /**
     * 手续费
     */
    @TableField("trans_fee")
    @ApiModelProperty(value = "手续费")
    private String transFee;
 
    /**
     * 手续费类型
     */
    @TableField("trans_fee_type")
    @ApiModelProperty(value = "手续费类型")
    private String transFeeType;
 
    /**
     * W10：购买
            W20：充值
            W30：转卖
     */
    @TableField("trans_id")
    @ApiModelProperty(value = "W10：购买            W20：充值            W30：转卖")
    private String transId;
 
    /**
     * 00：交易成功
            99：交易失败
     */
    @TableField("trans_result")
    @ApiModelProperty(value = "00：交易成功            99：交易失败")
    private String transResult;
 
    /**
     * 交易流水号
     */
    @TableId(value = "txn_primary_key" ,type = IdType.UUID)
    @ApiModelProperty(value = "交易流水号")
    private String txnPrimaryKey;
 
    /**
     * 更新时间
     */
    @TableField("update_time")
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
 
    /**
     * 更新人
     */
    @TableField("update_user")
    @ApiModelProperty(value = "更新人")
    private String updateUser;
 
    /**
     * 用户名
     */
    @TableField("user_name")
    @ApiModelProperty(value = "用户名")
    private String userName;


    @Override
    protected Serializable pkVal() { 
        return this.txnPrimaryKey;
    }
}
