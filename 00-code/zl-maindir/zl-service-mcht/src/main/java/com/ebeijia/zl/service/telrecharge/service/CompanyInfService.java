package com.ebeijia.zl.service.telrecharge.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.github.pagehelper.PageInfo;


/**
 *
 * 需要开通专用账户员工的所属企业 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
public interface CompanyInfService extends IService<CompanyInf> {

	/**
	 * 查询企业信息列表
	 * @param companyInf
	 * @return
	 */
	public List<CompanyInf> getCompanyInfList(CompanyInf companyInf);

	/**
	 * 查询企业信息列表（分页）
	 * @param startNum
	 * @param pageSize
	 * @param companyInf
	 * @return
	 */
	public PageInfo<CompanyInf> getCompanyInfList(int startNum, int pageSize,CompanyInf companyInf);

	/**
	 * 根据信用代码查询企业信息
	 * @param lawCode
	 * @return
	 */
	public CompanyInf getCompanyInfByLawCode(@Param("lawCode")String lawCode);

	/**
	 * 根据平台标志查询企业信息
	 * @param isPlatform
	 * @return
	 */
	public CompanyInf getCompanyInfByIsPlatform(String isPlatform);

	/**
	 * 根据企业名称查询企业信息
	 * @param companyName
	 * @return
	 */
	public CompanyInf getCompanyInfByName(String companyName);
	
}
