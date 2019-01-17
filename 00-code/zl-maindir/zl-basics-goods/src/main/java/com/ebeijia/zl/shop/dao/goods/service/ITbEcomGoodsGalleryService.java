package com.ebeijia.zl.shop.dao.goods.service;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsGallery;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * 商品相册表 Service 接口类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
public interface ITbEcomGoodsGalleryService extends IService<TbEcomGoodsGallery> {

    /**
     * 查询商品相册信息（多条件查询）
     * @param ecomGoodsGallery
     * @return
     */
    List<TbEcomGoodsGallery> getGoodsGalleryList(TbEcomGoodsGallery ecomGoodsGallery);

    /**
     * 根据isDefault查询商品相册信息是否有默认的标识
     * @param ecomGoodsGallery
     * @return
     */
    TbEcomGoodsGallery getGoodsGalleryByIsDefault(TbEcomGoodsGallery ecomGoodsGallery);

    /**
     * 根据sort查询商品相册时候已存在该序号值
     * @param ecomGoodsGallery
     * @return
     */
    TbEcomGoodsGallery getGoodsGalleryBySort(TbEcomGoodsGallery ecomGoodsGallery);
}
