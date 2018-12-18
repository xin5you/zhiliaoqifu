package com.ebeijia.zl.basics.system.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *
 * oms组织机构表
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_web_organization")
public class Organization extends Model<Organization> {
 
    /**
     * 主键
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;
 
    /**
     * 地址
     */
    @TableField("address")
    private String address;
 
    /**
     * 代码
     */
    @TableField("code")
    private String code;
 
    /**
     * 标识
     */
    @TableField("icon")
    private String icon;
 
    /**
     * 名称
     */
    @TableField("name")
    private String name;
 
    /**
     * 序号
     */
    @TableField("seq")
    private Integer seq;
 
    /**
     * 父id
     */
    @TableField("pid")
    private String pid;
 
    /**
     * 状态
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
        return this.id;
    }
}
