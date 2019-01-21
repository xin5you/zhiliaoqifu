package com.ebeijia.zl.web.oms.company.service;

import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface CompanyService {

	public int openAccountCompany(HttpServletRequest req);

	Map<String, Object> addCompanyTransferCommit(HttpServletRequest req);

	Map<String, Object> updateCompanyTransferStat(HttpServletRequest req);

	/**
	 * 编辑企业收款金额
	 * @param req
	 * @return
	 */
	Map<String, Object> editCompanyInAmtCommit(HttpServletRequest req);

	/**
	 * 新增企业信息
	 * @param companyInf
	 * @return
	 */
	Map<String, Object> addCompanyInf(CompanyInf companyInf);

	/**
	 * 编辑企业信息
	 * @param companyInf
	 * @return
	 */
	Map<String, Object> editCompanyInf(CompanyInf companyInf);

}
