package com.ebeijia.zl.shop.dao.refund.models;

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
 * 退货商品表
 *
 * @User J
 * @Date 2018-12-04
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_sellback_goodslist")
@ApiModel("退货商品表")
public class TbEcomSellbackGoodslist extends Model<TbEcomSellbackGoodslist> {
 
    /**
     * 0：未退货
                        1：已退货
     */
    @TableField("apply_return_state")
    @ApiModelProperty(value = "0：未退货                        1：已退货")
    private String applyReturnState;
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    @TableField("goods_remark")
    @ApiModelProperty(value = "goods_remark")
    private String goodsRemark;
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    @TableField("o_item_id")
    @ApiModelProperty(value = "o_item_id")
    private String oItemId;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    @TableField("return_num")
    @ApiModelProperty(value = "return_num")
    private Integer returnNum;
 
    @TableField("returns_id")
    @ApiModelProperty(value = "returns_id")
    private String returnsId;
 
    /**
     * 对应tb_ecom_order_goods_detail表中的detail_id
     */
    @TableField("s_order_id")
    @ApiModelProperty(value = "对应tb_ecom_order_goods_detail表中的detail_id")
    private String sOrderId;
 
    @TableField("ship_num")
    @ApiModelProperty(value = "ship_num")
    private Integer shipNum;
 
    @TableField("storage_num")
    @ApiModelProperty(value = "storage_num")
    private Integer storageNum;
 
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
