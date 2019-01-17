package com.ebeijia.zl.service.telrecharge.facadeimpl;

import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.ebeijia.zl.facade.telrecharge.service.ProviderOrderInfFacade;
import com.ebeijia.zl.service.telrecharge.enums.TelRechargeConstants;
import com.ebeijia.zl.service.telrecharge.service.ProviderOrderInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration  
@com.alibaba.dubbo.config.annotation.Service(interfaceName="providerOrderInfFacade")
public class ProviderOrderInfFacadeImpl implements ProviderOrderInfFacade {

	@Autowired
	private ProviderOrderInfService providerOrderInfService;

	@Override
	public ProviderOrderInf getProviderOrderInfById(String regOrderId) throws Exception {
		return providerOrderInfService.getById(regOrderId);
	}

	@Override
	public boolean saveProviderOrderInf(ProviderOrderInf providerOrderInf) throws Exception {
		return providerOrderInfService.save(providerOrderInf);
	}

	@Override
	public boolean updateProviderOrderInf(ProviderOrderInf providerOrderInf) throws Exception {
		return providerOrderInfService.updateById(providerOrderInf);
	}

	@Override
	public boolean deleteProviderOrderInfById(String regOrderId) throws Exception {
		return providerOrderInfService.removeById(regOrderId);
	}

	@Override
	public List<ProviderOrderInf> getProviderOrderInfList(ProviderOrderInf providerOrderInf)
			throws Exception {
		return providerOrderInfService.getProviderOrderInfList(providerOrderInf);
	}

	@Override
	public ProviderOrderInf getOrderInfByChannelOrderId(String channelOrderId) throws Exception {
		return providerOrderInfService.getOrderInfByChannelOrderId(channelOrderId);
	}

	/**
	 * 供应商订单分页
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param ProviderOrderInf
	 * @return
	 * @throws Exception
	 */
	public PageInfo<ProviderOrderInf> getProviderOrderInfPage(int startNum, int pageSize,
			ProviderOrderInf ProviderOrderInf) throws Exception {
		PageHelper.startPage(startNum, pageSize);
		List<ProviderOrderInf> ProviderOrderInfList = getProviderOrderInfList(ProviderOrderInf);
		for (ProviderOrderInf ProviderOrderInf2 : ProviderOrderInfList) {
			if(!StringUtil.isNullOrEmpty(ProviderOrderInf2.getPayState()))
				ProviderOrderInf2.setPayState(TelRechargeConstants.providerOrderPayState.findByCode(ProviderOrderInf2.getPayState()));
			if(!StringUtil.isNullOrEmpty(ProviderOrderInf2.getRechargeState()))
				ProviderOrderInf2.setRechargeState(TelRechargeConstants.providerOrderRechargeState.findByCode(ProviderOrderInf2.getRechargeState()));
		}
		PageInfo<ProviderOrderInf> ProviderOrderInfPage = new PageInfo<ProviderOrderInf>(
				ProviderOrderInfList);
		return ProviderOrderInfPage;
	}
	
	/**
	 * 查找updateTime 10分钟以内，1分钟以上的订单
	 * @param providerOrderInf
	 * @return
	 */
	public List<ProviderOrderInf> getListByTimer(ProviderOrderInf providerOrderInf){
		return providerOrderInfService.getListByTimer(providerOrderInf);
	}
}
