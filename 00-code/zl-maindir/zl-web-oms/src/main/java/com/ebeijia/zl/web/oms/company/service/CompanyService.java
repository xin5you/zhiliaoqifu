package com.ebeijia.zl.web.oms.company.service;

import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface CompanyService {

	/**
	 * 企业开户
	 * @param req
	 * @return
	 */
	Map<String, Object> openAccountCompany(HttpServletRequest req);

	/**
	 * 平台转账至企业
	 * @param req
	 * @return
	 */
	Map<String, Object> addCompanyTransferCommit(HttpServletRequest req);

	/**
	 * 更新平台转账至企业的转账状态
	 * @param req
	 * @return
	 */
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

	/**
	 * 新增平台上账(转账至分销商)
	 * @param req
	 * @param evidenceUrlFile
	 * @return
	 */
	Map<String, Object> addPlatformTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile);

	/**
	 * 编辑平台上账信息（转账至分销商）
	 * @param req
	 * @param evidenceUrlFile
	 * @return
	 */
	Map<String, Object> editPlatformTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile);

	/**
	 * 新增平台上账提交（调用充值接口）
	 * @param req
	 * @return
	 */
	Map<String, Object> addPlatformTransferCommit(HttpServletRequest req);

	/**
	 * 平台打款至分销商账户
	 * @param req
	 * @return
	 */
	Map<String, Object> updatePlatformRemitStatCommit(HttpServletRequest req);

}
