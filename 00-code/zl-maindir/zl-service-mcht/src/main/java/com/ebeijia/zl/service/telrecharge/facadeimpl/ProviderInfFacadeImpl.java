package com.ebeijia.zl.service.telrecharge.facadeimpl;

import java.util.List;

import com.ebeijia.zl.facade.telrecharge.domain.ProviderBillingTypeInf;
import com.ebeijia.zl.service.telrecharge.service.ProviderBillingTypeInfService;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import com.ebeijia.zl.service.telrecharge.enums.TelRechargeConstants;
import com.ebeijia.zl.service.telrecharge.enums.TelRechargeConstants.providerDefaultRoute;
import com.ebeijia.zl.service.telrecharge.service.ProviderInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.context.annotation.Configuration;

@Configuration
@com.alibaba.dubbo.config.annotation.Service()
public class ProviderInfFacadeImpl implements ProviderInfFacade {

	@Autowired
	private ProviderInfService providerInfService;

	@Autowired
	private ProviderBillingTypeInfService providerBillingTypeInfService;
	
	@Override
	public ProviderInf getProviderInfById(String providerId) throws Exception {
		return providerInfService.getById(providerId);
	}

	@Override
	public boolean saveProviderInf(ProviderInf providerInf) throws Exception {
		if(providerDefaultRoute.DefaultRoute0.getCode().equals(providerInf.getDefaultRoute())){
			ProviderInf p = new ProviderInf();
			p.setDefaultRoute(providerDefaultRoute.DefaultRoute0.getCode());
			List<ProviderInf> providerList = providerInfService.getProviderInfList(p);
			if (providerList != null && providerList.size() >= 1) {
				providerInfService.updateByDefaultRoute();
			}
		}
		return providerInfService.save(providerInf);
	}

	@Override
	public boolean updateProviderInf(ProviderInf ProviderInf) throws Exception {
		ProviderInf tpInf = this.getProviderInfById(ProviderInf.getProviderId());
		if(providerDefaultRoute.DefaultRoute0.getCode().equals(ProviderInf.getDefaultRoute())){
			providerInfService.updateByDefaultRoute();
		}
		tpInf.setProviderName(ProviderInf.getProviderName());
		tpInf.setLawCode(ProviderInf.getLawCode());
		tpInf.setAppUrl(ProviderInf.getAppUrl());
		tpInf.setAppSecret(ProviderInf.getAppSecret());
		tpInf.setAccessToken(ProviderInf.getAccessToken());
		tpInf.setDefaultRoute(ProviderInf.getDefaultRoute());
		tpInf.setProviderRate(ProviderInf.getProviderRate());
		tpInf.setOperSolr(ProviderInf.getOperSolr());
		tpInf.setIsOpen(ProviderInf.getIsOpen());
		tpInf.setRemarks(ProviderInf.getRemarks());
		tpInf.setUpdateUser(ProviderInf.getUpdateUser());
		tpInf.setUpdateTime(System.currentTimeMillis());
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

	@Override
	public ProviderInf getProviderInfByLawCode(String lawCode) throws Exception {
		return providerInfService.getProviderInfByLawCode(lawCode);
	}

	@Override
	public ProviderInf getProviderInfByOperSolr(Integer operSolr) throws Exception {
		return providerInfService.getProviderInfByOperSolr(operSolr);
	}

	@Override
	public ProviderBillingTypeInf getProviderBillingTypeInfById(String id) {
		return providerBillingTypeInfService.getById(id);
	}

	@Override
	public List<ProviderBillingTypeInf> getProviderBillingTypeInfList(ProviderBillingTypeInf providerBillingTypeInf) {
		return providerBillingTypeInfService.getProviderBillingTypeInfList(providerBillingTypeInf);
	}

	@Override
	public PageInfo<ProviderBillingTypeInf> getProviderBillingTypeInfPage(int startNum, int pageSize, ProviderBillingTypeInf providerBillingTypeInf) {
		return providerBillingTypeInfService.getProviderBillingTypeInfPage(startNum, pageSize, providerBillingTypeInf);
	}

	@Override
	public ProviderBillingTypeInf getProviderBillingTypeInfByBIdAndProviderId(ProviderBillingTypeInf providerBillingTypeInf) {
		return providerBillingTypeInfService.getProviderBillingTypeInfByBIdAndProviderId(providerBillingTypeInf);
	}

	@Override
	public boolean insertProviderBillingTypeInf(ProviderBillingTypeInf providerBillingTypeInf) {
		return providerBillingTypeInfService.save(providerBillingTypeInf);
	}

	@Override
	public boolean updateProviderBillingTypeInf(ProviderBillingTypeInf providerBillingTypeInf) {
		return providerBillingTypeInfService.updateById(providerBillingTypeInf);
	}

	@Override
	public boolean deleteProviderBillingTypeInf(String id) {
		return providerBillingTypeInfService.removeById(id);
	}


	public ProviderInf getProviderInfByProviderName(String providerName) {
		return providerInfService.getProviderInfByProviderName(providerName);
	}
}
