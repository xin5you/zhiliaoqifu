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
 * 平台用户角色关联表
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_web_user_role")
public class UserRole extends Model<UserRole> {
 
    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;
 
    /**
     * 角色id
     */
    @TableField("role_id")
    private String roleId;
 
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
