package com.ebeijia.zl.shop.dao.info.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.shop.dao.info.domain.TbEcomItxLogDetail;
import com.ebeijia.zl.shop.dao.info.mapper.TbEcomItxLogDetailMapper;
import com.ebeijia.zl.shop.dao.info.service.ITbEcomItxLogDetailService;
import org.springframework.stereotype.Service;


/**
 *
 * tb_ecom_itx_log_detail Service 实现类
 *
 * @User J
 * @Date 2019-01-10
 */
@Service
public class TbEcomItxLogDetailService extends ServiceImpl<TbEcomItxLogDetailMapper, TbEcomItxLogDetail> implements ITbEcomItxLogDetailService{

    @Override
    public String getPhoneChargeProvider(String id){
        return baseMapper.getPhoneChargeProvider(id);
    }
}
