package com.cn.thinkx.oms.specialAccount.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cn.thinkx.oms.specialAccount.mapper.CompanyBillingTypeInfMapper;
import com.cn.thinkx.oms.specialAccount.mapper.CompanyInfMapper;
import com.cn.thinkx.oms.specialAccount.model.BillingTypeInf;
import com.cn.thinkx.oms.specialAccount.model.CompanyBillingTypeInf;
import com.cn.thinkx.oms.specialAccount.model.CompanyInf;
import com.cn.thinkx.oms.specialAccount.service.BillingTypeInfService;
import com.cn.thinkx.oms.specialAccount.service.CompanyBillingTypeInfService;
import com.cn.thinkx.oms.specialAccount.service.CompanyInfService;
import com.cn.thinkx.oms.sys.model.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("companyInfService")
public class CompanyInfServiceImpl implements CompanyInfService {
	
	@Autowired
	@Qualifier("companyInfMapper")
	private CompanyInfMapper companyInfMapper;
	
	@Autowired
	@Qualifier("companyBillingTypeInfService")
	private CompanyBillingTypeInfService companyBillingTypeInfService;
	
	@Autowired
	@Qualifier("companyBillingTypeInfMapper")
	private CompanyBillingTypeInfMapper companyBillingTypeInfMapper;
	
	@Autowired
	@Qualifier("billingTypeInfService")
	private BillingTypeInfService billingTypeInfService;

	/**
	 * 通过ID查找企业信息
	 * @param cId
	 * @return CompanyInf
	 */
	public CompanyInf getCompanyInfById(String cId) {
		// TODO Auto-generated method stub
		return companyInfMapper.getCompanyInfById(cId);
	}

	/**
	 * 保存企业信息
	 * @param companyInf
	 * @param bIds
	 * @return Boolean
	 */
	public Boolean insertCompanyInf(CompanyInf companyInf,String[] bIds) {
		// TODO Auto-generated method stub
		companyInfMapper.insertCompanyInf(companyInf);//插入企业信息
		List<CompanyInf> list = companyInfMapper.getCompanyInfList(null);
		String cId = list.get(0).getcId();//获取新增企业信息对象的ID
		
		BillingTypeInf billingTypeInf = new BillingTypeInf();
		List<BillingTypeInf> bList = billingTypeInfService.getBillingTypeInfList(billingTypeInf);//获得所有的专有账户类型
		
		Boolean rt = false;
		CompanyBillingTypeInf companyBillingTypeInf =new CompanyBillingTypeInf();
	    companyBillingTypeInf.setcId(cId);//以新增的企业ID构造企业专有账户类型对象companyBillingTypeInf
	    companyBillingTypeInf.setCreateUser(companyInf.getCreateUser());
		int row = 0;
	    for (int i = 0; i < bList.size(); i++) {//在数据库中针对新增的企业信息，将所有的专有账户类型插入一遍
	    	companyBillingTypeInf.setbId(bList.get(i).getbId());
	    	int num = companyBillingTypeInfMapper.insertCompanyBillingTypeInf(companyBillingTypeInf);
	    	row = row + num;
		}
	    rt = row == bList.size() ? true : false;
	    if (rt) {
	    	companyBillingTypeInf.setUpdateUser(companyInf.getCreateUser());
	    	companyBillingTypeInfMapper.deleteCompanyBillingTypeInfByCId(companyBillingTypeInf);//将此企业下的专有账户类型状态都改为1
	    	User user = new User();
	    	user.setId(companyInf.getCreateUser().trim());
			rt = companyBillingTypeInfService.updateCompanyBillingTypeInf(cId, bIds,user);//调用修改企业专有账户类型的方法，实现新增该企业的专有账户类型
		}
	    
		return rt;
	}

	/**
	 * 修改企业信息
	 * @param bIds
	 * @return CompanyInf
	 */
	public Boolean updateCompanyInf(CompanyInf companyInf,String[] bIds) {
		// TODO Auto-generated method stub
		int row = companyInfMapper.updateCompanyInf(companyInf);//修改企业信息
		Boolean rt = row>0?true:false;
		
		if (rt) {
			User user = new User();
	    	user.setId(companyInf.getCreateUser().trim());
			rt = companyBillingTypeInfService.updateCompanyBillingTypeInf(companyInf.getcId(), bIds,user);//修改该企业下的相关专有账户类型
		}
		return rt;
	}

	/**
	 * 删除企业信息
	 * @param cId
	 * @param user
	 * @return
	 */
	public Boolean deleteCompanyInf(String cId,User user) {
		// TODO Auto-generated method stub
		CompanyInf companyInf = new CompanyInf();
		companyInf.setcId(cId);
		companyInf.setUpdateUser(user.getId().toString());
		int row = companyInfMapper.deleteCompanyInf(companyInf);//删除企业信息
		Boolean rt = row>0?true:false;
		CompanyBillingTypeInf companyBillingTypeInf = new CompanyBillingTypeInf();
		companyBillingTypeInf.setcId(cId);
		companyBillingTypeInf.setUpdateUser(user.getId().toString());
		if (rt) {
			rt = companyBillingTypeInfService.deleteCompanyBillingTypeInfByCId(companyBillingTypeInf)>0;//删除该企业下的相关专用账户类型
		}
		
		return rt;
	}

	/**
	 * 查询所有企业
	 * @param companyInf
	 * @return List
	 */
	public List<CompanyInf> getCompanyInfList(CompanyInf companyInf) {
		// TODO Auto-generated method stub
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
	 * 通过企业代码查询企业
	 * @param comCode
	 * @return CompanyInf
	 */
	public CompanyInf getCompanyInfByComCode(String comCode) {
		// TODO Auto-generated method stub
		return companyInfMapper.getCompanyInfByComCode(comCode);
	}

	/**
	 * 通过统一社会信用代码查询企业
	 * @param lawCode
	 * @return CompanyInf
	 */
	public CompanyInf getCompanyInfByLawCode(String lawCode) {
		// TODO Auto-generated method stub
		return companyInfMapper.getCompanyInfByLawCode(lawCode);
	}

}
