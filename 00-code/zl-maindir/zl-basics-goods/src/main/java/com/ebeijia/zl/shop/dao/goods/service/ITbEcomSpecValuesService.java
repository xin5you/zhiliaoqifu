package com.ebeijia.zl.shop.dao.goods.service;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomSpecValues;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * 商品规格明细 Service 接口类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
public interface ITbEcomSpecValuesService extends IService<TbEcomSpecValues> {

    /**
     * 查询商品规格值列表（高级查询）
     * @param entity
     * @return
     */
    List<TbEcomSpecValues> getGoodsSpecValuesList(TbEcomSpecValues entity);

    /**
     * 根据specOrder查询规格值信息
     * @param entity
     * @return
     */
    TbEcomSpecValues getGoodsSpecValueBySpecOrder(TbEcomSpecValues entity);

    /**
     * 根据SpecValueName查询规格值信息
     * @param entity
     * @return
     */
    TbEcomSpecValues getGoodsSpecValueBySpecValueName(TbEcomSpecValues entity);
}
