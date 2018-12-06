package com.cn.thinkx.oms.specialAccount.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cn.thinkx.oms.specialAccount.mapper.CompanyBillingTypeInfMapper;
import com.cn.thinkx.oms.specialAccount.model.CompanyBillingTypeInf;
import com.cn.thinkx.oms.specialAccount.service.CompanyBillingTypeInfService;
import com.cn.thinkx.oms.sys.model.User;

@Service("companyBillingTypeInfService")
public class CompanyBillingTypeInfServiceImpl implements CompanyBillingTypeInfService {
	
	@Autowired
	@Qualifier("companyBillingTypeInfMapper")
	private CompanyBillingTypeInfMapper companyBillingTypeInfMapper;

	/**
	 * 修改企业专用账户类型
	 * @param merchantId
	 * @return
	 */
	public Boolean updateCompanyBillingTypeInf(String cId, String[] bIds,User user) {
		// TODO Auto-generated method stub
		CompanyBillingTypeInf companyBillingTypeInf = new CompanyBillingTypeInf();
		companyBillingTypeInf.setcId(cId);
		companyBillingTypeInf.setUpdateUser(user.getId().toString());
		int row = 0;
		if (bIds == null || bIds.length == 0) {//该企业下没有专用账户类型，就把所有的新增的专用账户类型状态改为1，即删除
			companyBillingTypeInfMapper.deleteCompanyBillingTypeInfByCId(companyBillingTypeInf);
			return true;
		} else {//该企业下有专用账户类型，就先把所有的新增的专用账户类型状态改为1，再循环将符合条件的数据状态改为0，即可用
			companyBillingTypeInfMapper.deleteCompanyBillingTypeInfByCId(companyBillingTypeInf);
			for (String bId : bIds) {
				companyBillingTypeInf.setbId(bId);
				int num =companyBillingTypeInfMapper.updateCompanyBillingTypeInf(companyBillingTypeInf);
				row = row + num;
			}
			return row == bIds.length ? true : false;
		}
		
	}

	/**
	 * 通过企业id删除企业专用账户类型
	 * @param bId
	 * @return
	 */
	public int deleteCompanyBillingTypeInfByCId(CompanyBillingTypeInf CompanyBillingTypeInf) {
		// TODO Auto-generated method stub
		return companyBillingTypeInfMapper.deleteCompanyBillingTypeInfByCId(CompanyBillingTypeInf);
	}

	/**
	 * 查询所有企业开票专用账户类型
	 * @param MerchantInf
	 * @return
	 */
	public List<CompanyBillingTypeInf> getCompanyBillingTypeInfList(String cId) {
		// TODO Auto-generated method stub
		return companyBillingTypeInfMapper.getCompanyBillingTypeInfList(cId);
	}

	/**
	 * 通过开票类型id删除企业专用账户类型
	 * @param bId
	 * @return
	 */
	public int deleteCompanyBillingTypeInfByBId(CompanyBillingTypeInf companyBillingTypeInf) {
		// TODO Auto-generated method stub
		return companyBillingTypeInfMapper.deleteCompanyBillingTypeInfByBId(companyBillingTypeInf);
	}

}
