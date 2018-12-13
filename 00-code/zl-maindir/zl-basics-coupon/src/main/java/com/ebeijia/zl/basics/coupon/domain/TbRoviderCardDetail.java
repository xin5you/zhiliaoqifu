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
 * 供应商代金券权益追加信息
 *
 * @User J
 * @Date 2018-12-13
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_rovider_card_detail")
@ApiModel("供应商代金券权益追加信息")
public class TbRoviderCardDetail extends Model<TbRoviderCardDetail> {
 
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
     * 权益记录主键
     */
    @TableId(value = "detail_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "权益记录主键")
    private String detailId;
 
    /**
     * 乐观锁版本
     */
    @TableField("lock_version")
    @ApiModelProperty(value = "乐观锁版本")
    private Integer lockVersion;
 
    /**
     * 产品号
     */
    @TableField("product_code")
    @ApiModelProperty(value = "产品号")
    private String productCode;
 
    /**
     * provider_id
     */
    @TableField("provider_id")
    @ApiModelProperty(value = "provider_id")
    private String providerId;
 
    /**
     * 单位分
     */
    @TableField("quota_total")
    @ApiModelProperty(value = "单位分")
    private Integer quotaTotal;
 
    /**
     * 备注
     */
    @TableField("remarks")
    @ApiModelProperty(value = "备注")
    private String remarks;
 
    /**
     * 修改时间
     */
    @TableField("update_time")
    @ApiModelProperty(value = "修改时间")
    private Long updateTime;
 
    /**
     * 修改人
     */
    @TableField("update_user")
    @ApiModelProperty(value = "修改人")
    private String updateUser;


    @Override
    protected Serializable pkVal() { 
        return this.detailId;
    }
}
