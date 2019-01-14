package com.ebeijia.zl.web.cms.couponManage.controller;

import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.coupon.dao.domain.TbCouponTransLog;
import com.ebeijia.zl.web.cms.couponManage.service.CouponTransService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "couponTransManage")
public class CouponTransManage {


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CouponTransService couponTransService;

    /**
     * 卡券流水管理（分页）
     * @param req
     * @param couponTransLog
     * @return
     */
    @RequestMapping(value = "/getCouponTransLogList")
    public ModelAndView getGoodsSpecList(HttpServletRequest req, TbCouponTransLog couponTransLog) {
        ModelAndView mv = new ModelAndView("couponManage/getCouponTransLogList");
        int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
        PageInfo<TbCouponTransLog> pageInfo = new PageInfo<>();

        try {
            pageInfo = couponTransService.getCouponListPage(startNum, pageSize, couponTransLog);
        } catch (Exception e){
            logger.error("## 查询卡券信息异常[{}]", e);
        }

        mv.addObject("pageInfo", pageInfo);
        mv.addObject("couponProduct", couponTransLog);

        return mv;
    }









}
