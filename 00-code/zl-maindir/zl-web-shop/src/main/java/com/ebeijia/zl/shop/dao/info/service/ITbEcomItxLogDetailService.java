package com.ebeijia.zl.shop.dao.info.service;

import com.ebeijia.zl.shop.dao.info.domain.TbEcomItxLogDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 * tb_ecom_itx_log_detail Service 接口类
 *
 * @User J
 * @Date 2019-01-10
 */
public interface ITbEcomItxLogDetailService extends IService<TbEcomItxLogDetail> {

    String getPhoneChargeProvider();
}
