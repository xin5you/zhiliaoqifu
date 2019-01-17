package com.ebeijia.zl.shop.dao.info.mapper;

import com.ebeijia.zl.shop.dao.info.domain.TbEcomItxLogDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * tb_ecom_itx_log_detail Mapper 接口
 *
 * @User J
 * @Date 2019-01-10
 */
@Mapper
public interface TbEcomItxLogDetailMapper extends BaseMapper<TbEcomItxLogDetail> {

    @Select("SELECT provider_name FROM tb_provider_inf WHERE  provider_id='916438f8-3acc-4242-a648-90902455555'")
    String getPhoneChargeProvider();
}
