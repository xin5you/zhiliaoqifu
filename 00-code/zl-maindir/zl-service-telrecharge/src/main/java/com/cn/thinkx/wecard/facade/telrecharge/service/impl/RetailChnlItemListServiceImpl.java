package com.cn.thinkx.wecard.facade.telrecharge.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.thinkx.wecard.facade.telrecharge.domain.RetailChnlItemList;
import com.cn.thinkx.wecard.facade.telrecharge.mapper.RetailChnlItemListMapper;
import com.cn.thinkx.wecard.facade.telrecharge.service.RetailChnlItemListService;

/**
 *
 * 分销商产品关联供应商商品表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Service
public class RetailChnlItemListServiceImpl extends ServiceImpl<RetailChnlItemListMapper, RetailChnlItemList> implements RetailChnlItemListService{

	
	@Autowired
	public RetailChnlItemListMapper retailChnlItemListMapper;
	
	
	public int deleteByProductId(String productId){
		 return retailChnlItemListMapper.deleteByProductId(productId);
	 }
}