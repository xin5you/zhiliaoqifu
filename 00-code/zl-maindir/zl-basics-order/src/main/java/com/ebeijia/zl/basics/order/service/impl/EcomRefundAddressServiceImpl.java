package com.ebeijia.zl.basics.order.service.impl;

import com.ebeijia.zl.basics.order.domain.EcomRefundAddress;
import com.ebeijia.zl.basics.order.mapper.EcomRefundAddressMapper;
import com.ebeijia.zl.basics.order.service.EcomRefundAddressService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
