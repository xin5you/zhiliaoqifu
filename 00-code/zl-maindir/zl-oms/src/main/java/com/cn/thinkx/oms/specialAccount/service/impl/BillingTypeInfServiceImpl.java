package com.cn.thinkx.oms.specialAccount.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cn.thinkx.oms.specialAccount.mapper.BillingTypeInfMapper;
import com.cn.thinkx.oms.specialAccount.mapper.CompanyBillingTypeInfMapper;
import com.cn.thinkx.oms.specialAccount.mapper.GatewayBillingTypeInfMapper;
import com.cn.thinkx.oms.specialAccount.model.BillingTypeInf;
import com.cn.thinkx.oms.specialAccount.service.BillingTypeInfService;
import com.cn.thinkx.oms.specialAccount.service.CompanyInfService;
import com.cn.thinkx.oms.specialAccount.service.GatewayInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("billingTypeInfService")
public class BillingTypeInfServiceImpl implements BillingTypeInfService {
	
	@Autowired
	@Qualifier("billingTypeInfMapper")
	private BillingTypeInfMapper billingTypeInfMapper;
	
	@Autowired
	@Qualifier("companyInfService")
	private CompanyInfService companyInfService;
	
	@Autowired
	@Qualifier("companyBillingTypeInfMapper")
	private CompanyBillingTypeInfMapper companyBillingTypeInfMapper;
	
	@Autowired
	@Qualifier("gatewayBillingTypeInfMapper")
	private GatewayBillingTypeInfMapper gatewayBillingTypeInfMapper;
	
	@Autowired
	@Qualifier("gatewayInfService")
	private GatewayInfService gatewayInfService;

	/**
	 * 通过ID查找专用账户类型
	 * @param bId
	 * @return 专用账户类型对象
	 */
	public BillingTypeInf getBillingTypeInfById(String bId) {
		// TODO Auto-generated method stub
		return billingTypeInfMapper.getBillingTypeInfById(bId);
	}

	/**
	 * 查询所有开票类型
	 * @param MerchantInf
	 * @return 开票类型集合
	 */
	public List<BillingTypeInf> getBillingTypeInfList(BillingTypeInf billingTypeInf) {
		// TODO Auto-generated method stub
		return billingTypeInfMapper.getBillingTypeInfList(billingTypeInf);
	}

	/**
	 * 结合分页查询所有开票类型
	 * @param MerchantInf
	 * @return PageInfo对象
	 */
	public PageInfo<BillingTypeInf> getBillingTypeInfList(int startNum, int pageSize, BillingTypeInf billingTypeInf) {
		PageHelper.startPage(startNum, pageSize);
		List<BillingTypeInf> list = getBillingTypeInfList(billingTypeInf);
		PageInfo<BillingTypeInf> page = new PageInfo<BillingTypeInf>(list);
		return page;
	}

	/**
	 * 通过name查找专用账户类型
	 * @param name
	 * @return 专用账户类型对象
	 */
	public BillingTypeInf getBillingTypeInfByName(String name) {
		// TODO Auto-generated method stub
		return billingTypeInfMapper.getBillingTypeInfByName(name);
	}
	


}
