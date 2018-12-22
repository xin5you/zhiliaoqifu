package com.ebeijia.zl.shop.service.category.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.core.redis.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsCategory;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsCategoryService;
import com.ebeijia.zl.shop.service.category.ICategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private ITbEcomGoodsCategoryService goodsCategoryDao;

    @Autowired
    JedisUtilsWithNamespace jedis;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<TbEcomGoodsCategory> listCategory(String type) {
        List<TbEcomGoodsCategory> list = goodsCategoryDao.list();
        return list;
    }

    @Override
    public List<TbEcomGoodsCategory> listCategory(String billingType, String parent) {
        TbEcomGoodsCategory category = new TbEcomGoodsCategory();
        if (!StringUtils.isEmpty(parent)) {
            category.setParentId(parent);
        }
        QueryWrapper<TbEcomGoodsCategory> query = new QueryWrapper<>(category);
        if (!StringUtils.isEmpty(billingType)) {
            SpecAccountTypeEnum type = SpecAccountTypeEnum.findByBId(billingType);
            if (type != null) {
                //TODO 优化sql
                query.inSql("cat_id", "select cat_id from tb_ecom_categroy_billing where b_id=\"" + type.getbId()+"\"");
            }
        }
        List<TbEcomGoodsCategory> list = goodsCategoryDao.list(query);
        return list;
    }


}
