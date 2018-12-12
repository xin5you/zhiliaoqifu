package com.ebeijia.zl.service.telrecharge.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlProductInf;

@Mapper
public interface RetailChnlProductInfMapper extends BaseMapper<RetailChnlProductInf> {
	
	
   List<RetailChnlProductInf> getList(RetailChnlProductInf retailChnlProductInf);
   

	RetailChnlProductInf getProductRateByMaps(Map maps);

	/**
	 * 查看分销商的折扣率
	 * 
	 * @param id
	 * @return
	 */
	RetailChnlProductInf getChannelProductByItemId(String id);

	/**
	 * 通过分销商id获取手机充值产品
	 * 
	 * @param channelId
	 * @return
	 */
	List<RetailChnlProductInf> getChannelProductListByChannelId(String channelId);
}
