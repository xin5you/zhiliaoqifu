package com.ebeijia.zl.facade.telrecharge.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * 分销商充值产品管理表
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_retail_chnl_product_inf")
public class RetailChnlProductInf extends Model<RetailChnlProductInf> {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -6715659580603168763L;

	/**
     * 产品ID
     */
    @TableId(value = "product_id" ,type = IdType.UUID)
    private String productId;
 
    /**
     * 运营商名称
     */
    @TableField("oper_name")
    private String operName;
 
    /**
     * 1:移动
            2.:联通
            3:电信
     */
    @TableField("oper_id")
    private String operId;
 
    /**
     * 0:区分
            1:不区分
     */
    @TableField("area_flag")
    private String areaFlag;
 
    /**
     * 产品面额
     */
    @TableField("product_amt")
    private BigDecimal productAmt;
 
    /**
     * 产品售价单位元
     */
    @TableField("product_price")
    private BigDecimal productPrice;
 
    /**
     * 1:话费
            2:流量
            3:其他
     */
    @TableField("product_type")
    private String productType;
 
    /**
     * RESV1
     */
    @TableField("resv1")
    private String resv1;
 
    /**
     * RESV2
     */
    @TableField("resv2")
    private String resv2;
 
    /**
     * RESV3
     */
    @TableField("resv3")
    private String resv3;
 
    /**
     * RESV4
     */
    @TableField("resv4")
    private String resv4;
 
    /**
     * RESV5
     */
    @TableField("resv5")
    private String resv5;
 
    /**
     * RESV6
     */
    @TableField("resv6")
    private String resv6;
 
    /**
     * 数据状态
     */
    @TableField("data_stat")
    private String dataStat;
 
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
 
    /**
     * 创建人
     */
    @TableField("create_user")
    private String createUser;
 
    /**
     * 修改人
     */
    @TableField("update_user")
    private String updateUser;
 
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;
 
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Long updateTime;
 
    /**
     * 乐观锁版本
     */
    @TableField("lock_version")
    private Integer lockVersion;
    
    
    /*****
     * 业务数据
     */
    @TableField(exist=false)
 	private String chnlItemId;	//明细
    
    @TableField(exist=false)
	private BigDecimal channelRate; //分销商折扣率
    
    @TableField(exist=false)
	private String areaId;	//区域id
    
    @TableField(exist=false)
	private String channelId;	//分销商id

    
    @TableField(exist=false)
	private String channel_name; //分销商名称
    
    @Override
    protected Serializable pkVal() { 
        return this.productId;
    }
}
