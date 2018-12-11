package com.cn.thinkx.wecard.facade.telrecharge.domain;

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
 * 供应商信息
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_provider_inf")
public class ProviderInf extends Model<ProviderInf> {
 
    /**
     * provider_id
     */
    @TableId(value = "provider_id" ,type = IdType.UUID)
    private String providerId;
 
    /**
     * 专项类型id
     */
    @TableField("b_id")
    private String bId;
 
    /**
     * 供应商名称
     */
    @TableField("provider_name")
    private String providerName;
 
    /**
     * app_url
     */
    @TableField("app_url")
    private String appUrl;
 
    /**
     * app_secret
     */
    @TableField("app_secret")
    private String appSecret;
 
    /**
     * access_token
     */
    @TableField("access_token")
    private String accessToken;
 
    /**
     * 0:是, 1:否
     */
    @TableField("default_route")
    private String defaultRoute;
 
    /**
     * 折扣率
     */
    @TableField("provider_rate")
    private BigDecimal providerRate;
 
    /**
     * 操作顺序
     */
    @TableField("oper_solr")
    private Integer operSolr;
 
    /**
     * 0:否，1：是
     */
    @TableField("is_open")
    private String isOpen;
 
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


    @Override
    protected Serializable pkVal() { 
        return this.providerId;
    }
}
