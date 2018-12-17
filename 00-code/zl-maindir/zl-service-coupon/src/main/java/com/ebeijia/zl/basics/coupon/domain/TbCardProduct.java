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
import java.math.BigDecimal;

/**
 *
 * 卡密产品为卡密的大分类
 *
 * @User J
 * @Date 2018-12-13
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_card_product")
@ApiModel("卡密产品为卡密的大分类")
public class TbCardProduct extends Model<TbCardProduct> {
 
    /**
     * 以分为单位
     */
    @TableField("amount")
    @ApiModelProperty(value = "以分为单位")
    private BigDecimal amount;
 
    /**
     * 可购数量
     */
    @TableField("available_num")
    @ApiModelProperty(value = "可购数量")
    private Integer availableNum;
 
    /**
     * 专项类型Id
     */
    @TableField("b_id")
    @ApiModelProperty(value = "专项类型Id")
    private String bId;
 
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
     * LOGO
     */
    @TableField("logo_url")
    @ApiModelProperty(value = "LOGO")
    private String logoUrl;
 
    /**
     * 以M/G/元/月 等为单位
     */
    @TableField("org_amount")
    @ApiModelProperty(value = "以M/G/元/月 等为单位")
    private String orgAmount;
 
    /**
     * 产品号
     */
    @TableId(value = "product_code" ,type = IdType.UUID)
    @ApiModelProperty(value = "产品号")
    private String productCode;
 
    /**
     * 产品描述
     */
    @TableField("product_desc")
    @ApiModelProperty(value = "产品描述")
    private String productDesc;
 
    /**
     * 产品名称
     */
    @TableField("product_name")
    @ApiModelProperty(value = "产品名称")
    private String productName;
 
    /**
     * 产品类型
     */
    @TableField("product_type")
    @ApiModelProperty(value = "产品类型")
    private String productType;
 
    /**
     * 备注
     */
    @TableField("remarks")
    @ApiModelProperty(value = "备注")
    private String remarks;
 
    /**
     * 供应商
     */
    @TableField("supplier")
    @ApiModelProperty(value = "供应商")
    private String supplier;
 
    /**
     * 已发总数
     */
    @TableField("total_num")
    @ApiModelProperty(value = "已发总数")
    private Integer totalNum;
 
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


    @Override
    protected Serializable pkVal() { 
        return this.productCode;
    }
}
