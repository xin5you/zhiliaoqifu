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
    private String memberId;


    /**
     * 用户信息_id
     */
    private String userId;


    /**
     * 手机号
     */
    private String mobilePhoneNo;

}
