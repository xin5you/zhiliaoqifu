package com.ebeijia.zl.shop.service.goods.impl;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;
import com.ebeijia.zl.shop.service.goods.IGoodsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService implements IGoodsService {
    @Override
    public List<TbEcomGoods> listGoods(int catid, String orderby, int start, int limit) {
        return null;
    }

    @Override
    public TbEcomGoodsDetail goodDetail(String goodsId) {
        return null;
    }
}
