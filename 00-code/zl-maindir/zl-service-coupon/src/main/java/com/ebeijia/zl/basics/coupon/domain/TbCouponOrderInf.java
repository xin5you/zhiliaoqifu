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
 * 卡密交易订单表
 *
 * @User J
 * @Date 2018-12-13
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_coupon_order_inf")
@ApiModel("卡密交易订单表")
public class TbCouponOrderInf extends Model<TbCouponOrderInf> {
 
    /**
     * 订单金额
     */
    @TableField("amount")
    @ApiModelProperty(value = "订单金额")
    private String amount;
 
    /**
     * 银行卡号
     */
    @TableField("bank_no")
    @ApiModelProperty(value = "银行卡号")
    private String bankNo;
 
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
     * 订单数量
     */
    @TableField("num")
    @ApiModelProperty(value = "订单数量")
    private Integer num;
 
    /**
     * 订单号
     */
    @TableId(value = "order_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "订单号")
    private String orderId;
 
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
     * 10：购卡中
            11：购卡失败
            12：已购卡
            20：充值中
            21：充值失败
            22：已充值
            30：转卖中
            31：转卖失败
            32：已转卖
     */
    @TableField("stat")
    @ApiModelProperty(value = "10：购卡中            11：购卡失败            12：已购卡            20：充值中            21：充值失败            22：已充值            30：转卖中            31：转卖失败            32：已转卖")
    private String stat;
 
    /**
     * 10：购卡
            20：充值
            30：转卖
     */
    @TableField("type")
    @ApiModelProperty(value = "10：购卡            20：充值            30：转卖")
    private String type;
 
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
     * 用户ID
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户ID")
    private String userId;


    @Override
    protected Serializable pkVal() { 
        return this.orderId;
    }
}
