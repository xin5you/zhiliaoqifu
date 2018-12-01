package com.ebeijia.zl.facade.user.vo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * 渠道用户信息
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_channel_user_inf")
public class ChannelUserInf extends Model<ChannelUserInf> {
 
    /**
     * 渠道用户id
     */
    @TableId(value = "channel_user_id" ,type = IdType.UUID)
    private String channelUserId;
 
    /**
     * 用户信息_id
     */
    @TableField("user_id")
    private String userId;
 
    /**
     * 关键业务数据
     */
    @TableField("external_id")
    private String externalId;
 
    /**
     * 渠道号
     */
    @TableField("channel_code")
    private String channelCode;
 
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
    @Version
    @TableField("lock_version")
    private Integer lockVersion;


    @Override
    protected Serializable pkVal() { 
        return this.channelUserId;
    }
}
