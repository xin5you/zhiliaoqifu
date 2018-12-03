package com.cn.thinkx.ecom.basics.member.service.impl;

import com.cn.thinkx.ecom.basics.member.domain.UserMerchantAcct;
import com.cn.thinkx.ecom.basics.member.mapper.UserMerchantAcctMapper;
import com.cn.thinkx.ecom.basics.member.service.UserMerchantAcctService;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMerchantAcctServiceImpl implements UserMerchantAcctService {
	
	@Autowired
	private UserMerchantAcctMapper userMerchantAcctMapper;

	
	public List<UserMerchantAcct> getUserMerchantAcctByUser(UserMerchantAcct entity) {
		entity.setChannelCode(Constants.ChannelCode.CHANNEL1.toString());
		List<UserMerchantAcct> list = userMerchantAcctMapper.getUserMerchantAcctByUser(entity);
		if (list != null && list.size() > 0) {
			for (UserMerchantAcct acc : list) {
				acc.setAccBal(NumberUtils.RMBCentToYuan(acc.getAccBal()));
			}
		}
		return list;
	}
}
