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
 * 用户个人信息
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_person_inf")
public class PersonInf extends Model<PersonInf> {
 
    /**
     * 36
     */
    @TableId(value = "personal_id" ,type = IdType.UUID)
    private String personalId;
 
    /**
     * 用户信息_id
     */
    @TableField("user_id")
    private String userId;
 
    /**
     * 姓名
     */
    @TableField("personal_name")
    private String personalName;
 
    /**
     * 关键业务数据
     */
    @TableField("personal_card_type")
    private String personalCardType;
 
    /**
     * 关键业务数据
     */
    @TableField("personal_card_no")
    private String personalCardNo;
 
    /**
     * 性别
     */
    @TableField("sex")
    private String sex;
 
    /**
     * 生日
     */
    @TableField("birthday")
    private String birthday;
 
    /**
     * 籍贯
     */
    @TableField("native_place")
    private String nativePlace;
 
    /**
     * 手机号
     */
    @TableField("mobile_phone_no")
    private String mobilePhoneNo;
 
    /**
     * 邮箱
     */
    @TableField("email")
    private String email;
 
    /**
     * 婚姻状况
     */
    @TableField("marriage_stat")
    private String marriageStat;
 
    /**
     * 居住地址
     */
    @TableField("home_address")
    private String homeAddress;
 
    /**
     * 公司地址
     */
    @TableField("company_address")
    private String companyAddress;
 
    /**
     * 100 现金 200 银行卡 300 微信 310 支付宝 900 其他
     */
    @TableField("trans_habit")
    private String transHabit;
 
    /**
     * 数据状态
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
        return this.personalId;
    }
}
