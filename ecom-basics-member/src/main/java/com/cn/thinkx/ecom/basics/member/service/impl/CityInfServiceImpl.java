package com.cn.thinkx.ecom.basics.member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.member.domain.CityInf;
import com.cn.thinkx.ecom.basics.member.mapper.CityInfMapper;
import com.cn.thinkx.ecom.basics.member.service.CityInfService;

@Service("cityInfService")
public class CityInfServiceImpl implements CityInfService {

	@Autowired
	private CityInfMapper cityInfMapper;

	@Override
	public List<CityInf> findCityInfList(CityInf entity) {
		return cityInfMapper.findCityInfList(entity);
	}
}
