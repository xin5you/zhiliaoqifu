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
 * 微信客户粉丝表
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("t_wx_client_fans")
public class AccountFans extends Model<AccountFans> {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 5919812540763771072L;

	/**
     * 客户粉丝id
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;
 
    /**
     * 微信id
     */
    @TableField("openid")
    private String openId;
    
    /**
     * unionid
     */
    @TableField("unionid")
    private String unionid;
    
    /**
     * groupid
     */
    @TableField("groupid")
    private String groupid;
 
    /**
     * 微信号
     */
    @TableField("wxid")
    private String wxid;
 
    /**
     * 订阅状态
     */
    @TableField("subscribestatus")
    private Integer subscribestatus;
 
    /**
     * 订阅时间
     */
    @TableField("subscribetime")
    private String SubscribeTime;
 
    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;
 
    /**
     * 性别
     */
    @TableField("gender")
    private Integer gender;
 
    /**
     * 语言
     */
    @TableField("language")
    private String language;
 
    /**
     * 国家
     */
    @TableField("country")
    private String country;
 
    /**
     * 省
     */
    @TableField("province")
    private String province;
 
    /**
     * 城市
     */
    @TableField("city")
    private String city;
 
    /**
     * 头像
     */
    @TableField("headimgurl")
    private String headimgurl;
 
    /**
     * 粉丝状态
     */
    @TableField("fans_status")
    private String fansStatus;
 
    /**
     * 创建时间
     */
    @TableField("createtime")
    private Long createtime;
 
    /**
     * 数据状态
     */
    @TableField("status")
    private Integer status;
 
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
