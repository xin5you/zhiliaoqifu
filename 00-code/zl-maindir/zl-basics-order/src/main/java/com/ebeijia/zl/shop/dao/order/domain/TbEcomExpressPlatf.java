package com.ebeijia.zl.shop.dao.order.domain;

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
 * 常规物流信息表
 *
 * @User J
 * @Date 2018-12-29
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_express_platf")
@ApiModel("常规物流信息表")
public class TbEcomExpressPlatf extends Model<TbEcomExpressPlatf> {
 
    /**
     * 递送开始时间
     */
    @TableField("delivery_time")
    @ApiModelProperty(value = "递送开始时间")
    private Long deliveryTime;
 
    /**
     * 分销商代码
     */
    @TableField("ecom_code")
    @ApiModelProperty(value = "分销商代码")
    private String ecomCode;
 
    /**
     * 快递公司id
     */
    @TableField("express_company_id")
    @ApiModelProperty(value = "快递公司id")
    private String expressCompanyId;
 
    /**
     * 快递公司名
     */
    @TableField("express_company_name")
    @ApiModelProperty(value = "快递公司名")
    private String expressCompanyName;
 
    /**
     * 物流编号
     */
    @TableField("express_no")
    @ApiModelProperty(value = "物流编号")
    private String expressNo;
 
    /**
     * 0:未签收，1：已经签收
     */
    @TableField("is_sign")
    @ApiModelProperty(value = "0:未签收，1：已经签收")
    private String isSign;
 
    /**
     * 物流id
     */
    @TableId(value = "pack_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "物流id")
    private String packId;
 
    /**
     * 包裹号
     */
    @TableField("package_no")
    @ApiModelProperty(value = "包裹号")
    private String packageNo;
 
    /**
     * 00:待发货
                        10:已出库
                        20;已签收
                        90:已完成
     */
    @TableField("package_stat")
    @ApiModelProperty(value = "00:待发货                        10:已出库                        20;已签收                        90:已完成")
    private String packageStat;
 
    /**
     * 明细
     */
    @TableField("package_stat_desc")
    @ApiModelProperty(value = "明细")
    private String packageStatDesc;
 
    /**
     * 子订单id
     */
    @TableField("s_order_id")
    @ApiModelProperty(value = "子订单id")
    private String sOrderId;
 
    /**
     * 签收时间
     */
    @TableField("sign_time")
    @ApiModelProperty(value = "签收时间")
    private Long signTime;


    @Override
    protected Serializable pkVal() { 
        return this.packId;
    }
}
