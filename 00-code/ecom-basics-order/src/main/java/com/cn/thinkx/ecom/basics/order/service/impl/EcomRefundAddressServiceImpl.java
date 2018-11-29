package com.cn.thinkx.ecom.basics.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.EcomRefundAddress;
import com.cn.thinkx.ecom.basics.order.mapper.EcomRefundAddressMapper;
import com.cn.thinkx.ecom.basics.order.service.EcomRefundAddressService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.common.util.StringUtil;

@Service("ecomRefundAddressService")
public class EcomRefundAddressServiceImpl extends BaseServiceImpl<EcomRefundAddress>
		implements EcomRefundAddressService {

	@Autowired
	private EcomRefundAddressMapper ecomRefundAddressMapper;

	@Override
	public void saveEcomRefundAddress(EcomRefundAddress entity) {
		EcomRefundAddress refundadd = this.selectByReturnsId(entity.getReturnsId());
		if(StringUtil.isNullOrEmpty(refundadd)){
			ecomRefundAddressMapper.insert(entity);
		}else{
			refundadd.setReturnsId(entity.getReturnsId());
			refundadd.setProvinceName(entity.getProvinceName());
			refundadd.setCityName(entity.getCityName());
			refundadd.setDistrictName(entity.getDistrictName());
			refundadd.setAddress(entity.getAddress());
			refundadd.setFullAddress(entity.getFullAddress());
			refundadd.setZipCode(entity.getZipCode());
			refundadd.setName(entity.getName());
			refundadd.setMobile(entity.getMobile());
			refundadd.setReturnsDesc(entity.getTelephone());
			ecomRefundAddressMapper.updateByPrimaryKey(entity);
		}
	}

	@Override
	public EcomRefundAddress selectByReturnsId(String returnsId) {
		return ecomRefundAddressMapper.selectByReturnsId(returnsId);
	}

}
