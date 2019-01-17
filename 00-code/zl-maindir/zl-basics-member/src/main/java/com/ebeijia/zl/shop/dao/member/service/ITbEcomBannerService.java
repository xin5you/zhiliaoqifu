package com.ebeijia.zl.shop.dao.member.service;

import com.ebeijia.zl.shop.dao.member.domain.TbEcomBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * Banner信息 Service 接口类
 *
 * @User J
 * @Date 2018-12-07
 */
public interface ITbEcomBannerService extends IService<TbEcomBanner> {

    /**
     * 根据条件查询banner信息
     * @param entity
     * @return
     */
    List<TbEcomBanner> getBannerList(TbEcomBanner entity);

    /**
     * 根据排序号查询banner信息
     * @param sort
     * @return
     */
    TbEcomBanner getBannerBySort(Integer sort);
}
