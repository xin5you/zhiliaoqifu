package com.ebeijia.zl.shop.service.goods;

import com.ebeijia.zl.shop.dao.goods.domain.*;
import com.ebeijia.zl.shop.vo.GoodsDetailInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IProductService {


    GoodsDetailInfo getDetail(String goodsId);

    List<TbEcomGoodsGallery> getGallery(String goodsId);

    List<TbEcomGoodsProduct> listSkuByGoodsId(String goodsId);

    PageInfo<Goods> listGoods(String billingType, String catid, String orderby, Integer start, Integer limit);

    PageInfo<Goods> listGoods(String billingType, String orderby, Integer start, Integer limit);
}
