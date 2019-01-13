package com.ebeijia.zl.shop.dao.goods.service;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * 商品详情表 Service 接口类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
public interface ITbEcomGoodsDetailService extends IService<TbEcomGoodsDetail> {

    /**
     * 查询商品详情信息（多条件查询）
     * @param ecomGoodsDetail
     * @return
     */
    List<TbEcomGoodsDetail> getGoodsDetailList(TbEcomGoodsDetail ecomGoodsDetail);

    /**
     * 根据goodsId查询商品详情信息
     * @param goodsId
     * @return
     */
    TbEcomGoodsDetail getGoodsDetailByGoodsId(String goodsId);
}
