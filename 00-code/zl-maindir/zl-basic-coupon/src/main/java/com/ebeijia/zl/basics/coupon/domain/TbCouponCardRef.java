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

/**
 *
 * 关联卡密和代金券
 *
 * @User J
 * @Date 2018-12-13
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_coupon_card_ref")
@ApiModel("关联卡密和代金券")
public class TbCouponCardRef extends Model<TbCouponCardRef> {
 
    /**
     * 卡密id
     */
    @TableField("card_id")
    @ApiModelProperty(value = "卡密id")
    private String cardId;
 
    /**
     * 代金券id
     */
    @TableField("coupon_id")
    @ApiModelProperty(value = "代金券id")
    private String couponId;
 
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
     * 说明
     */
    @TableField("description")
    @ApiModelProperty(value = "说明")
    private String description;
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private Integer id;
 
    /**
     * 乐观锁版本
     */
    @TableField("lock_version")
    @ApiModelProperty(value = "乐观锁版本")
    private Integer lockVersion;
 
    /**
     * 转换订单id
     */
    @TableField("order_id")
    @ApiModelProperty(value = "转换订单id")
    private String orderId;
 
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
        return this.id;
    }
}
