package com.ebeijia.zl.service.control.sms.domain;


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
 * tb_sms_details
 *
 * @User zhuqi
 * @Date 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_sms_details")
public class TbSmsDetails extends Model<TbSmsDetails> {
 
    /**
     * msgId
     */
    @TableId(value = "msg_id" ,type = IdType.UUID)
    private String msgId;
 
    /**
     * phoneNumber
     */
    @TableField("phone_number")
    private String phoneNumber;
 
    /**
     * template_param
     */
    @TableField("template_param")
    private String templateParam;
 
    /**
     * transChnl
     */
    @TableField("trans_chnl")
    private String transChnl;
 
    /**
     * smsType
     */
    @TableField("sms_type")
    private String smsType;
 
    /**
     * respCode
     */
    @TableField("resp_code")
    private String respCode;
 
    /**
     * respMsg
     */
    @TableField("resp_msg")
    private String respMsg;
 
    /**
     * respId
     */
    @TableField("resp_id")
    private String respId;
 
    /**
     * 数据状态
     */
    @TableField("data_stat")
    private String data;
 
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


    @Override
    protected Serializable pkVal() { 
        return this.msgId;
    }
}
