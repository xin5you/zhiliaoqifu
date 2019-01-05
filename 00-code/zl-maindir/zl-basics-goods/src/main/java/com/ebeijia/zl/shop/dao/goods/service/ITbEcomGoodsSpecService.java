package com.ebeijia.zl.shop.dao.goods.service;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsSpec;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 * SKU规格对照表 Service 接口类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
public interface ITbEcomGoodsSpecService extends IService<TbEcomGoodsSpec> {

    /**
     * 根据goodsId和productId查询商品规格关联表信息
     * @param ecomGoodsSpec
     * @return
     */
    TbEcomGoodsSpec getGoodsSpecByGoodsIdAndProductId(TbEcomGoodsSpec ecomGoodsSpec);
}
