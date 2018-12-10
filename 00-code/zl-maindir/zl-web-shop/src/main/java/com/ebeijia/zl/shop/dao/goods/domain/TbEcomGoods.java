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
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 商品表
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_goods")
@ApiModel("商品表")
public class TbEcomGoods extends Model<TbEcomGoods> {
 
    /**
     * 商品id
     */
    @TableId(value = "goods_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "商品id")
    private String goodsId;
 
    /**
     * 商品名
     */
    @TableField("goods_name")
    @ApiModelProperty(value = "商品名")
    private String goodsName;
 
    /**
     * SPU代码
     */
    @TableField("spu_code")
    @ApiModelProperty(value = "SPU代码")
    private String spuCode;
 
    /**
     * 分销商代码
     */
    @TableField("ecom_code")
    @ApiModelProperty(value = "分销商代码")
    private String ecomCode;
 
    /**
     * 0：实物类，1：服务类
     */
    @TableField("goods_type")
    @ApiModelProperty(value = "0：实物类，1：服务类")
    private String goodsType;
 
    /**
     * 单位
     */
    @TableField("unit")
    @ApiModelProperty(value = "单位")
    private String unit;
 
    /**
     * 重量
     */
    @TableField("weight")
    @ApiModelProperty(value = "重量")
    private BigDecimal weight;
 
    /**
     * 默认sku代码
     */
    @TableField("default_sku_code")
    @ApiModelProperty(value = "默认sku代码")
    private String defaultSkuCode;
 
    /**
     * 0代表已下架，1代表已上架
     */
    @TableField("market_enable")
    @ApiModelProperty(value = "0代表已下架，1代表已上架")
    private String marketEnable;
 
    /**
     * 简介
     */
    @TableField("brief")
    @ApiModelProperty(value = "简介")
    private String brief;
 
    /**
     * 存储商品的富文本描述
     */
    @TableField("goods_detail")
    @ApiModelProperty(value = "存储商品的富文本描述")
    private String goodsDetail;
 
    /**
     * 0代表单个商品，1代表组合商品
     */
    @TableField("have_groups")
    @ApiModelProperty(value = "0代表单个商品，1代表组合商品")
    private String haveGroups;
 
    /**
     * 0代表未开启，1代表已开启
     */
    @TableField("have_params")
    @ApiModelProperty(value = "0代表未开启，1代表已开启")
    private String haveParams;
 
    /**
     * 0代表未开启，
                        1代表已开启
     */
    @TableField("have_spec")
    @ApiModelProperty(value = "0代表未开启，                        1代表已开启")
    private String haveSpec;
 
    /**
     * 0代表未禁用，1代表已禁用
     */
    @TableField("is_disabled")
    @ApiModelProperty(value = "0代表未禁用，1代表已禁用")
    private String isDisabled;
 
    @TableField("ponumber")
    @ApiModelProperty(value = "ponumber")
    private Integer ponumber;
 
    @TableField("goods_sord")
    @ApiModelProperty(value = "goods_sord")
    private Integer goodsSord;
 
    @TableField("goods_weight")
    @ApiModelProperty(value = "goods_weight")
    private String goodsWeight;
 
    @TableField("grade")
    @ApiModelProperty(value = "grade")
    private Integer grade;
 
    /**
     * 0：不是，1：是
     */
    @TableField("is_hot")
    @ApiModelProperty(value = "0：不是，1：是")
    private String isHot;
 
    /**
     * 商品图片
     */
    @TableField("goods_img")
    @ApiModelProperty(value = "商品图片")
    private String goodsImg;
 
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
        return this.goodsId;
    }
}
