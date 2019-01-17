package com.ebeijia.zl.shop.dao.info.domain;

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
 * tb_ecom_itx_log_detail
 *
 * @User J
 * @Date 2019-01-10
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_itx_log_detail")
@ApiModel("tb_ecom_itx_log_detail")
public class TbEcomItxLogDetail extends Model<TbEcomItxLogDetail> {
 
    @TableId(value = "itx_key" ,type = IdType.UUID)
    @ApiModelProperty(value = "itx_key")
    private String itxKey;
 
    @TableField("img")
    @ApiModelProperty(value = "img")
    private String img;
 
    @TableField("title")
    @ApiModelProperty(value = "title")
    private String title;
 
    @TableField("descinfo")
    @ApiModelProperty(value = "descinfo")
    private String descinfo;
 
    @TableField("price")
    @ApiModelProperty(value = "price")
    private Long price;
 
    @TableField("amount")
    @ApiModelProperty(value = "amount")
    private Integer amount;

    @TableField("out_id")
    @ApiModelProperty(value = "out_id")
    private String outId;

    @TableField("source_bid")
    @ApiModelProperty(value = "source_bid")
    private String sourceBid;

    @TableField("member_id")
    @ApiModelProperty(value = "会员ID")
    private String memberId;

    @Override
    protected Serializable pkVal() { 
        return this.itxKey;
    }
}
