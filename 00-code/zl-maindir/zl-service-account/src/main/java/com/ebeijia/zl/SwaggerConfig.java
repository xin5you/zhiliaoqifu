package com.ebeijia.zl;

import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.deepoove.swagger.dubbo.annotations.EnableDubboSwagger;

@DubboComponentScan(basePackages = { "com.ebeijia.zl.service.account.facade" })  //dubbo实现类的路径
@EnableDubboSwagger //生成api-docs及调用的REST接口
@Configuration
public class SwaggerConfig {

}
