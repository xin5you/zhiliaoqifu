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
 * 订单SKU明细表
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_order_product_item")
@ApiModel("订单SKU明细表")
public class TbEcomOrderProductItem extends Model<TbEcomOrderProductItem> {
 
    @TableId(value = "o_item_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "o_item_id")
    private String oItemId;
 
    @TableField("s_order_id")
    @ApiModelProperty(value = "s_order_id")
    private String sOrderId;
 
    @TableField("product_id")
    @ApiModelProperty(value = "product_id")
    private String productId;
 
    @TableField("cart_id")
    @ApiModelProperty(value = "cart_id")
    private String cartId;
 
    @TableField("product_price")
    @ApiModelProperty(value = "product_price")
    private Integer productPrice;
 
    @TableField("product_num")
    @ApiModelProperty(value = "product_num")
    private Integer productNum;
 
    @TableField("product_name")
    @ApiModelProperty(value = "product_name")
    private String productName;
 
    @TableField("gained_point")
    @ApiModelProperty(value = "gained_point")
    private Integer gainedPoint;
 
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
 
    @TableField("change_product_name")
    @ApiModelProperty(value = "change_product_name")
    private String changeProductName;
 
    @TableField("change_product_id")
    @ApiModelProperty(value = "change_product_id")
    private Integer changeProductId;
 
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
        return this.oItemId;
    }
}
