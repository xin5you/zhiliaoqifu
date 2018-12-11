package com.cn.thinkx.wecard.facade.telrecharge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.thinkx.wecard.facade.telrecharge.domain.RetailChnlInf;

@Mapper
public interface RetailChnlInfMapper extends BaseMapper<RetailChnlInf> {

	RetailChnlInf getRetailChnlInfByMchntCode(String mchntCode);
	
	List<RetailChnlInf> getList(RetailChnlInf retailChnlInf);
}
