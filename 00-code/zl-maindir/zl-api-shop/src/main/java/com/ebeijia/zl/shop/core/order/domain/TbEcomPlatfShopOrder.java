package com.ebeijia.zl.shop.core.order.domain;

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
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_platf_shop_order")
@ApiModel("渠道子订单表")
public class TbEcomPlatfShopOrder extends Model<TbEcomPlatfShopOrder> {
 
    @TableId(value = "s_order_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "s_order_id")
    private String sOrderId;
 
    @TableField("order_id")
    @ApiModelProperty(value = "order_id")
    private String orderId;
 
    @TableField("member_id")
    @ApiModelProperty(value = "member_id")
    private String memberId;
 
    @TableField("ecom_code")
    @ApiModelProperty(value = "ecom_code")
    private String ecomCode;
 
    @TableField("dms_related_key")
    @ApiModelProperty(value = "dms_related_key")
    private String dmsRelatedKey;
 
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
 
    @TableField("order_remark")
    @ApiModelProperty(value = "order_remark")
    private String orderRemark;
 
    /**
     * 单位：分（不含运费）
     */
    @TableField("pay_amt")
    @ApiModelProperty(value = "单位：分（不含运费）")
    private Integer payAmt;
 
    /**
     * 单位：分
     */
    @TableField("shipping_freight_price")
    @ApiModelProperty(value = "单位：分")
    private Integer shippingFreightPrice;
 
    @TableField("chnl_order_price")
    @ApiModelProperty(value = "chnl_order_price")
    private Integer chnlOrderPrice;
 
    @TableField("chnl_order_postage")
    @ApiModelProperty(value = "chnl_order_postage")
    private Integer chnlOrderPostage;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;


    @Override
    protected Serializable pkVal() { 
        return this.sOrderId;
    }
}
