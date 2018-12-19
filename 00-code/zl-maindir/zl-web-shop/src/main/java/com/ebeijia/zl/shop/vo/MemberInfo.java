package com.ebeijia.zl.shop.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户个人信息
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
public class MemberInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 36
     */
    private String personalId;

    /**
     * 用户信息_id
     */
    private String userId;

    /**
     * 姓名
     */
    private String personalName;


    /**
     * 公司名
     */
    private String companyName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 手机号
     */
    private String mobilePhoneNo;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 婚姻状况
     */
    private String marriageStat;

    /**
     * 居住地址
     */
    private String homeAddress;

    /**
     * 公司地址
     */
    private String companyAddress;

    /**
     * 备注
     */
    private String remarks;

}
