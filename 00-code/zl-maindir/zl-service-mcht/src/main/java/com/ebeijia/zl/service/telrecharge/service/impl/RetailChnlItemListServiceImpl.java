package com.ebeijia.zl.service.telrecharge.service.impl;

import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlItemList;
import com.ebeijia.zl.service.telrecharge.mapper.RetailChnlItemListMapper;
import com.ebeijia.zl.service.telrecharge.service.RetailChnlItemListService;

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


	@Override
	public boolean save(RetailChnlItemList entity) {
		entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		entity.setCreateTime(System.currentTimeMillis());
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setLockVersion(0);
		return super.save(entity);
	}

	@Override
	public boolean updateById(RetailChnlItemList entity){
		entity.setUpdateTime(System.currentTimeMillis());
		return super.updateById(entity);
	}
	
	public int deleteByProductId(String productId){
		 return retailChnlItemListMapper.deleteByProductId(productId);
	 }
}
