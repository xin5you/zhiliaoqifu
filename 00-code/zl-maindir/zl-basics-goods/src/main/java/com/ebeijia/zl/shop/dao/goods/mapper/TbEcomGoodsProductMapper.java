package com.ebeijia.zl.shop.dao.goods.mapper;

import com.ebeijia.zl.shop.dao.goods.domain.GoodsProduct;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 货品(SKU)信息表 Mapper 接口
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Mapper
public interface TbEcomGoodsProductMapper extends BaseMapper<TbEcomGoodsProduct> {

    /**
     * 查询某个商品的货品
     *
     * @param goodsId
     * @return
     */
    public List<TbEcomGoodsProduct> getGoodsProductListByGoodsId(String goodsId);

    /**
     * 库存列表
     *
     * @param pro
     * @return
     */
    List<GoodsProduct> getInventoryList(GoodsProduct pro);


    GoodsProduct getGoodsProductByPrimaryKey(String primaryKey);

    /**
     * 查询货品信息（多条件查询）
     *
     * @param ecomGoodsProduct
     * @return
     */
    List<TbEcomGoodsProduct> getGoodsProductList(TbEcomGoodsProduct ecomGoodsProduct);

    /**
     * 查询所有sku信息（没有关联的spu的sku信息）
     *
     * @param skuList
     * @return
     */
    List<TbEcomGoodsProduct> getGoodsProductListBySkuList(List<String> skuList);

    /**
     * 根据SkuCode查询Sku商品信息
     *
     * @param skuCode
     * @return
     */
    TbEcomGoodsProduct getGoodsProductBySkuCode(String skuCode);
}
