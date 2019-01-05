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
 * 商品详情表
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_goods_detail")
@ApiModel("商品详情表")
public class TbEcomGoodsDetail extends Model<TbEcomGoodsDetail> {
 
    /**
     * 购买量
     */
    @TableField("buy_count")
    @ApiModelProperty(value = "购买量")
    private Integer buyCount;
 
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
     * 商品详情id
     */
    @TableId(value = "detail_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "商品详情id")
    private String detailId;
 
    /**
     * 商品id
     */
    @TableField("goods_id")
    @ApiModelProperty(value = "商品id")
    private String goodsId;
 
    /**
     * 简介
     */
    @TableField("intro")
    @ApiModelProperty(value = "简介")
    private String intro;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    /**
     * 参数
     */
    @TableField("params")
    @ApiModelProperty(value = "参数")
    private String params;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    /**
     * 规格
     */
    @TableField("specs")
    @ApiModelProperty(value = "规格")
    private String specs;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;
 
    /**
     * 浏览量
     */
    @TableField("view_count")
    @ApiModelProperty(value = "浏览量")
    private Integer viewCount;

    @TableField("detail_name")
    @ApiModelProperty(value = "商品详情名称")
    private String detailName;

    @Override
    protected Serializable pkVal() { 
        return this.detailId;
    }
}
