package com.ebeijia.zl.shop.constants;

public interface ResultState {
    //代表正常
    int OK = 200;

    //代表没有更多内容
    int NO_CONTENT = 204;

    //没有登录
    int UNAUTHORIZED = 401;

    //没有发现资源
    int NOT_FOUND = 404;

    //用户或者参数不匹配
    int NOT_ACCEPTABLE = 406;

    //服务器处理发生错误
    int ERROR = 500;
}
