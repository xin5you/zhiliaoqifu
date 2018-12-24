package com.ebeijia.zl.shop.vo;


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
@ApiModel("SKU值对象")
public class ItemInfo {

    @ApiModelProperty(value = "o_item_id")
    private String oItemId;

    @ApiModelProperty(value = "s_order_id")
    private String sOrderId;

    @ApiModelProperty(value = "product_id")
    private String productId;

    @ApiModelProperty(value = "商品单价")
    private Integer productPrice;

    @ApiModelProperty(value = "商品数量")
    private Integer productNum;

    @ApiModelProperty(value = "商品名")
    private String productName;

    @ApiModelProperty(value = "gained_point")
    private Integer gainedPoint;

    @ApiModelProperty(value = "addon")
    private String addon;

    /**
     * 0：未退货
     1：已退货
     */
    @ApiModelProperty(value = "0：未退货                        1：已退货")
    private String applyReturnState;

    @ApiModelProperty(value = "换货品名")
    private String changeProductName;

    @ApiModelProperty(value = "换货商品ID")
    private Integer changeProductId;

    @ApiModelProperty(value = "data_stat")
    private String dataStat;

    @ApiModelProperty(value = "remarks")
    private String remarks;

    /**
     * 附加数据
     */
    @ApiModelProperty(value = "商品图片")
    private String productPic;

    @ApiModelProperty(value = "专项类型")
    private String billingType;


}
