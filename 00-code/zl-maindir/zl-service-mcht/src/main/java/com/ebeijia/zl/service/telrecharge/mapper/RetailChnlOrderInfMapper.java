package com.ebeijia.zl.service.telrecharge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;

@Mapper
public interface RetailChnlOrderInfMapper extends BaseMapper<RetailChnlOrderInf> {

	RetailChnlOrderInf getRetailChnlOrderInfByOuterId(@Param("outerId")String outerId,@Param("channelId")String channelId) ;
	
	List<RetailChnlOrderInf> getRetailChnlOrderInfList(RetailChnlOrderInf order);
	
	List<RetailChnlOrderInf> getList(RetailChnlOrderInf order);
	
	RetailChnlOrderInf getRetailChnlOrderInfCount(RetailChnlOrderInf order);
	
}
