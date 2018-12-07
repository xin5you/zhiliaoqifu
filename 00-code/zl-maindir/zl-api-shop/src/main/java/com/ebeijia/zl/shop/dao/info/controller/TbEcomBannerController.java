package com.ebeijia.zl.shop.dao.info.controller;

import com.ebeijia.zl.shop.dao.info.domain.TbEcomBanner;
import com.ebeijia.zl.shop.dao.info.service.impl.ITbEcomBannerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bestTeam.qtbt.controller.BaseController;


/**
 *
 * Banner信息 Controller 实现类
 *
 * @User J
 * @Date 2018-12-07
 */
@RestController
@RequestMapping("b/ecom/banner")
public class TbEcomBannerController extends BaseController<ITbEcomBannerService,TbEcomBanner>{
}