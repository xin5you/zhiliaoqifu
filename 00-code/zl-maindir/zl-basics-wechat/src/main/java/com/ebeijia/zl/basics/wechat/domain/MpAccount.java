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
 * 微信账户表
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("t_wxcms_account")
public class MpAccount extends Model<MpAccount> {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 3670017829189279347L;

	/**
     * 主键
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;
 
    /**
     * 微信账户名
     */
    @TableField("name")
    private String name;
 
    /**
     * 微信号
     */
    @TableField("account")
    private String account;
 
    /**
     * appid
     */
    @TableField("appid")
    private String appid;
 
    /**
     * App密码
     */
    @TableField("appsecret")
    private String appsecret;
 
    /**
     * url
     */
    @TableField("url")
    private String url;
 
    /**
     * Token
     */
    @TableField("token")
    private String token;
 
    /**
     * 消息数
     */
    @TableField("msgcount")
    private Integer msgcount;
 
    /**
     * 创建时间
     */
    @TableField("createtime")
    private Long createtime;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
