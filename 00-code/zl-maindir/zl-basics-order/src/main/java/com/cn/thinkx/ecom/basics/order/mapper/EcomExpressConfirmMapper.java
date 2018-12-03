package com.cn.thinkx.ecom.basics.order.mapper;

import com.cn.thinkx.ecom.basics.order.domain.EcomExpressConfirm;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EcomExpressConfirmMapper extends BaseDao<EcomExpressConfirm> {

	/**
	 * 通过退款申请id查询退货包裹确认信息
	 * 
	 * @param returnsId
	 * @return
	 */
	EcomExpressConfirm selectByReturnsId(String returnsId);
}
