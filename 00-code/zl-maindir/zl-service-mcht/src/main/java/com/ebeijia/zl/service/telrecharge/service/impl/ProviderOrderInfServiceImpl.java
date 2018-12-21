package com.ebeijia.zl.service.telrecharge.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.ebeijia.zl.service.telrecharge.mapper.ProviderOrderInfMapper;
import com.ebeijia.zl.service.telrecharge.service.ProviderOrderInfService;

/**
 *
 * 供应商订单明细表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Service()
public class ProviderOrderInfServiceImpl extends ServiceImpl<ProviderOrderInfMapper, ProviderOrderInf> implements ProviderOrderInfService{

	@Autowired
	private ProviderOrderInfMapper providerOrderInfMapper;

	@Override
	public List<ProviderOrderInf> getProviderOrderInfList(ProviderOrderInf providerOrderInf) {
		QueryWrapper<ProviderOrderInf> queryWrapper=new QueryWrapper<ProviderOrderInf>(providerOrderInf);
		return this.list(queryWrapper);
	}
	
	/**
	 * 查找updateTime 10分钟以内，1分钟以上的订单
	 * @param providerOrderInf
	 * @return
	 */
	public List<ProviderOrderInf> getListByTimer(ProviderOrderInf providerOrderInf){
		return providerOrderInfMapper.getListByTimer(providerOrderInf);
	}


	@Override
	public ProviderOrderInf getTelOrderInfByChannelOrderId(String OrderId) {
		return providerOrderInfMapper.getOrderInfByChannelOrderId(OrderId);
	}

}
