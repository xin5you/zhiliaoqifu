package com.ebeijia.zl.facade.user.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * 用户信息
 *
 * @author zhuqi
 * @Date 2018-11-30
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_user_inf")
public class UserInf extends Model<UserInf> {
 
    /**
     * 用户信息_id
     */
    @TableId(value = "user_id" ,type = IdType.UUID)
    private String userId;
 
    /**
     * 企业id
     */
    @TableField("company_id")
    private String companyId;
 
    /**
     * 关键业务数据
     */
    @TableField("user_name")
    private String userName;
 
    /**
     * 		100：企业员工账户
     *      200：企业账户
     *      300：供应商账户
     *      400：分销商账户
     */
    @TableField("user_type")
    private String userType;
 
    /**
     * 密码
     */
    @TableField("user_passwd")
    private  String userPasswd;
 
    /**
     * 产品号
     */
    @TableField("product_code")
    private String productCode;
 
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
        int dayOfYear = Calendar.DAY_OF_YEAR;
        return this.userId;
    }
}
