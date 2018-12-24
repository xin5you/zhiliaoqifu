package com.ebeijia.zl.service.telrecharge.facadeimpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlProductInf;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlProductInfFacade;
import com.ebeijia.zl.service.telrecharge.enums.TelRechargeConstants;
import com.ebeijia.zl.service.telrecharge.service.RetailChnlProductInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Configuration  
@com.alibaba.dubbo.config.annotation.Service(interfaceName="retailChnlProductInfFacade")
public class RetailChnlProductInfFacadeImpl implements RetailChnlProductInfFacade {

	@Autowired
	private RetailChnlProductInfService retailChnlProductInfService;

	@Override
	public RetailChnlProductInf getRetailChnlProductInfById(String productId) throws Exception {
		return retailChnlProductInfService.getById(productId);
	}

	@Override
	public boolean saveRetailChnlProductInf(RetailChnlProductInf retailChnlProductInf) throws Exception {
		return retailChnlProductInfService.save(retailChnlProductInf);
	}

	/**
	 * 保存对象返回ID
	 * 
	 * @param retailChnlProductInf
	 * @return
	 * @throws Exception
	 */
	public String saveTelChannelProductForId(RetailChnlProductInf retailChnlProductInf) throws Exception {
		boolean oper = retailChnlProductInfService.save(retailChnlProductInf);
		if (oper) {
			return retailChnlProductInf.getProductId();
		} else {
			return "";
		}
	}

	@Override
	public boolean updateRetailChnlProductInf(RetailChnlProductInf retailChnlProductInf) throws Exception {
		return retailChnlProductInfService.updateById(retailChnlProductInf);
	}

	@Override
	public boolean deleteRetailChnlProductInfById(String productId) throws Exception {
		return retailChnlProductInfService.removeById(productId);
	}

	/**
	 * 获取分销商产品的折扣
	 * 
	 * @return maps -->productId:产品编号, operId:运营商，productType: 类型，
	 *         areaName:地区名称，productAmt:产品面额（3位小数）
	 */
	public RetailChnlProductInf getProductRateByMaps(Map maps) {
		return retailChnlProductInfService.getProductRateByMaps(maps);
	}

	@Override
	public List<RetailChnlProductInf> getRetailChnlProductInfList(RetailChnlProductInf RetailChnlProductInf)
			throws Exception {
		return retailChnlProductInfService.getList(RetailChnlProductInf);
	}

	@Override
	public PageInfo<RetailChnlProductInf> getRetailChnlProductInfPage(int startNum, int pageSize,
			RetailChnlProductInf RetailChnlProductInf) throws Exception {
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

	@Override
	public List<RetailChnlProductInf> getChannelProductListByChannelId(String channelId) throws Exception {
		return retailChnlProductInfService.getChannelProductListByChannelId(channelId);
	}

	@Override
	public RetailChnlProductInf getChannelProductByItemId(String id) throws Exception {
		return retailChnlProductInfService.getChannelProductByItemId(id);
	}
}
