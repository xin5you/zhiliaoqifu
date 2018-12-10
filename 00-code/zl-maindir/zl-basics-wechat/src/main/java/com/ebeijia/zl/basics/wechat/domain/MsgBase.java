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
 * 消息基础表
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("t_wxcms_msg_base")
public class MsgBase extends Model<MsgBase> {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 5724477854731219113L;

	/**
     * 主键
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;
 
    /**
     * 消息类型
     */
    @TableField("msgtype")
    private String msgtype;
 
    /**
     * 输入码
     */
    @TableField("inputcode")
    private String inputcode;
 
    /**
     * 规则
     */
    @TableField("rule")
    private String rule;
 
    /**
     * 启用标志
     */
    @TableField("enable")
    private Integer enable;
 
    /**
     * 消息阅读数
     */
    @TableField("readcount")
    private Integer readcount;
 
    /**
     * 消息点赞数
     */
    @TableField("favourcount")
    private Integer favourcount;
 
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
