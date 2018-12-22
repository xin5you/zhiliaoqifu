package com.ebeijia.zl.service.telrecharge.service.impl;

import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlAreaInf;
import com.ebeijia.zl.service.telrecharge.mapper.RetailChnlAreaInfMapper;
import com.ebeijia.zl.service.telrecharge.service.RetailChnlAreaInfService;

/**
 *
 * 分销商话费地区维护表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Service
public class RetailChnlAreaInfServiceImpl extends ServiceImpl<RetailChnlAreaInfMapper, RetailChnlAreaInf> implements RetailChnlAreaInfService{


    @Override
    public boolean save(RetailChnlAreaInf entity) {
        entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setLockVersion(0);
        return super.save(entity);
    }

    @Override
    public boolean updateById(RetailChnlAreaInf entity){
        entity.setUpdateTime(System.currentTimeMillis());
        return super.updateById(entity);
    }

}
