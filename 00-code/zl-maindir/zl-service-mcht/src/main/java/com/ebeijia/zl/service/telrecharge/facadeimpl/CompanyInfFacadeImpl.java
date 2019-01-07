package com.ebeijia.zl.service.telrecharge.facadeimpl;

import java.util.List;

import com.ebeijia.zl.facade.telrecharge.domain.CompanyBillingTypeInf;
import com.ebeijia.zl.service.telrecharge.service.CompanyBillingTypeInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.service.telrecharge.service.CompanyInfService;
import com.github.pagehelper.PageInfo;

@Configuration
@com.alibaba.dubbo.config.annotation.Service(interfaceName="companyInfFacade")
public class CompanyInfFacadeImpl implements CompanyInfFacade {

	@Autowired
	private CompanyInfService companyInfService;

	@Autowired
	private CompanyBillingTypeInfService companyBillingTypeInfService;

	@Override
	public CompanyInf getCompanyInfById(String companyId) {
		return companyInfService.getById(companyId);
	}

	@Override
	public boolean insertCompanyInf(CompanyInf companyInf) {
		return companyInfService.save(companyInf);
	}

	@Override
	public boolean updateCompanyInf(CompanyInf companyInf) {
		return companyInfService.updateById(companyInf);
	}

	@Override
	public boolean deleteCompanyInf(CompanyInf companyInf) {
		return companyInfService.removeById(companyInf.getCompanyId());
	}

	@Override
	public List<CompanyInf> getCompanyInfList(CompanyInf companyInf) {
		return companyInfService.getCompanyInfList(companyInf);
	}

	@Override
	public PageInfo<CompanyInf> getCompanyInfList(int startNum, int pageSize, CompanyInf companyInf) {
		return companyInfService.getCompanyInfList(startNum, pageSize, companyInf);
	}

	@Override
	public CompanyInf getCompanyInfByLawCode(String lawCode) {
		return companyInfService.getCompanyInfByLawCode(lawCode);
	}

	@Override
	public CompanyInf getCompanyInfByIsPlatform(String isPlatform) {
		return companyInfService.getCompanyInfByIsPlatform(isPlatform);
	}

	@Override
	public CompanyBillingTypeInf getCompanyBillingTypeInfById(String id) {
		return companyBillingTypeInfService.getById(id);
	}

	@Override
	public List<CompanyBillingTypeInf> getCompanyBillingTypeInfList(CompanyBillingTypeInf companyBillingTypeInf) {
		return companyBillingTypeInfService.getCompanyBillingTypeInfList(companyBillingTypeInf);
	}

	@Override
	public PageInfo<CompanyBillingTypeInf> getCompanyBillingTypeInfPage(int startNum, int pageSize, CompanyBillingTypeInf companyBillingTypeInf) {
		return companyBillingTypeInfService.getCompanyBillingTypeInfPage(startNum, pageSize, companyBillingTypeInf);
	}

	@Override
	public CompanyBillingTypeInf getCompanyBillingTypeInfByBIdAndCompanyId(CompanyBillingTypeInf companyBillingTypeInf) {
		return companyBillingTypeInfService.getCompanyBillingTypeInfByBIdAndCompanyId(companyBillingTypeInf);
	}

	@Override
	public boolean insertCompanyBillingTypeInf(CompanyBillingTypeInf companyBillingTypeInf) {
		return companyBillingTypeInfService.save(companyBillingTypeInf);
	}

	@Override
	public boolean updateCompanyBillingTypeInf(CompanyBillingTypeInf companyBillingTypeInf) {
		return companyBillingTypeInfService.updateById(companyBillingTypeInf);
	}

	@Override
	public boolean deleteCompanyBillingTypeInf(String id) {
		return companyBillingTypeInfService.removeById(id);
	}

}
