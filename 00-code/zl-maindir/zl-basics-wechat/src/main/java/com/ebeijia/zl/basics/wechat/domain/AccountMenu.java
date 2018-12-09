package com.ebeijia.zl.basics.wechat.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * 微信菜单表
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("t_wxcms_account_menu")
public class AccountMenu extends Model<AccountMenu> {
 
    /**
     * 主键
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;
 
    /**
     * 类型
     */
    @TableField("type")
    private String type;
 
    /**
     * even_type
     */
    @TableField("even_type")
    private String evenType;
 
    /**
     * 菜单名称
     */
    @TableField("name")
    private String name;
 
    /**
     * 输出值
     */
    @TableField("inputcode")
    private String inputcode;
 
    /**
     * 链接
     */
    @TableField("url")
    private String url;
 
    /**
     * 级别
     */
    @TableField("sort")
    private Integer sort;
 
    /**
     * 父级Id
     */
    @TableField("parentid")
    private String parentid;
 
    /**
     * 消息id
     */
    @TableField("msgid")
    private String msgid;
 
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;
 
    /**
     * Attribute_11
     */
    @TableField("gid")
    private Integer gid;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
