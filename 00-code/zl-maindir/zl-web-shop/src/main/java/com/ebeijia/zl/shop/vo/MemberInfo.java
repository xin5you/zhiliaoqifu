package com.ebeijia.zl.shop.vo;

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
public class MemberInfo{

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
