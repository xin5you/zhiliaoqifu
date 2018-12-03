package com.cn.thinkx.ecom.basics.goods.mapper;

import com.cn.thinkx.ecom.basics.goods.domain.Esetting;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EsettingMapper extends BaseDao<Esetting> {

	Esetting getSettingByEcomCode(String ecomCode);
}
