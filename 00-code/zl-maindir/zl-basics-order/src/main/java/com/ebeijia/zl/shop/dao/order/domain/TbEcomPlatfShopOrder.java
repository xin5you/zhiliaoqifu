package com.ebeijia.zl.shop.dao.order.domain;

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
 * 渠道子订单表
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_platf_shop_order")
@ApiModel("渠道子订单表")
public class TbEcomPlatfShopOrder extends Model<TbEcomPlatfShopOrder> {
 
    /**
     * 渠道邮费
     */
    @TableField("chnl_order_postage")
    @ApiModelProperty(value = "渠道邮费")
    private Long chnlOrderPostage;
 
    /**
     * 渠道订单金额
     */
    @TableField("chnl_order_price")
    @ApiModelProperty(value = "渠道订单金额")
    private Long chnlOrderPrice;
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    /**
     * 记录与bid关联用，目前直接记录bid
     */
    @TableField("dms_related_key")
    @ApiModelProperty(value = "记录与bid关联用，目前直接记录bid")
    private String dmsRelatedKey;
 
    /**
     * 供应商
     */
    @TableField("ecom_code")
    @ApiModelProperty(value = "供应商")
    private String ecomCode;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    /**
     * 会员ID
     */
    @TableField("member_id")
    @ApiModelProperty(value = "会员ID")
    private String memberId;
 
    /**
     * 主订单ID
     */
    @TableField("order_id")
    @ApiModelProperty(value = "主订单ID")
    private String orderId;
 
    @TableField("order_remark")
    @ApiModelProperty(value = "order_remark")
    private String orderRemark;
 
    /**
     * 单位：分（不含运费）
     */
    @TableField("pay_amt")
    @ApiModelProperty(value = "单位：分（不含运费）")
    private Long payAmt;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    /**
     * 子订单ID
     */
    @TableId(value = "s_order_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "子订单ID")
    private String sOrderId;
 
    /**
     * 单位：分
     */
    @TableField("shipping_freight_price")
    @ApiModelProperty(value = "单位：分")
    private Long shippingFreightPrice;
 
    /**
     * 00：初始状态
                        10：待发货
                        11：待出库
                        12：已发货
                        13：已收货
                        14：已完成
                        15：作废
                        20：申请换货
                        21：已换货
                        22：申请退货
                        23：已退货
                        24：换货被拒绝
                        25：退货被拒绝
                        26：申请取消
                        27：已取消
                        28：取消被拒
                        44：外部渠道发货失败
     */
    @TableField("sub_order_status")
    @ApiModelProperty(value = "00：初始状态                        10：待发货                        11：待出库                        12：已发货                        13：已收货                        14：已完成                        15：作废                        20：申请换货                        21：已换货                        22：申请退货                        23：已退货                        24：换货被拒绝                        25：退货被拒绝                        26：申请取消                        27：已取消                        28：取消被拒                        44：外部渠道发货失败")
    private String subOrderStatus;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;

    @TableField(exist = false)
    private String mobilePhoneNo;

    @TableField(exist = false)
    private String personalName;

    /**
     * 0：未付款
     * 1：已付款待确认
     * 2：已付款
     * 3：已退款
     * 4：部分退款
     * 5：部分付款
     * 8：已取消
     * 9：已完成
     */
    @TableField(exist = false)
    private String payStatus;

    @TableField(exist = false)
    private Long payTime;

    @TableField(exist = false)
    private String subOrderStatusName;

    @TableField(exist = false)
    private String goodsType;

    @Override
    protected Serializable pkVal() { 
        return this.sOrderId;
    }
}
