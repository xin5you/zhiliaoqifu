package com.ebeijia.zl.service.telrecharge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyBillingTypeInf;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 企业专项类型关联信息Mapper
 */
@Mapper
public interface CompanyBillingTypeInfMapper extends BaseMapper<CompanyBillingTypeInf> {

    /**
     * 根据条件查询企业专项类型信息列表
     * @param companyBillingTypeInf
     * @return
     */
    public List<CompanyBillingTypeInf> getCompanyBillingTypeInfList(CompanyBillingTypeInf companyBillingTypeInf);

    /**
     * 根据BId与companyId查询企业专项类型信息
     * @param companyBillingTypeInf
     * @return
     */
    public CompanyBillingTypeInf getCompanyBillingTypeInfByBIdAndCompanyId(CompanyBillingTypeInf companyBillingTypeInf);

}
