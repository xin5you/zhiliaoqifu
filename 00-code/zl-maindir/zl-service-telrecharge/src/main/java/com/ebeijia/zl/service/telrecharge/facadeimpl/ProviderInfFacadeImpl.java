package com.ebeijia.zl.service.telrecharge.facadeimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import com.ebeijia.zl.service.telrecharge.enums.TelRechargeConstants;
import com.ebeijia.zl.service.telrecharge.enums.TelRechargeConstants.providerDefaultRoute;
import com.ebeijia.zl.service.telrecharge.service.ProviderInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Configuration  
@com.alibaba.dubbo.config.annotation.Service(interfaceName="providerInfFacade")
public class ProviderInfFacadeImpl implements ProviderInfFacade {

	@Autowired
	private ProviderInfService providerInfService;

	@Override
	public ProviderInf getProviderInfById(String providerId) throws Exception {
		return providerInfService.getById(providerId);
	}

	@Override
	public boolean saveProviderInf(ProviderInf providerInf) throws Exception {
		if(providerDefaultRoute.DefaultRoute0.getCode().equals(providerInf.getDefaultRoute())){
			providerInfService.updateByDefaultRoute();
		}
		return providerInfService.save(providerInf);
	}

	@Override
	public boolean updateProviderInf(ProviderInf ProviderInf) throws Exception {
		ProviderInf tpInf = this.getProviderInfById(ProviderInf.getProviderId());
		if(providerDefaultRoute.DefaultRoute0.getCode().equals(ProviderInf.getDefaultRoute())){
			if(!providerDefaultRoute.DefaultRoute0.getCode().equals(tpInf.getDefaultRoute())){	//修改的供应商数据原来不是默认路由
				providerInfService.updateByDefaultRoute();
			}
		}
		tpInf.setProviderName(ProviderInf.getProviderName());
		tpInf.setAppUrl(ProviderInf.getAppUrl());
		tpInf.setAppSecret(ProviderInf.getAppSecret());
		tpInf.setAccessToken(ProviderInf.getAccessToken());
		tpInf.setDefaultRoute(ProviderInf.getDefaultRoute());
		tpInf.setProviderRate(ProviderInf.getProviderRate());
		tpInf.setOperSolr(ProviderInf.getOperSolr());
		tpInf.setRemarks(ProviderInf.getRemarks());
		tpInf.setUpdateUser(ProviderInf.getUpdateUser());
		return providerInfService.updateById(tpInf);
	}

	@Override
	public boolean deleteProviderInfById(String providerId) throws Exception {
		return providerInfService.removeById(providerId);
	}

	@Override
	public List<ProviderInf> getProviderInfList(ProviderInf providerInf) throws Exception {
		return providerInfService.getProviderInfList(providerInf);
	}

	/**
	 * 供应商信息分页
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param ProviderInf
	 * @return
	 * @throws Exception
	 */
	public PageInfo<ProviderInf> getProviderInfPage(int startNum, int pageSize, ProviderInf providerInf)
			throws Exception {
		PageHelper.startPage(startNum, pageSize);
		List<ProviderInf> ProviderInfList = getProviderInfList(providerInf);
		for (ProviderInf ProviderInf2 : ProviderInfList) {
			if(!StringUtil.isNullOrEmpty(ProviderInf2.getDefaultRoute()))
				ProviderInf2.setDefaultRoute(TelRechargeConstants.providerDefaultRoute.findByCode(ProviderInf2.getDefaultRoute()));
		}
		PageInfo<ProviderInf> ProviderInfPage = new PageInfo<ProviderInf>(ProviderInfList);
		return ProviderInfPage;
	}
}
