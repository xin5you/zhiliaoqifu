package com.ebeijia.zl.basics.member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.basics.member.domain.UserMerchantAcct;
import com.ebeijia.zl.basics.member.mapper.UserMerchantAcctMapper;
import com.ebeijia.zl.basics.member.service.UserMerchantAcctService;
import com.ebeijia.zl.common.utils.enums.TransChnl;
import com.ebeijia.zl.common.utils.tools.NumberUtils;

@Service
public class UserMerchantAcctServiceImpl implements UserMerchantAcctService {
	
	@Autowired
	private UserMerchantAcctMapper userMerchantAcctMapper;

	
	public List<UserMerchantAcct> getUserMerchantAcctByUser(UserMerchantAcct entity) {
		entity.setChannelCode(TransChnl.CHANNEL1.toString());
		List<UserMerchantAcct> list = userMerchantAcctMapper.getUserMerchantAcctByUser(entity);
		if (list != null && list.size() > 0) {
			for (UserMerchantAcct acc : list) {
				acc.setAccBal(NumberUtils.RMBCentToYuan(acc.getAccBal()));
			}
		}
		return list;
	}
}
