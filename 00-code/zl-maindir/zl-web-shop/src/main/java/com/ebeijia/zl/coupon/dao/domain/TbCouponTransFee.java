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

/**
 *
 * tb_coupon_trans_fee
 *
 * @User J
 * @Date 2019-01-05
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_coupon_trans_fee")
@ApiModel("tb_coupon_trans_fee")
public class TbCouponTransFee extends Model<TbCouponTransFee> {
 
    @TableId(value = "fee_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "fee_id")
    private String feeId;
 
    /**
     * 单位万分之一
     */
    @TableField("fee")
    @ApiModelProperty(value = "单位万分之一")
    private Integer fee;
 
    /**
     * 卡券种类NA
     */
    @TableField("coupon_code")
    @ApiModelProperty(value = "卡券种类NA")
    private String couponCode;
 
    /**
     * BID
     */
    @TableField("b_id")
    @ApiModelProperty(value = "BID")
    private String bId;
 
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
        return this.feeId;
    }
}
