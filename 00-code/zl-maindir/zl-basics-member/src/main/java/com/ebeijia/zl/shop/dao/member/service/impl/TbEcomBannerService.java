package com.ebeijia.zl.shop.dao.member.service.impl;

import com.ebeijia.zl.shop.dao.member.domain.TbEcomBanner;
import com.ebeijia.zl.shop.dao.member.mapper.TbEcomBannerMapper;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * Banner信息 Service 实现类
 *
 * @User J
 * @Date 2018-12-07
 */
@Service
public class TbEcomBannerService extends ServiceImpl<TbEcomBannerMapper, TbEcomBanner> implements ITbEcomBannerService{

    @Override
    public List<TbEcomBanner> getBannerList(TbEcomBanner entity) {
        return baseMapper.getBannerList(entity);
    }

    @Override
    public TbEcomBanner getBannerBySort(Integer sort) {
        return baseMapper.getBannerBySort(sort);
    }
}
