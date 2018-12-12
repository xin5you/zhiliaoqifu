package com.ebeijia.zl.basics.goods.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ebeijia.zl.basics.goods.domain.Esetting;
import com.ebeijia.zl.common.core.mapper.BaseDao;

@Mapper
public interface EsettingMapper extends BaseDao<Esetting> {

	Esetting getSettingByEcomCode(String ecomCode);
}
