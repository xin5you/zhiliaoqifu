package com.ebeijia.zl.service.telrecharge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;

/**
 *
 * 需要开通专用账户员工的所属企业 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Mapper
public interface CompanyInfMapper extends BaseMapper<CompanyInf> {

	CompanyInf getCompanyInfByLawCode(String lawCode);
	
	public List<CompanyInf> getCompanyInfList(CompanyInf companyInf);
}
