package com.ebeijia.zhiliao.shop.refund.models;

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
 * 退换货申请表
 *
 * @User J
 * @Date 2018-12-04
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_returns_order")
@ApiModel("退换货申请表")
public class TbEcomReturnsOrder extends Model<TbEcomReturnsOrder> {
 
    /**
     * 提交日期
     */
    @TableField("add_time")
    @ApiModelProperty(value = "提交日期")
    private String addTime;
 
    @TableField("apply_reason")
    @ApiModelProperty(value = "apply_reason")
    private String applyReason;
 
    /**
     * 1:无理由
                        2:质量问题
     */
    @TableField("apply_reason_type")
    @ApiModelProperty(value = "1:无理由                        2:质量问题")
    private String applyReasonType;
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    @TableField("member_id")
    @ApiModelProperty(value = "member_id")
    private String memberId;
 
    @TableField("photo_img")
    @ApiModelProperty(value = "photo_img")
    private String photoImg;
 
    /**
     * 客服拒绝理由
     */
    @TableField("refuse_reason")
    @ApiModelProperty(value = "客服拒绝理由")
    private String refuseReason;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    @TableId(value = "returns_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "returns_id")
    private String returnsId;
 
    /**
     * 0：未处理，
                        1：已拒绝，
                        2：已同意，
                        3：已删除，
                        9：已完成
     */
    @TableField("returns_status")
    @ApiModelProperty(value = "0：未处理，                        1：已拒绝，                        2：已同意，                        3：已删除，                        9：已完成")
    private String returnsStatus;
 
    /**
     * 1：退货，
                        2：渠道取消
                        3：用户申请取消
                        4：换货
                        5：退款
                        6：平台取消
     */
    @TableField("returns_type")
    @ApiModelProperty(value = "1：退货，                        2：渠道取消                        3：用户申请取消                        4：换货                        5：退款                        6：平台取消")
    private String returnsType;
 
    @TableField("s_order_id")
    @ApiModelProperty(value = "s_order_id")
    private String sOrderId;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;


    @Override
    protected Serializable pkVal() { 
        return this.returnsId;
    }
}
