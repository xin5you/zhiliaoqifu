package com.ebeijia.zl.shop.core.info.domain;

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
 * 购物车表
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_cart")
@ApiModel("购物车表")
public class TbEcomCart extends Model<TbEcomCart> {
 
    /**
     * 购物车主键
     */
    @TableId(value = "cart_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "购物车主键")
    private String cartId;
 
    /**
     * 商品ID
     */
    @TableField("product_id")
    @ApiModelProperty(value = "商品ID")
    private String productId;
 
    /**
     * 渠道代码
     */
    @TableField("ecom_code")
    @ApiModelProperty(value = "渠道代码")
    private String ecomCode;
 
    /**
     * 类型 0：商品，1：捆绑商品，2：赠品。现阶段暂时只有0类型，1和2暂时用不到
     */
    @TableField("product_type")
    @ApiModelProperty(value = "类型 0：商品，1：捆绑商品，2：赠品。现阶段暂时只有0类型，1和2暂时用不到")
    private String productType;
 
    /**
     * 数量
     */
    @TableField("product_num")
    @ApiModelProperty(value = "数量")
    private Integer productNum;
 
    /**
     * 重量
     */
    @TableField("weight")
    @ApiModelProperty(value = "重量")
    private String weight;
 
    @TableField("session_id")
    @ApiModelProperty(value = "session_id")
    private String sessionId;
 
    /**
     * 价格(分)
     */
    @TableField("product_price")
    @ApiModelProperty(value = "价格(分)")
    private Integer productPrice;
 
    /**
     * 优惠价格(分)
     */
    @TableField("preferential_price")
    @ApiModelProperty(value = "优惠价格(分)")
    private Integer preferentialPrice;
 
    /**
     * 会员ID
     */
    @TableField("member_id")
    @ApiModelProperty(value = "会员ID")
    private String memberId;
 
    /**
     * 0：选中，1：不选中
     */
    @TableField("is_check")
    @ApiModelProperty(value = "0：选中，1：不选中")
    private String isCheck;
 
    /**
     * 0：加入购物车
                        1：提交订单
                        2：立即购物
     */
    @TableField("is_change")
    @ApiModelProperty(value = "0：加入购物车                        1：提交订单                        2：立即购物")
    private String isChange;
 
    /**
     * 促销截止时间
     */
    @TableField("activity_end_time")
    @ApiModelProperty(value = "促销截止时间")
    private Long activityEndTime;
 
    /**
     * 促销ID
     */
    @TableField("activity_id")
    @ApiModelProperty(value = "促销ID")
    private String activityId;
 
    /**
     * 促销明细
     */
    @TableField("activity_detail")
    @ApiModelProperty(value = "促销明细")
    private String activityDetail;
 
    /**
     * 状态
     */
    @TableField("data_stat")
    @ApiModelProperty(value = "状态")
    private String dataStat;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;


    @Override
    protected Serializable pkVal() { 
        return this.cartId;
    }
}
