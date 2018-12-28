package com.ebeijia.zl.shop.dao.goods.service;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomSpecification;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * 商品规格表 Service 接口类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
public interface ITbEcomSpecificationService extends IService<TbEcomSpecification> {

    /**
     * 根据条件查询商品规格信息
     * @param ecomSpecification
     * @return
     */
    List<TbEcomSpecification> getGoodsSpecList(TbEcomSpecification ecomSpecification);
}
