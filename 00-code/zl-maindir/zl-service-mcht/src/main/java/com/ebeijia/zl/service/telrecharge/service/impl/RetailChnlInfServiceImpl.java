package com.ebeijia.zl.service.telrecharge.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlAreaInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.service.telrecharge.mapper.RetailChnlInfMapper;
import com.ebeijia.zl.service.telrecharge.service.RetailChnlInfService;

/**
 *
 * 分销商信息表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Service
public class RetailChnlInfServiceImpl extends ServiceImpl<RetailChnlInfMapper, RetailChnlInf> implements RetailChnlInfService{

	@Override
	public boolean save(RetailChnlInf entity) {
		entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		entity.setCreateTime(System.currentTimeMillis());
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setLockVersion(0);
		return super.save(entity);
	}

	@Override
	public boolean updateById(RetailChnlInf entity){
		entity.setUpdateTime(System.currentTimeMillis());
		return super.updateById(entity);
	}
	
	public List<RetailChnlInf> getList(RetailChnlInf retailChnlInf){
		return baseMapper.getList(retailChnlInf);
	}

	@Override
	public RetailChnlInf getRetailChnlInfByLawCode(String lawCode) {
		return baseMapper.getRetailChnlInfByLawCode(lawCode);
	}

	@Override
	public RetailChnlInf getRetailChnlInfByChannelName(String channelName) throws Exception {
		QueryWrapper<RetailChnlInf> queryWrapper = new QueryWrapper<RetailChnlInf>();
		queryWrapper.eq("channel_name", channelName);
		queryWrapper.eq("data_stat",DataStatEnum.TRUE_STATUS.getCode());
		return baseMapper.selectOne(queryWrapper);
	}
}
