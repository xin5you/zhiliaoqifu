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
 * 订单SKU明细表
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_order_product_item")
@ApiModel("订单SKU明细表")
public class TbEcomOrderProductItem extends Model<TbEcomOrderProductItem> {
 
    @TableField("addon")
    @ApiModelProperty(value = "addon")
    private String addon;
 
    /**
     * 0：未退货
                        1：已退货
     */
    @TableField("apply_return_state")
    @ApiModelProperty(value = "0：未退货                        1：已退货")
    private String applyReturnState;
 
    /**
     * 购物车ID
     */
    @TableField("cart_id")
    @ApiModelProperty(value = "购物车ID")
    private String cartId;
 
    @TableField("change_product_id")
    @ApiModelProperty(value = "change_product_id")
    private Integer changeProductId;
 
    @TableField("change_product_name")
    @ApiModelProperty(value = "change_product_name")
    private String changeProductName;
 
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
     * 积分
     */
    @TableField("gained_point")
    @ApiModelProperty(value = "积分")
    private Integer gainedPoint;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    /**
     * 主键ID
     */
    @TableId(value = "o_item_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "主键ID")
    private String oItemId;
 
    /**
     * SKU ID
     */
    @TableField("product_id")
    @ApiModelProperty(value = "SKU ID")
    private String productId;
 
    /**
     * 名字
     */
    @TableField("product_name")
    @ApiModelProperty(value = "名字")
    private String productName;
 
    /**
     * 个数
     */
    @TableField("product_num")
    @ApiModelProperty(value = "个数")
    private Integer productNum;
 
    /**
     * 价格
     */
    @TableField("product_price")
    @ApiModelProperty(value = "价格")
    private Integer productPrice;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    /**
     * Shop渠道订单ID
     */
    @TableField("s_order_id")
    @ApiModelProperty(value = "Shop渠道订单ID")
    private String sOrderId;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;


    @Override
    protected Serializable pkVal() { 
        return this.oItemId;
    }
}
