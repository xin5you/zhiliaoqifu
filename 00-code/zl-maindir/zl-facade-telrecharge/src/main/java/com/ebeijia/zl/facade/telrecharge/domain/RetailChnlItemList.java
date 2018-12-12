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
 * 分销商产品关联供应商商品表
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_retail_chnl_item_list")
public class RetailChnlItemList extends Model<RetailChnlItemList> {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -2361263548174886060L;

	/**
     * chnl_item_id
     */
    @TableId(value = "chnl_item_id" ,type = IdType.UUID)
    private String chnlItemId;
 
    /**
     * product_id
     */
    @TableField("product_id")
    private String productId;
 
    /**
     * area_id
     */
    @TableField("area_id")
    private String areaId;
 
    /**
     * 分销商折扣率
     */
    @TableField("channel_rate")
    private BigDecimal channelRate;
 
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
    
    
    /**
     * 业务数据================
     */
    @TableField(exist=false)
	private String areaName;	//区域名称
    
    @TableField(exist=false)
	private String operName;	//运营商名称
	
    @TableField(exist=false)
	private String operId;	//运营商标识
	
    @TableField(exist=false)
	private String channelName; //供应商名称


    @Override
    protected Serializable pkVal() { 
        return this.chnlItemId;
    }
}
