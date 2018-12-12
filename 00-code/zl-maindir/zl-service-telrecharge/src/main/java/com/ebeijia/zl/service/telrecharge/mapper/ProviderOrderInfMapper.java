package com.ebeijia.zl.service.telrecharge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;

@Mapper
public interface ProviderOrderInfMapper extends BaseMapper<ProviderOrderInf> {

	ProviderOrderInf getOrderInfByChannelOrderId(String channelOrderId);
	
	/**
	 * 查找updateTime 10分钟以内，1分钟以上的订单
	 * @param ProviderOrderInf
	 * @return
	 */
	List<ProviderOrderInf> getListByTimer(ProviderOrderInf providerOrderInf);
}
