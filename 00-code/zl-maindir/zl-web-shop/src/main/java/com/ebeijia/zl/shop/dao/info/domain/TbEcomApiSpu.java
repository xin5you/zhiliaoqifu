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
 * 渠道API通讯信息
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_api_spu")
@ApiModel("渠道API通讯信息")
public class TbEcomApiSpu extends Model<TbEcomApiSpu> {
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
 
    /**
     * 渠道代码
     */
    @TableField("ecom_code")
    @ApiModelProperty(value = "渠道代码")
    private String ecomCode;
 
    /**
     * 渠道SPU
     */
    @TableField("spu_code")
    @ApiModelProperty(value = "渠道SPU")
    private String spuCode;
 
    /**
     * 同步状态 0:未同步，1已同步
     */
    @TableField("sku_sync_stat")
    @ApiModelProperty(value = "同步状态 0:未同步，1已同步")
    private String skuSyncStat;
 
    /**
     * 数据状态 0:可用，1：不可用
     */
    @TableField("data_stat")
    @ApiModelProperty(value = "数据状态 0:可用，1：不可用")
    private String dataStat;
 
    /**
     * 备注
     */
    @TableField("remarks")
    @ApiModelProperty(value = "备注")
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
        return this.id;
    }
}
