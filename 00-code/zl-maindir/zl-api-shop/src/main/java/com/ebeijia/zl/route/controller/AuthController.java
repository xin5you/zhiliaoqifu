
package com.ebeijia.zl.route.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于定义认证接口
 */
@RestController
@RequestMapping("/auth")
public class AuthController {


    public String login(String id, String pwd) {
        return null;
    }

}
