package com.ebeijia.zl.facade.telrecharge.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * 分销商信息表
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_retail_chnl_inf")
public class RetailChnlInf extends Model<RetailChnlInf> {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -7757246963885895726L;

	/**
     * 分销商ID
     */
    @TableId(value = "channel_id" ,type = IdType.UUID)
    private String channelId;
 
    /**
     * 分销商名称
     */
    @TableField("channel_name")
    private String channelName;
 
    /**
     * 分销商编号
     */
    @TableField("channel_code")
    private String channelCode;
 
    /**
     * 分销商KEY
     */
    @TableField("channel_key")
    private String channelKey;
 
    /**
     * 分销商备付金额元
     */
    @TableField("channel_reserve_amt")
    private BigDecimal channelReserveAmt;
 
    /**
     * 分销商预警金额
     */
    @TableField("channel_prewarning_amt")
    private BigDecimal channelPrewarningAmt;
 
    /**
     * 管理员手机号
     */
    @TableField("phone_no")
    private String phoneNo;
 
    /**
     * 0：否 1：是
     */
    @TableField("is_open")
    private String isOpen;
 
    /**
     * 邮箱
     */
    @TableField("email")
    private String email;
 
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
    @Version
    @TableField("lock_version")
    private Integer lockVersion;


    @Override
    protected Serializable pkVal() { 
        return this.channelId;
    }
}
