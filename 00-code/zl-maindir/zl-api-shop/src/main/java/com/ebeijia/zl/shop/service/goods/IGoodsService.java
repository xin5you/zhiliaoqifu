package com.ebeijia.zl.shop.service.goods;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;

import java.util.List;

public interface IGoodsService {

    List<TbEcomGoods> listGoods(int catid, String orderby, int start, int limit);

    TbEcomGoodsDetail goodDetail(String goodsId);

}
