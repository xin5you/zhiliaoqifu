package com.ebeijia.zl.web.cms.goodsmanage.service;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomSpecValues;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomSpecification;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

public interface GoodsManageService {

    /**
     * 商品规格列表（分页）
     * @param startNum
     * @param pageSize
     * @param entity
     * @return
     */
    PageInfo<TbEcomSpecification> getGodosSpecListPage(int startNum, int pageSize, TbEcomSpecification entity);

    /**
     * 商品规格新增
     * @param entity
     * @param specImgFile
     * @return
     */
    BaseResult<Object> addGoodsSpec(TbEcomSpecification entity, MultipartFile specImgFile);

    /**
     * 商品规格编辑
     * @param entity
     * @param specImgFile
     * @return
     */
    BaseResult<Object> editGoodsSpec(TbEcomSpecification entity, MultipartFile specImgFile);

    /**
     * 商品规格删除
     * @param entity
     * @return
     */
    BaseResult<Object> deleteGoodsSpec(TbEcomSpecification entity);

    /**
     * 商品规格值列表（分页）
     * @param startNum
     * @param pageSize
     * @param entity
     * @return
     */
    PageInfo<TbEcomSpecValues> getGodosSpecValuesListPage(int startNum, int pageSize, TbEcomSpecValues entity);

    /**
     * 商品规格值新增
     * @param entity
     * @param specImageFile
     * @return
     */
    BaseResult<Object> addGoodsSpecValues(TbEcomSpecValues entity, MultipartFile specImageFile);

    /**
     * 商品规格值编辑
     * @param entity
     * @param specImageFile
     * @return
     */
    BaseResult<Object> editGoodsSpecValues(TbEcomSpecValues entity, MultipartFile specImageFile);

    /**
     * 商品规格删除
     * @param entity
     * @return
     */
    BaseResult<Object> deleteGoodsSpecValues(TbEcomSpecValues entity);
}
