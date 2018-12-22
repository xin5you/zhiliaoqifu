package com.ebeijia.zl.service.telrecharge.service.impl;

import java.util.List;
import java.util.Map;

import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlProductInf;
import com.ebeijia.zl.service.telrecharge.enums.TelRechargeConstants;
import com.ebeijia.zl.service.telrecharge.mapper.RetailChnlProductInfMapper;
import com.ebeijia.zl.service.telrecharge.service.RetailChnlProductInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 *
 * 分销商充值产品管理表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Service
public class RetailChnlProductInfServiceImpl extends ServiceImpl<RetailChnlProductInfMapper, RetailChnlProductInf> implements RetailChnlProductInfService{
	


	@Autowired
	private RetailChnlProductInfMapper retailChnlProductInfMapper;

	@Override
	public boolean save(RetailChnlProductInf entity) {
		entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		entity.setCreateTime(System.currentTimeMillis());
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setLockVersion(0);
		return super.save(entity);
	}

	@Override
	public boolean updateById(RetailChnlProductInf entity){
		entity.setUpdateTime(System.currentTimeMillis());
		return super.updateById(entity);
	}
	/**
	 * 保存对象返回ID
	 * 
	 * @param retailChnlProductInf
	 * @return
	 * @throws Exception
	 */
	public String saveRetailChnlProductForId(RetailChnlProductInf retailChnlProductInf) throws Exception {
		int oper = retailChnlProductInfMapper.insert(retailChnlProductInf);
		if (oper > 0) {
			return retailChnlProductInf.getProductId();
		} else {
			return "";
		}
	}




	/**
	 * 获取分销商产品的折扣
	 * 
	 * @return maps -->productId:产品编号, operId:运营商，productType: 类型，
	 *         areaName:地区名称，productAmt:产品面额（3位小数）
	 */
	public RetailChnlProductInf getProductRateByMaps(Map maps) {
		return retailChnlProductInfMapper.getProductRateByMaps(maps);
	}

	public List<RetailChnlProductInf> getRetailChnlProductInfList(RetailChnlProductInf retailChnlProductInf) {
		return retailChnlProductInfMapper.getList(retailChnlProductInf);
	}

	
	public PageInfo<RetailChnlProductInf> getRetailChnlProductInfPage(int startNum, int pageSize,RetailChnlProductInf RetailChnlProductInf){
		PageHelper.startPage(startNum, pageSize);
		List<RetailChnlProductInf> RetailChnlProductInfList = getRetailChnlProductInfList(RetailChnlProductInf);
		for (RetailChnlProductInf RetailChnlProductInf2 : RetailChnlProductInfList) {
			if (!StringUtil.isNullOrEmpty(RetailChnlProductInf2.getOperId()))
				RetailChnlProductInf2
						.setOperId(TelRechargeConstants.OperatorType.findByCode(RetailChnlProductInf2.getOperId()));
			if (!StringUtil.isNullOrEmpty(RetailChnlProductInf2.getProductType()))
				RetailChnlProductInf2.setProductType(
						TelRechargeConstants.ChannelProductProType.findByCode(RetailChnlProductInf2.getProductType()));
			if (!StringUtil.isNullOrEmpty(RetailChnlProductInf2.getAreaFlag()))
				RetailChnlProductInf2.setAreaFlag(
						TelRechargeConstants.ChannelProductAreaFlag.findByCode(RetailChnlProductInf2.getAreaFlag()));
		}
		PageInfo<RetailChnlProductInf> telProviderInfPage = new PageInfo<RetailChnlProductInf>(
				RetailChnlProductInfList);
		return telProviderInfPage;
	}

	
	public List<RetailChnlProductInf> getChannelProductListByChannelId(String channelId) {
		return retailChnlProductInfMapper.getChannelProductListByChannelId(channelId);
	}

	
	public RetailChnlProductInf getChannelProductByItemId(String id) {
		return retailChnlProductInfMapper.getChannelProductByItemId(id);
	}




	@Override
	public List<RetailChnlProductInf> getList(RetailChnlProductInf retailChnlProductInf) {
		return retailChnlProductInfMapper.getList(retailChnlProductInf);
	}


}
