package com.ebeijia.zl.shop.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用于包装账户认证中心返回的令牌信息
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Token {

    private String token;
//    private String userId;

    public Token(String token) {
        this.token = token;
//        this.userId = userId;
    }
}
