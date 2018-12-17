package com.ebeijia.zl.web.oms.sys.model;


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
 * 平台资源表
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_web_resource")
public class Resource extends Model<Resource> {
 
    /**
     * 资源id
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;
 
    /**
     * 资源描述
     */
    @TableField("description")
    private String description;
 
    /**
     * 标识
     */
    @TableField("icon")
    private String icon;
 
    /**
     * 资源名称
     */
    @TableField("resource_name")
    private String resourceName;
 
    /**
     * 0菜单 1功能
     */
    @TableField("resource_type")
    private String resourceType;
 
    /**
     * 资源Key
     */
    @TableField("resource_key")
    private String resourceKey;
 
    /**
     * 序号
     */
    @TableField("seq")
    private Integer seq;
 
    /**
     * URL
     */
    @TableField("url")
    private String url;
 
    /**
     * 父级编号
     */
    @TableField("pid")
    private String pid;
 
    /**
     * 0启用 1停用
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
     * 1.oms(运营管理平台)
            2.cms(电商管理平台)
            3.diy(商户自助服务平台)
     */
    @TableField("login_type")
    private String loginType;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
