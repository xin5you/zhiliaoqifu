package com.cn.thinkx.ecom.basics.goods.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.goods.domain.Esetting;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface EsettingMapper extends BaseDao<Esetting> {

	Esetting getSettingByEcomCode(String ecomCode);
}
