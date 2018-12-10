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
 * 消息表
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("t_wxcms_msg_news")
public class MsgNews extends Model<MsgNews> {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -3783790657393921264L;

	/**
     * 消息id
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;
 
    /**
     * 标题
     */
    @TableField("title")
    private String title;
 
    /**
     * 作者
     */
    @TableField("author")
    private String author;
 
    /**
     * 简介
     */
    @TableField("brief")
    private String brief;
 
    /**
     * 描述
     */
    @TableField("description")
    private String description;
 
    /**
     * 封面图片
     */
    @TableField("picpath")
    private String picpath;
 
    /**
     * 图片显示标志
     */
    @TableField("showpic")
    private Integer showpic;
 
    /**
     * 图文连接
     */
    @TableField("url")
    private String url;
 
    /**
     * 外部连接
     */
    @TableField("fromurl")
    private String fromurl;
 
    /**
     * 消息主表ID
     */
    @TableField("base_id")
    private String baseId;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
