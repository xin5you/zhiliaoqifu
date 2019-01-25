package com.ebeijia.zl.web.cms.couponManage.controller;

import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.coupon.dao.domain.TbCouponProduct;
import com.ebeijia.zl.coupon.dao.service.ITbCouponProductService;
import com.ebeijia.zl.web.cms.base.exception.BizHandlerException;
import com.ebeijia.zl.web.cms.couponManage.service.CouponManageService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "couponManage")
public class CouponManageController {


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CouponManageService couponManageService;

    @Autowired
    private ITbCouponProductService iTbCouponProductService;

    /**
     * 商品规格信息列表（分页）
     * @param req
     * @param couponProduct
     * @return
     */
    @RequestMapping(value = "/getCouponsInfList")
    public ModelAndView getGoodsSpecList(HttpServletRequest req, TbCouponProduct couponProduct) {
        ModelAndView mv = new ModelAndView("couponManage/getCouponsInfList");
        int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
        PageInfo<TbCouponProduct> pageInfo = new PageInfo<>();

        try {
           pageInfo = couponManageService.getCouponListPage(startNum, pageSize, couponProduct);
        } catch (Exception e){
            logger.error("## 查询卡券信息异常[{}]", e);
        }

        SpecAccountTypeEnum[] values = SpecAccountTypeEnum.values();
        List<Object> list = new ArrayList<>();
        for (int i =0;i<values.length;i++){
            if("A"!=values[i].getCode()){
                list.add(values[i]);
            }
        }

        mv.addObject("pageInfo", pageInfo);
        mv.addObject("couponProduct", couponProduct);
        mv.addObject("specAccountTypeList", list);



        return mv;
    }


    /**
     * 新增卡券信息
     * @param iconImageFile
     * @param req
     * @return
     */
    @PostMapping(value = "/addCouponValues")
    public BaseResult<Object> addCouponValues(@RequestParam("iconImageFile") MultipartFile iconImageFile, HttpServletRequest req) {
        BaseResult<Object> result = new BaseResult<>();
        try {
            TbCouponProduct couponProduct = getTbCouponProductValues(req);
            if(StringUtil.isNullOrEmpty(couponProduct.getCouponCode())){
                couponProduct.setCouponCode(IdUtil.getNextId());
                result = couponManageService.addCouponValues(couponProduct, iconImageFile);
            }else {
                result=couponManageService.editCouponValues(couponProduct,iconImageFile);
            }

        } catch (BizHandlerException e) {
            logger.error("## 商品规格值图片上传异常", e.getMessage());
            return ResultsUtil.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("## 新增卡券值信息出错", e);
            return ResultsUtil.error(ExceptionEnum.CouponNews.CouponNews02.getCode(), ExceptionEnum.CouponNews.CouponNews02.getMsg());
        }
        return result;
    }

    /**
     * 卡券信息信息值封装类方法
     * @param req
     * @return
     */
    private TbCouponProduct getTbCouponProductValues(HttpServletRequest req) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute(Constants.SESSION_USER);

        String couponCode = req.getParameter("couponCode");
        String couponName = req.getParameter("coupon_Name");
        Long price = Long.parseLong(req.getParameter("price"));
        //产品类型暂时没用,默认为1
       // String couponType = req.getParameter("couponType");
        String bId = req.getParameter("b_id");
        String couponDesc = req.getParameter("couponDesc");
        String iconImage = req.getParameter("iconImage");
       /* String availableNum = req.getParameter("availableNum");
        String totalNum = req.getParameter("totalNum");*/
        String remarks = req.getParameter("remarks");

        TbCouponProduct couponProduct = null;

        if(!StringUtil.isNullOrEmpty(couponCode)){
            couponProduct = iTbCouponProductService.getById(couponCode);
            couponProduct.setLockVersion(couponProduct.getLockVersion() + 1);
        }else {
            couponProduct = new TbCouponProduct();
            //couponProduct.setCouponCode(IdUtil.getNextId());
            couponProduct.setCreateUser(user.getId());
            couponProduct.setCreateTime(System.currentTimeMillis());
            couponProduct.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
            couponProduct.setLockVersion(0);
        }
        couponProduct.setBId(bId);
        couponProduct.setCouponName(couponName);
        couponProduct.setPrice(price*100);
        couponProduct.setCouponType("1");
        couponProduct.setCouponDesc(couponDesc);
        couponProduct.setIconImage(iconImage);
      /*  couponProduct.setTotalNum(Integer.parseInt(totalNum));
        couponProduct.setAvailableNum(Integer.parseInt(availableNum));*/
        couponProduct.setRemarks(remarks);
        couponProduct.setUpdateUser(user.getId());
        couponProduct.setUpdateTime(System.currentTimeMillis());

        return couponProduct;
    }


    //getCouponsInf
    /**
     * 根据主键查询卡券信息
     * @param req
     * @param couponProduct
     * @return
     */
    @PostMapping(value = "/getCouponsInf")
    public TbCouponProduct getGoodsSpec(HttpServletRequest req, TbCouponProduct couponProduct) {
        try {
            couponProduct = iTbCouponProductService.getById(couponProduct.getCouponCode());
            couponProduct.setPrice(couponProduct.getPrice()/100);
        } catch (Exception e) {
            logger.error("## 查询主键为[{}]的商品规格信息出错", couponProduct.getCouponCode(), e);
        }
        return couponProduct;
    }


    /**
     * 删除卡券规格信息
     * @param req
     * @return
     */
    @PostMapping(value = "/deleteCoupon")
    public BaseResult<Object> deleteGoodsSpec(HttpServletRequest req) {
        BaseResult<Object> result = new BaseResult<>();
        try {
            String couponCode = req.getParameter("couponCode");
            TbCouponProduct couponProduct = iTbCouponProductService.getById(couponCode);
            couponProduct.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
            result = couponManageService.deleteGoodsSpec(couponProduct);
        } catch (BizHandlerException e) {
            logger.error("## 删除商品规格图片异常", e.getMessage());
            return ResultsUtil.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("## 删除商品规格信息出错", e);
            return ResultsUtil.error(ExceptionEnum.CouponNews.CouponNews01.getCode(), ExceptionEnum.CouponNews.CouponNews01.getMsg());
        }
        return result;
    }












}
