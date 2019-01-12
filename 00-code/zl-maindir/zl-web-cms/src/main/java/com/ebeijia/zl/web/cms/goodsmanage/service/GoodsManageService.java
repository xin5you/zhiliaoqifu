package com.ebeijia.zl.web.cms.goodsmanage.service;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.shop.dao.goods.domain.*;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface GoodsManageService {

    /**
     * 商品规格列表（分页）
     * @param startNum
     * @param pageSize
     * @param entity
     * @return
     */
    PageInfo<TbEcomSpecification> getGodosSpecListPage(int startNum, int pageSize, TbEcomSpecification entity);

    /**
     * 商品规格新增
     * @param entity
     * @param specImgFile
     * @return
     */
    BaseResult<Object> addGoodsSpec(TbEcomSpecification entity, MultipartFile specImgFile);

    /**
     * 商品规格编辑
     * @param entity
     * @param specImgFile
     * @return
     */
    BaseResult<Object> editGoodsSpec(TbEcomSpecification entity, MultipartFile specImgFile);

    /**
     * 商品规格删除
     * @param entity
     * @return
     */
    BaseResult<Object> deleteGoodsSpec(TbEcomSpecification entity);

    /**
     * 商品规格值列表（分页）
     * @param startNum
     * @param pageSize
     * @param entity
     * @return
     */
    PageInfo<TbEcomSpecValues> getGodosSpecValuesListPage(int startNum, int pageSize, TbEcomSpecValues entity);

    /**
     * 商品规格值新增
     * @param entity
     * @param specImageFile
     * @return
     */
    BaseResult<Object> addGoodsSpecValues(TbEcomSpecValues entity, MultipartFile specImageFile);

    /**
     * 商品规格值编辑
     * @param entity
     * @param specImageFile
     * @return
     */
    BaseResult<Object> editGoodsSpecValues(TbEcomSpecValues entity, MultipartFile specImageFile);

    /**
     * 商品规格删除
     * @param entity
     * @return
     */
    BaseResult<Object> deleteGoodsSpecValues(TbEcomSpecValues entity);

    /**
     * 查询商品(Spu)信息列表（分页）
     * @param entity
     * @return
     */
    PageInfo<TbEcomGoods> getGoodsInfListPage(int startNum, int pageSize, TbEcomGoods entity);

    /**
     * 商品信息新增
     * @param entity
     * @param goodsImgFile
     * @return
     */
    BaseResult<Object> addGoodsInf(TbEcomGoods entity, MultipartFile goodsImgFile);

    /**
     * 商品信息编辑
     * @param entity
     * @param goodsImgFile
     * @return
     */
    BaseResult<Object> editGoodsInf(TbEcomGoods entity, MultipartFile goodsImgFile);

    /**
     * 商品信息删除
     * @param req
     * @return
     */
    BaseResult<Object> deleteGoodsInf(HttpServletRequest req);

    /**
     * 更新商品上下架状态
     * @return
     */
    BaseResult<Object> updateGoodsInfEnable(HttpServletRequest req);

    /**
     * 查询商品相册信息列表（分页）
     * @param entity
     * @return
     */
    PageInfo<TbEcomGoodsGallery> getGoodsGalleryListPage(int startNum, int pageSize, TbEcomGoodsGallery entity);

    /**
     * 商品相册信息新增
     * @param entity
     * @param thumbnailFile
     * @return
     */
    BaseResult<Object> addGoodsGallery(TbEcomGoodsGallery entity, MultipartFile thumbnailFile);

    /**
     * 商品相册信息编辑
     * @param entity
     * @param thumbnailFile
     * @return
     */
    BaseResult<Object> editGoodsGallery(TbEcomGoodsGallery entity, MultipartFile thumbnailFile);

    /**
     * 商品相册信息删除
     * @param req)
     * @return
     */
    BaseResult<Object> deleteGoodsGallery(HttpServletRequest req);

    /**
     * 查询商品Sku信息列表（分页）
     * @param entity
     * @return
     */
    PageInfo<TbEcomGoodsProduct> getGoodsProductListPage(int startNum, int pageSize, TbEcomGoodsProduct entity);

    /**
     * 商品Sku信息新增
     * @param entity
     * @param picUrlFile
     * @return
     */
    BaseResult<Object> addGoodsProduct(TbEcomGoodsProduct entity, MultipartFile picUrlFile);

    /**
     * 商品Sku信息编辑
     * @param entity
     * @param picUrlFile
     * @return
     */
    BaseResult<Object> editGoodsProduct(TbEcomGoodsProduct entity, MultipartFile picUrlFile);

    /**
     * 商品Sku信息删除
     * @param req)
     * @return
     */
    BaseResult<Object> deleteGoodsProduct(HttpServletRequest req);

    /**
     * 查询商品详情信息列表（分页）
     * @param entity
     * @return
     */
    PageInfo<TbEcomGoodsDetail> getGoodsDetailListPage(int startNum, int pageSize, TbEcomGoodsDetail entity);

}
