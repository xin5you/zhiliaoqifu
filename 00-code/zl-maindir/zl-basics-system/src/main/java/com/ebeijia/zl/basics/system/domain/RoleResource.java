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
 * 平台角色资源关联表
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_web_role_resource")
public class RoleResource extends Model<RoleResource> {
 
    /**
     * 资源id
     */
    @TableField("resource_id")
    private String resourceId;
 
    /**
     * 角色id
     */
    @TableField("role_id")
    private String roleId;
 
    /**
     * 主键
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private Integer id;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
