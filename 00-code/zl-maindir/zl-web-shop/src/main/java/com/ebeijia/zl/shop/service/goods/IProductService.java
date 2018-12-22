package com.ebeijia.zl.shop.service.goods;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsGallery;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsProduct;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IProductService {

    PageInfo<TbEcomGoods> listGoods(String billingType, Integer catid, String orderby, Integer start, Integer limit);

    TbEcomGoodsDetail getDetail(String goodsId);

    List<TbEcomGoodsGallery> getGallery(String goodsId);

    List<TbEcomGoodsProduct> listSkuByGoodsId(String goodsId);

    PageInfo<TbEcomGoods> listGoods(String billingType, String orderby, Integer start, Integer limit);
}
