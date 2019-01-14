package com.ebeijia.zl.web.cms.couponManage.service.impl;

import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.ImageTypeEnum;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.coupon.dao.domain.TbCouponProduct;
import com.ebeijia.zl.coupon.dao.service.ITbCouponProductService;
import com.ebeijia.zl.web.cms.base.exception.BizHandlerException;
import com.ebeijia.zl.web.cms.base.service.ImageService;
import com.ebeijia.zl.web.cms.base.vo.FTPImageVo;
import com.ebeijia.zl.web.cms.couponManage.service.CouponManageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service("couponManageService")
public class CouponManageServiceIml implements CouponManageService{


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${FILE_UPLAOD_PATH}")
    private String FILE_UPLAOD_PATH;

    @Value("${FILE_UPLAOD_SEPARATOR}")
    private String FILE_UPLAOD_SEPARATOR;

    @Value("${IMG_SERVER}")
    private String IMG_SERVER;

    @Value("${FILE_NEW_PATH}")
    private String FILE_NEW_PATH;

    @Autowired
    ITbCouponProductService couponProductService;

    @Autowired
    private ImageService imageService;


    @Override
    public PageInfo<TbCouponProduct> getCouponListPage(int startNum, int pageSize, TbCouponProduct couponProduct) {
        List<TbCouponProduct> goodsSpecList = new ArrayList<>();

        PageHelper.startPage(startNum, pageSize);
        List<TbCouponProduct> tbCouponProducts = couponProductService.getCouponList(couponProduct);

        PageInfo<TbCouponProduct> page = new PageInfo<TbCouponProduct>(tbCouponProducts);
        return page;
    }

    @Override
    public BaseResult<Object> addCouponValues(TbCouponProduct entity, MultipartFile iconImageFile) {
        if (iconImageFile == null || iconImageFile.isEmpty()) {
            return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews03.getCode(), ExceptionEnum.ImageNews.ImageNews03.getMsg());
        }

        FTPImageVo imgVo = new FTPImageVo();
        imgVo.setImgId(entity.getCouponCode());
        imgVo.setService(IMG_SERVER);
        imgVo.setNewPath(FILE_NEW_PATH);
        imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
        imgVo.setUploadPath(FILE_UPLAOD_PATH);
        imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_02.getValue());

        try {
            String imageUrl = imageService.uploadImangeName(imgVo, iconImageFile);
            if (StringUtil.isNullOrEmpty(imageUrl)) {
                logger.error("## 卡券值图片上传返回路径为空");
                return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews01.getCode(), ExceptionEnum.ImageNews.ImageNews01.getMsg());
            }
            entity.setIconImage(imageUrl);
        } catch (BizHandlerException e) {
            logger.error("## 卡券值图片上传异常", e.getMessage());
            return ResultsUtil.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("## 卡券值图片上传异常");
            return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
        }
        if (!couponProductService.save(entity)) {
            logger.error("## 新增卡券值信息失败");
            return ResultsUtil.error(ExceptionEnum.CouponNews.CouponNews01.getCode(), ExceptionEnum.CouponNews.CouponNews01.getMsg());
        }
        return ResultsUtil.success();
    }

    @Override
    public BaseResult<Object> editCouponValues(TbCouponProduct couponProduct, MultipartFile iconImageFile) {
        if (iconImageFile == null || iconImageFile.isEmpty()) {
           /* return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews03.getCode(), ExceptionEnum.ImageNews.ImageNews03.getMsg());*/
            if (!couponProductService.updateById(couponProduct)) {
                logger.error("## 修改卡券值信息失败");
                return ResultsUtil.error(ExceptionEnum.CouponNews.CouponNews03.getCode(), ExceptionEnum.CouponNews.CouponNews03.getMsg());
            }
            return ResultsUtil.success();
        }

        FTPImageVo imgVo = new FTPImageVo();
        imgVo.setImgId(couponProduct.getCouponCode());
        imgVo.setService(IMG_SERVER);
        imgVo.setNewPath(FILE_NEW_PATH);
        imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
        imgVo.setUploadPath(FILE_UPLAOD_PATH);
        imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_02.getValue());

        try {
            String imageUrl = imageService.uploadImangeName(imgVo, iconImageFile);
            if (StringUtil.isNullOrEmpty(imageUrl)) {
                logger.error("## 卡券值图片上传返回路径为空");
                return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews01.getCode(), ExceptionEnum.ImageNews.ImageNews01.getMsg());
            }
            couponProduct.setIconImage(imageUrl);
        } catch (BizHandlerException e) {
            logger.error("## 卡券值图片上传异常", e.getMessage());
            return ResultsUtil.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("## 卡券值图片上传异常");
            return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
        }
        if (!couponProductService.updateById(couponProduct)) {
            logger.error("## 修改卡券值信息失败");
            return ResultsUtil.error(ExceptionEnum.CouponNews.CouponNews03.getCode(), ExceptionEnum.CouponNews.CouponNews03.getMsg());
        }
        return ResultsUtil.success();
    }

    @Override
    public BaseResult<Object> deleteGoodsSpec(TbCouponProduct couponProduct) {
        if (!couponProductService.updateById(couponProduct)) {
            logger.error("## 删除商品规格信息失败");
            return ResultsUtil.error(ExceptionEnum.CouponNews.CouponNews01.getCode(), ExceptionEnum.CouponNews.CouponNews01.getMsg());
        }
        return ResultsUtil.success();
    }



}
