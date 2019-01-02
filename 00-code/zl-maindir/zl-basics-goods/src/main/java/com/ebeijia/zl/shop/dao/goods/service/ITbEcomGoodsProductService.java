package com.ebeijia.zl.shop.dao.goods.service;

import com.ebeijia.zl.shop.dao.goods.domain.GoodsProduct;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * 货品(SKU)信息表 Service 接口类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
public interface ITbEcomGoodsProductService extends IService<TbEcomGoodsProduct> {

    /**
     * 查询货品信息（多条件查询）
     * @param ecomGoodsProduct
     * @return
     */
    List<TbEcomGoodsProduct> getGoodsProductList(TbEcomGoodsProduct ecomGoodsProduct);

    /**
     * 查询某个商品的货品
     *
     * @param goodsId
     * @return
     */
    public List<TbEcomGoodsProduct> getProductlistByGoodsId(String goodsId);

    /**
     * 根据SkuCode查询Sku商品信息
     * @param skuCode
     * @return
     */
    TbEcomGoodsProduct getGoodsProductBySkuCode(String skuCode);

}
