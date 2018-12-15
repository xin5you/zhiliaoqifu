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

	public List<CompanyInf> getCompanyInfList(CompanyInf companyInf);
	
	public PageInfo<CompanyInf> getCompanyInfList(int startNum, int pageSize,CompanyInf companyInf);
	
	public CompanyInf getCompanyInfByLawCode(@Param("lawCode")String lawCode);
	
}
