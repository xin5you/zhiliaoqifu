package com.ebeijia.zl.shop.dao.goods.service;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsBilling;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 * 商品专项账户关联表 Service 接口类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
public interface ITbEcomGoodsBillingService extends IService<TbEcomGoodsBilling> {

    /**
     * 根据goodsId查询商品关联表信息
     * @param goodsId
     * @return
     */
    TbEcomGoodsBilling getGoodsBillingByGoodsId(String goodsId);

    /**
     * 新增商品专项类型关联表
     * @param ecomGoodsBilling
     * @return
     */
    int insert(TbEcomGoodsBilling ecomGoodsBilling);
}
