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
 * 微信菜单组
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("t_wxcms_account_menu_group")
public class AccountMenuGroup extends Model<AccountMenuGroup> {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -1164532674327256660L;

	/**
     * 主键
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;
 
    /**
     * 名称
     */
    @TableField("name")
    private String name;
 
    /**
     * 是否打开
     */
    @TableField("enable")
    private Integer enable;
 
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
