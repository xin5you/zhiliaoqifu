package com.ebeijia.zl.shop.core.refund.models;

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
 * 退货物流信息表
 *
 * @User J
 * @Date 2018-12-04
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_express_confirm")
@ApiModel("退货物流信息表")
public class TbEcomExpressConfirm extends Model<TbEcomExpressConfirm> {
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    @TableField("returns_id")
    @ApiModelProperty(value = "returns_id")
    private String returnsId;
 
    /**
     * 10:已发货
                        20:已签收
     */
    @TableField("returns_state")
    @ApiModelProperty(value = "10:已发货                        20:已签收")
    private String returnsState;
 
    @TableField("tracking_company")
    @ApiModelProperty(value = "tracking_company")
    private String trackingCompany;
 
    @TableField("tracking_num")
    @ApiModelProperty(value = "tracking_num")
    private String trackingNum;
 
    @TableField("tracking_time")
    @ApiModelProperty(value = "tracking_time")
    private Long trackingTime;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
