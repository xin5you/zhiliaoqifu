package com.ebeijia.zl.service.telrecharge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyBillingTypeInf;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 企业专项类型关联信息Service
 */
public interface CompanyBillingTypeInfService extends IService<CompanyBillingTypeInf> {

    /**
     * 根据条件查询企业专项类型信息列表
     * @param companyBillingTypeInf
     * @return
     */
    public List<CompanyBillingTypeInf> getCompanyBillingTypeInfList(CompanyBillingTypeInf companyBillingTypeInf);

    /**
     * 根据条件查询企业专项类型信息列表（分页）
     * @param companyBillingTypeInf
     * @return
     */
    public PageInfo<CompanyBillingTypeInf> getCompanyBillingTypeInfPage(int startNum, int pageSize,CompanyBillingTypeInf companyBillingTypeInf);

    /**
     * 根据BId与companyId查询企业专项类型信息
     * @param companyBillingTypeInf
     * @return
     */
    public CompanyBillingTypeInf getCompanyBillingTypeInfByBIdAndCompanyId(CompanyBillingTypeInf companyBillingTypeInf);

}
