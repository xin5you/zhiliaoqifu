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
 * 平台角色表
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_web_role")
public class Role extends Model<Role> {
 
    /**
     * 角色id
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;
 
    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;
 
    /**
     * 角色描述
     */
    @TableField("description")
    private String description;
 
    /**
     * 1.oms(运营管理平台)
            2.cms(电商管理平台)
            3.diy(商户自助服务平台)
     */
    @TableField("login_type")
    private String loginType;
 
    /**
     * 序号
     */
    @TableField("seq")
    private Integer seq;
 
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
