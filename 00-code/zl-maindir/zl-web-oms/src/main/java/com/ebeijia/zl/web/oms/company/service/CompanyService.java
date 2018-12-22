package com.ebeijia.zl.web.oms.company.service;

import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;

public interface CompanyService {

	public int openAccountCompany(HttpServletRequest req);

	ModelMap addCompanyTransferCommit(HttpServletRequest req);

	ModelMap updateCompanyTransferStat(HttpServletRequest req);
}
