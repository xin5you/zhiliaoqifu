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
 * 卡券回收订单表
 * @User xiaomei
 * @Date 2019-02-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_chnl_coupon_order")
@ApiModel("卡券回收订单表")
public class TbChnlCouponOrder extends Model<TbChnlCouponOrder> {

    /**
     * 订单ID
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;

    /**
     * 分销商ID
     */
    @TableId(value = "chnl_id")
    @ApiModelProperty(value = "分销商ID")
    private String chnlId;

    /**
     * 卡券类型
     */
    @TableId(value = "coupon_bid")
    @ApiModelProperty(value = "卡券类型")
    private String couponBid;

    /**
     * 代金券总面额
     */
    @TableField("coupon_amt")
    @ApiModelProperty(value = "代金券总面额")
    private BigDecimal couponAmt;

    /**
     * 支出总金额
     */
    @TableField("total_amt")
    @ApiModelProperty(value = "totalAmt")
    private BigDecimal totalAmt;

    /**
     * 回收代金券总张数
     */
    @TableField("total_num")
    @ApiModelProperty(value = "回收代金券总张数")
    private Integer totalNum;

    /**
     * 分销商回收账户
     */
    @TableField("chnl_bid")
    @ApiModelProperty(value = "分销商回收账户")
    private String chnlBid;

    /**
     * 交易状态
     */
    @TableField("trans_stat")
    @ApiModelProperty(value = "交易状态")
    private String transStat;

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
        return this.id;
    }
}
