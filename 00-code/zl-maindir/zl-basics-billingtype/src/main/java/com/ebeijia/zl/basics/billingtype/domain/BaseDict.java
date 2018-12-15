package com.ebeijia.zl.basics.billingtype.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典表
 * @author Administrator
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_base_dict")
public class BaseDict extends Model<BaseDict> {
 
    /**
     * id
     */
    @TableId(value = "dict_id" ,type = IdType.UUID)
    private String dictId;
 
    /**
     * 字典代码
     */
    @TableField("dict_code")
    private String dictCode;
 
    /**
     * 是否默认（0:是，1：否）
     */
    @TableField("isdefault")
    private String isdefault;
 
    /**
     * 字典序号
     */
    @TableField("seq")
    private String seq;
 
    /**
     * 字典名称
     */
    @TableField("dict_name")
    private String dictName;
 
    /**
     * 父级Id
     */
    @TableField("pid")
    private String pid;
 
    /**
     * 字典类型
     */
    @TableField("dict_type")
    private String dictType;
 
    /**
     * 字典值
     */
    @TableField("dict_value")
    private String dictValue;
 
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
     * 更新人
     */
    @TableField("update_user")
    private String updateUser;
 
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;
 
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Long updateTime;
 
    /**
     * 乐观锁版本
     */
    @TableField("lock_version")
    private Integer lockVersion;


    @Override
    protected Serializable pkVal() { 
        return this.dictId;
    }
}
