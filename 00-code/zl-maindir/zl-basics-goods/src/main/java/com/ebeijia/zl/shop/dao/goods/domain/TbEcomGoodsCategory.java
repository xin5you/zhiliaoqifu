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
 * 商品分类表
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_goods_category")
@ApiModel("商品分类表")
public class TbEcomGoodsCategory extends Model<TbEcomGoodsCategory> {
 
    /**
     * 分类id
     */
    @TableId(value = "cat_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "分类id")
    private String catId;
 
    /**
     * 分类图像
     */
    @TableField("cat_image")
    @ApiModelProperty(value = "分类图像")
    private String catImage;
 
    /**
     * 分类名
     */
    @TableField("cat_name")
    @ApiModelProperty(value = "分类名")
    private String catName;
 
    /**
     * 此字段是以&#34;|&#34;分隔cat_id组成的字符串，如：0|12|25|
     */
    @TableField("cat_path")
    @ApiModelProperty(value = "此字段是以&#34;|&#34;分隔cat_id组成的字符串，如：0|12|25|")
    private String catPath;
 
    /**
     * 数值越小，排序越靠前
     */
    @TableField("cat_solr")
    @ApiModelProperty(value = "数值越小，排序越靠前")
    private Integer catSolr;
 
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
     * 分销商代码
     */
    @TableField("ecom_code")
    @ApiModelProperty(value = "分销商代码")
    private String ecomCode;
 
    /**
     * 0：不显示，1：普通商品。2：待定
     */
    @TableField("list_show")
    @ApiModelProperty(value = "0：不显示，1：普通商品。2：待定")
    private String listShow;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    /**
     * 外分类图像
     */
    @TableField("outer_cat_id")
    @ApiModelProperty(value = "外分类图像")
    private String outerCatId;
 
    /**
     * 父类id
     */
    @TableField("parent_id")
    @ApiModelProperty(value = "父类id")
    private String parentId;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;


    @Override
    protected Serializable pkVal() { 
        return this.catId;
    }
}
