package com.ebeijia.zl.service.telrecharge.service.impl;

import java.util.List;

import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.service.telrecharge.mapper.CompanyInfMapper;
import com.ebeijia.zl.service.telrecharge.service.CompanyInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 *
 * 需要开通专用账户员工的所属企业 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Service
public class CompanyInfServiceImpl extends ServiceImpl<CompanyInfMapper, CompanyInf> implements CompanyInfService{
	
	@Autowired
	private CompanyInfMapper companyInfMapper;

	@Override
	public boolean save(CompanyInf entity) {
		entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		entity.setCreateTime(System.currentTimeMillis());
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setLockVersion(0);
		return super.save(entity);
	}

	@Override
	public boolean updateById(CompanyInf entity){
		entity.setUpdateTime(System.currentTimeMillis());
		return super.updateById(entity);
	}

	/**
	 * 查询所有企业
	 * @param companyInf
	 * @return List
	 */
	public List<CompanyInf> getCompanyInfList(CompanyInf companyInf) {
		return companyInfMapper.getCompanyInfList(companyInf);
	}
	
	/**
	 * 结合分页查询所有企业
	 * @param startNum
	 * @param pageSize
	 * @param companyInf
	 * @return PageInfo
	 */
	public PageInfo<CompanyInf> getCompanyInfList(int startNum, int pageSize,CompanyInf companyInf) {
		PageHelper.startPage(startNum, pageSize);
		List<CompanyInf> list = getCompanyInfList(companyInf);
		PageInfo<CompanyInf> page = new PageInfo<CompanyInf>(list);
		return page;
	}

	/**
	 * 通过统一社会信用代码查询企业
	 * @param lawCode
	 * @return CompanyInf
	 */
	public CompanyInf getCompanyInfByLawCode(String lawCode) {
		return companyInfMapper.getCompanyInfByLawCode(lawCode);
	}

	@Override
	public CompanyInf getCompanyInfByIsPlatform(String isPlatform) {
		return companyInfMapper.getCompanyInfByIsPlatform(isPlatform);
	}
}
