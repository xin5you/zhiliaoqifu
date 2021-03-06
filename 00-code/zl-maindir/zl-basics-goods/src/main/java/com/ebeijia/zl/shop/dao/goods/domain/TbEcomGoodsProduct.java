package com.ebeijia.zl.shop.dao.goods.domain;

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
 * 货品(SKU)信息表
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_goods_product")
@ApiModel("货品(SKU)信息表")
public class TbEcomGoodsProduct extends Model<TbEcomGoodsProduct> {
 
    /**
     * 目前是同一个spu下的所有sku专用账户是同一个；同一个spu最多建立一个专用账户属性
     */
    @TableField("account_sign")
    @ApiModelProperty(value = "目前是同一个spu下的所有sku专用账户是同一个；同一个spu最多建立一个专用账户属性")
    private String accountSign;
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    /**
     * 状态
     */
    @TableField("data_stat")
    @ApiModelProperty(value = "状态")
    private String dataStat;
 
    /**
     * 商品详情id
     */
    @TableField("detail_id")
    @ApiModelProperty(value = "商品详情id")
    private String detailId;
 
    /**
     * 分销商代码
     */
    @TableField("ecom_code")
    @ApiModelProperty(value = "分销商代码")
    private String ecomCode;
 
    /**
     * 总库存
     */
    @TableField("enable_store")
    @ApiModelProperty(value = "总库存")
    private Integer enableStore;
 
    /**
     * 商品成本价
     */
    @TableField("goods_cost")
    @ApiModelProperty(value = "商品成本价")
    private String goodsCost;
 
    /**
     * 对应商品id
     */
    @TableField("goods_id")
    @ApiModelProperty(value = "对应商品id")
    private String goodsId;
 
    /**
     * 商品价格
     */
    @TableField("goods_price")
    @ApiModelProperty(value = "商品价格")
    private String goodsPrice;
 
    /**
     * 可用库存
     */
    @TableField("is_store")
    @ApiModelProperty(value = "可用库存")
    private Integer isStore;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    /**
     * 页面描述
     */
    @TableField("meta_description")
    @ApiModelProperty(value = "页面描述")
    private String metaDescription;
 
    /**
     * 商品市场价
     */
    @TableField("mkt_price")
    @ApiModelProperty(value = "商品市场价")
    private String mktPrice;
 
    /**
     * 页面标题
     */
    @TableField("page_title")
    @ApiModelProperty(value = "页面标题")
    private String pageTitle;
 
    /**
     * 图片
     */
    @TableField("pic_url")
    @ApiModelProperty(value = "图片")
    private String picUrl;
 
    /**
     * 是否上架
     */
    @TableField("product_enable")
    @ApiModelProperty(value = "是否上架")
    private Integer productEnable;
 
    /**
     * 货品id
     */
    @TableId(value = "product_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "货品id")
    private String productId;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    /**
     * sku代码
     */
    @TableField("sku_code")
    @ApiModelProperty(value = "sku代码")
    private String skuCode;
 
    /**
     * 对应spu代码
     */
    @TableField("spu_code")
    @ApiModelProperty(value = "对应spu代码")
    private String spuCode;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;

    @TableField(exist = false)
    private String isDefault;

    @TableField(exist = false)
    private String productEnableName;

    @TableField(exist = false)
    private String specId;

    @TableField(exist = false)
    private String specValueId;

    @TableField(exist = false)
    private String specValueName;

    @TableField(exist = false)
    private String specValue;

    @Override
    protected Serializable pkVal() { 
        return this.productId;
    }
}
