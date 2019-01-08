package com.ebeijia.zl.coupon.dao.domain;

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
 * @Date 2019-01-05
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_coupon_product")
@ApiModel("卡密产品为卡密的大分类")
public class TbCouponProduct extends Model<TbCouponProduct> {


    /**
     * FEE
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "折损率")
    private BigDecimal fee;

    /**
     * 产品号
     */
    @TableId(value = "coupon_code" ,type = IdType.UUID)
    @ApiModelProperty(value = "产品号")
    private String couponCode;
 
    /**
     * 专项类型Id
     */
    @TableField("b_id")
    @ApiModelProperty(value = "专项类型Id")
    private String bId;
 
    @TableField("price")
    @ApiModelProperty(value = "price")
    private Long price;
 
    /**
     * 产品名称
     */
    @TableField("coupon_name")
    @ApiModelProperty(value = "产品名称")
    private String couponName;
 
    /**
     * 产品类型
     */
    @TableField("coupon_type")
    @ApiModelProperty(value = "产品类型")
    private String couponType;
 
    /**
     * 产品描述
     */
    @TableField("coupon_desc")
    @ApiModelProperty(value = "产品描述")
    private String couponDesc;
 
    /**
     * LOGO
     */
    @TableField("icon_image")
    @ApiModelProperty(value = "LOGO")
    private String iconImage;
 
    /**
     * 以M/G/元/月 等为单位
     */
    @TableField("tag_unit")
    @ApiModelProperty(value = "以M/G/元/月 等为单位")
    private String tagUnit;
 
    /**
     * 标价,以分为单位
     */
    @TableField("tag_amount")
    @ApiModelProperty(value = "标价,以分为单位")
    private Long tagAmount;
 
    /**
     * 已发总数
     */
    @TableField("total_num")
    @ApiModelProperty(value = "已发总数")
    private Integer totalNum;
 
    /**
     * 可购数量
     */
    @TableField("available_num")
    @ApiModelProperty(value = "可购数量")
    private Integer availableNum;
 
    /**
     * 数据状态
     */
    @TableField("data_stat")
    @ApiModelProperty(value = "数据状态")
    private String dataStat;
 
    /**
     * 备注
     */
    @TableField("remarks")
    @ApiModelProperty(value = "备注")
    private String remarks;
 
    /**
     * 创建人
     */
    @TableField("create_user")
    @ApiModelProperty(value = "创建人")
    private String createUser;
 
    /**
     * 更新人
     */
    @TableField("update_user")
    @ApiModelProperty(value = "更新人")
    private String updateUser;
 
    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
 
    /**
     * 更新时间
     */
    @TableField("update_time")
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
 
    /**
     * 乐观锁版本
     */
    @TableField("lock_version")
    @ApiModelProperty(value = "乐观锁版本")
    private Integer lockVersion;


    @Override
    protected Serializable pkVal() { 
        return this.couponCode;
    }
}
