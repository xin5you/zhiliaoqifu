package com.ebeijia.zl.service.user.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.user.req.OpenUserInfReq;
import com.ebeijia.zl.facade.user.service.UserInfFacade;
import com.ebeijia.zl.facade.user.vo.ChannelUserInf;
import com.ebeijia.zl.facade.user.vo.PersonInf;
import com.ebeijia.zl.facade.user.vo.UserInf;
import com.ebeijia.zl.service.user.service.IChannelUserInfService;
import com.ebeijia.zl.service.user.service.IPersonInfService;
import com.ebeijia.zl.service.user.service.IUserInfService;

/**
* 
* @Description: 用户信息服务
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年12月3日 下午8:19:39 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年12月3日     zhuqi           v1.0.0
 */
@Service
public class UserInfFacadeImpl implements UserInfFacade {
	
	
	@Autowired
	private IUserInfService userInfService;
	
	@Autowired
	private IPersonInfService personInfService;
	
	@Autowired
	private IChannelUserInfService channelUserInfService;

	
	/**
	 * 用户注册
	 */
	public BaseResult registerUserInf(OpenUserInfReq req) {
		String userId=userInfService.registerUserInf(req.getUserType(), req.getUserName(), req.getCompanyId(), req.getMobilePhone(), req.getCardType(), req.getIcardNo(), req.getTransId(), req.getTransChnl());
		if(StringUtil.isNotEmpty(userId)){
			return ResultsUtil.success(userId);
		}else{
			return ResultsUtil.error("99","用户注册失败");
		}
		
	}

	@Override
	public UserInf getUserInfByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserInf getUserInfByExternalId(String externalId, String channel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersonInf getPersonInfByPhoneNo(String phoneNo, String channel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserInf getUserInfByPhoneNo(String phoneNo, String channel) {
		// TODO Auto-generated method stub
		return null;
	}


	

}