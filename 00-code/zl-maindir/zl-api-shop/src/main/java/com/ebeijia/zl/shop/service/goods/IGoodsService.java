package com.ebeijia.zl.shop.service.goods;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsGallery;
import com.github.pagehelper.PageInfo;

public interface IGoodsService {

    PageInfo<TbEcomGoods> listGoods(Integer catid, String orderby, Integer start, Integer limit);

    TbEcomGoodsDetail getDetail(String goodsId);

    TbEcomGoodsGallery getGallery(String goodsId);
}
