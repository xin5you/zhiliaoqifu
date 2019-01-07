package com.ebeijia.zl.service.telrecharge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyBillingTypeInf;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.service.telrecharge.mapper.CompanyBillingTypeInfMapper;
import com.ebeijia.zl.service.telrecharge.mapper.CompanyInfMapper;
import com.ebeijia.zl.service.telrecharge.service.CompanyBillingTypeInfService;
import com.ebeijia.zl.service.telrecharge.service.CompanyInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业专项类型关联信息ServiceImpl
 */
@Service
public class CompanyBillingTypeInfServiceImpl extends ServiceImpl<CompanyBillingTypeInfMapper, CompanyBillingTypeInf> implements CompanyBillingTypeInfService {

    @Override
    public List<CompanyBillingTypeInf> getCompanyBillingTypeInfList(CompanyBillingTypeInf companyBillingTypeInf) {
        return baseMapper.getCompanyBillingTypeInfList(companyBillingTypeInf);
    }

    @Override
    public PageInfo<CompanyBillingTypeInf> getCompanyBillingTypeInfPage(int startNum, int pageSize, CompanyBillingTypeInf companyBillingTypeInf) {
        PageHelper.startPage(startNum, pageSize);
        List<CompanyBillingTypeInf> list = baseMapper.getCompanyBillingTypeInfList(companyBillingTypeInf);
        PageInfo<CompanyBillingTypeInf> page = new PageInfo<CompanyBillingTypeInf>(list);
        return page;
    }

    @Override
    public CompanyBillingTypeInf getCompanyBillingTypeInfByBIdAndCompanyId(CompanyBillingTypeInf companyBillingTypeInf) {
        return baseMapper.getCompanyBillingTypeInfByBIdAndCompanyId(companyBillingTypeInf);
    }
}
