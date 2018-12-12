package com.ebeijia.zl.service.telrecharge.facadeimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.service.telrecharge.service.CompanyInfService;
import com.github.pagehelper.PageInfo;

@com.alibaba.dubbo.config.annotation.Service(version = "1.0.0")
@Service
public class CompanyInfFacadeImpl implements CompanyInfFacade {

	@Autowired
	private  CompanyInfService companyInfService;

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


}
