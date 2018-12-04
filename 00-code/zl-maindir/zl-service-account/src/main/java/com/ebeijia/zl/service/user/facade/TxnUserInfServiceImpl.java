package com.ebeijia.zl.service.user.facade;

import org.springframework.beans.factory.annotation.Autowired;

import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.facade.user.req.OpenUserInfReq;
import com.ebeijia.zl.facade.user.service.UserFacade;
import com.ebeijia.zl.facade.user.vo.PersonInf;
import com.ebeijia.zl.facade.user.vo.UserInf;
import com.ebeijia.zl.service.user.service.IPersonInfService;
import com.ebeijia.zl.service.user.service.IUserInfService;

/**
 * 
* 
* @ClassName: TxnUserInfServiceImpl.java
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
public class TxnUserInfServiceImpl implements UserFacade {
	
	
	@Autowired
	private IUserInfService userInfService;
	
	@Autowired
	private IPersonInfService personInfService;


	public String registerUserInf(OpenUserInfReq req) {
		
		/**
		 * 查询用户信息
		 */
//		UserInf userInf=userInfService.getUserInfByPhoneNo(req.getMobilePhone(), req.getTransChnl());
		
		/*** 判断用户 手机号是否已经注册 ***/
		PersonInf personInf = personInfService.getPersonInfByPhoneNo(req.getMobilePhone());
		
		
		UserInf user=null;
		if (personInf == null) {
			/** 用户信息主键 */
			
			user = new UserInf();
			user.setUserId(IdUtil.getNextId());
			user.setUserType(req.getUserType());
			user.setCompanyId(req.getCompanyId());
			userInfService.save(user);

			/** 个人信息 **/
			personInf = new PersonInf();
			personInf.setUserId(user.getUserId());
			personInf.setMobilePhoneNo(req.getMobilePhone());
			personInf.setPersonalName(req.getUserName());
			personInf.setPersonalCardType(req.getCardType());
			personInf.setPersonalCardNo(req.getIcardNo());
			personInfService.save(personInf);
		}else{
			user=userInfService.getUserInfByPhoneNo(req.getMobilePhone(), req.getTransChnl());
		}
		return null;
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
