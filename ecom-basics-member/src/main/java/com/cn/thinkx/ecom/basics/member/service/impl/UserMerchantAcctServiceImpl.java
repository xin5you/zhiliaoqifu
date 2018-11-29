package com.cn.thinkx.ecom.basics.member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.member.domain.UserMerchantAcct;
import com.cn.thinkx.ecom.basics.member.mapper.UserMerchantAcctMapper;
import com.cn.thinkx.ecom.basics.member.service.UserMerchantAcctService;
import com.cn.thinkx.ecom.common.constants.Constants.ChannelCode;
import com.cn.thinkx.ecom.common.util.NumberUtils;

@Service
public class UserMerchantAcctServiceImpl implements UserMerchantAcctService {
	
	@Autowired
	private UserMerchantAcctMapper userMerchantAcctMapper;

	
	public List<UserMerchantAcct> getUserMerchantAcctByUser(UserMerchantAcct entity) {
		entity.setChannelCode(ChannelCode.CHANNEL1.toString());
		List<UserMerchantAcct> list = userMerchantAcctMapper.getUserMerchantAcctByUser(entity);
		if (list != null && list.size() > 0) {
			for (UserMerchantAcct acc : list) {
				acc.setAccBal(NumberUtils.RMBCentToYuan(acc.getAccBal()));
			}
		}
		return list;
	}
}
