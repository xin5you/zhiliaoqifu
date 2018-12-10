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
 * 文本消息表
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("t_wxcms_msg_text")
public class MsgText extends Model<MsgText> {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -3746257993570049726L;

	/**
     * 文本消息id
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;
 
    /**
     * 内容
     */
    @TableField("content")
    private String content;
 
    /**
     * 消息主表ID
     */
    @TableField("base_id")
    private Integer baseId;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
