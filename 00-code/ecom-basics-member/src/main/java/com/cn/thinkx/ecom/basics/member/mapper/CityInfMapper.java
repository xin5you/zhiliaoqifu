package com.cn.thinkx.ecom.basics.member.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.member.domain.CityInf;

@Mapper
public interface CityInfMapper {

	/**
	 * 查找城市
	 * 
	 * @param entity
	 * @return
	 */
	List<CityInf> findCityInfList(CityInf entity);

}