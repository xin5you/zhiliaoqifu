package com.ebeijia.zl.service.user.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.user.req.OpenUserInfReqVo;
import com.ebeijia.zl.facade.user.service.UserInfFacade;
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
@Configuration
@com.alibaba.dubbo.config.annotation.Service(interfaceName="userInfFacade")
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
	public BaseResult registerUserInf(OpenUserInfReqVo req) {
		String userId=userInfService.registerUserInf(
				req.getUserType(), req.getUserName(),
				req.getCompanyId(), 
				req.getMobilePhone(),
				req.getCardType(), 
				req.getIcardNo(), 
				req.getTransId(),
				req.getTransChnl(),
				req.getUserChnl(),
				req.getUserChnlId());
		if(StringUtil.isNotEmpty(userId)){
			return ResultsUtil.success(userId);
		}else{
			return ResultsUtil.error("99","用户注册失败");
		}
		
	}

	@Override
	public UserInf getUserInfByUserId(String userId) {
		return userInfService.getById(userId);
	}

	@Override
	public UserInf getUserInfByExternalId(String externalId, String channel) {
		return userInfService.getUserInfByExternalId(externalId,channel);
	}

	@Override
	public PersonInf getPersonInfByPhoneNo(String phoneNo) {
		return personInfService.getPersonInfByPhoneNo(phoneNo);
	}

	@Override
	public UserInf getUserInfByPhoneNo(String phoneNo, String channel) {
		return userInfService.getUserInfByPhoneNo(phoneNo,channel);
	}

	public UserInf getUserInfByMobilePhone(String phoneNo){
		return userInfService.getUserInfByMobilePhone(phoneNo);
	}

}
