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
 * 平台用户信息表
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_web_user")
public class User extends Model<User> {
 
    /**
     * 用户id
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;
 
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
 
    /**
     * 密码
     */
    @TableField("password")
    private String password;
 
    /**
     * 登录名
     */
    @TableField("login_name")
    private String loginName;
 
    /**
     * 手机号码
     */
    @TableField("phone_no")
    private String phoneNo;
 
    /**
     * 1.oms(运营管理平台)
            2.cms(电商管理平台)
            3.diy(商户自助服务平台)
     */
    @TableField("login_type")
    private String loginType;
 
    /**
     * 默认标志
     */
    @TableField("isdefault")
    private String isdefault;
 
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
 
    /**
     * 组织机构id
     */
    @TableField("organization_id")
    private String organizationId;
 
    /**
     * 所属供应商
     */
    @TableField("supplier_id")
    private String supplierId;

    @TableField(exist = false)
    private String roleCheckflag;

    @TableField(exist = false)
    private String roleName;

    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
