package com.ebeijia.zl.service.user.service.impl;

import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.facade.account.dto.IntfaceTransLog;
import com.ebeijia.zl.service.account.service.IIntfaceTransLogService;
import com.ebeijia.zl.service.account.service.ITransLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.user.vo.ChannelUserInf;
import com.ebeijia.zl.facade.user.vo.PersonInf;
import com.ebeijia.zl.facade.user.vo.UserInf;
import com.ebeijia.zl.service.user.mapper.UserInfMapper;
import com.ebeijia.zl.service.user.service.IChannelUserInfService;
import com.ebeijia.zl.service.user.service.IPersonInfService;
import com.ebeijia.zl.service.user.service.IUserInfService;

/**
 *
 * 用户信息 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,rollbackFor=Exception.class)
public class UserInfServiceImpl extends ServiceImpl<UserInfMapper, UserInf> implements IUserInfService{
	
	@Autowired
	private UserInfMapper userInfMapper;
	
	
	@Autowired
	private IPersonInfService personInfService;
	
	@Autowired
	private IChannelUserInfService channelUserInfService;


	@Autowired
	private IIntfaceTransLogService intfaceTransLogService;

	@Autowired
	private ITransLogService transLogService;
	
	/**
	* 
	* @param:userType 用户注册类型
	* @param:userName 用户名
	* @param:companyId 所属企业
	* @param:mobilePhone 手机号
	* @param:cardType 证件类型
	* @param:cardNo  证件号
	* @param:transId 交易类型
	* @param:nl 交易渠道
	* @param:userChnl 用户渠道
	* @param:userChnlId 用户渠道Id
	* 
	* @return userId
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月4日 上午10:43:18 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月4日     zhuqi           v1.0.0
	 */
	public String registerUserInf(String userType,String userName,String companyId,String mobilePhone,String cardType,String cardNo,String transId,String transChnl,String userChnl,String userChnlId){
		
		//企业账户所属用户信息注册
		if(!UserType.TYPE100.getCode().equals(userType)){
			return this.registerUserInfForCompany(userName,userType, companyId);
		}
		
		//企业员工用户注册
		/*** 判断用户 手机号是否已经注册 ***/
		PersonInf personInf = personInfService.getPersonInfByPhoneNo(mobilePhone);
		
		UserInf user=null;
		boolean userRegFlag=false;
		
		if (personInf == null) {
			/** 用户信息主键 */
			
			user = new UserInf();
			user.setUserId(IdUtil.getNextId());
			user.setUserType(userType);
			user.setUserName(user.getUserId());
			user.setCompanyId(companyId);
			this.save(user);

			/** 个人信息 **/
			personInf = new PersonInf();
			personInf.setPersonalCardType(IdUtil.getNextId());
			personInf.setUserId(user.getUserId());
			personInf.setMobilePhoneNo(mobilePhone);
			personInf.setPersonalName(userName);
			personInf.setPersonalCardType(cardType);
			personInf.setPersonalCardNo(cardNo);
			personInfService.save(personInf);
			
		}else{
			//手机号是否在当前渠道注册
			user=this.getUserInfByPhoneNo(mobilePhone, userChnl);
			if(user !=null){
				userRegFlag=true;
			}
		}
		
		/** 用户渠道信息 **/
		if(!userRegFlag){
			ChannelUserInf channelUserInf = new ChannelUserInf();
			channelUserInf.setUserId(personInf.getUserId());
		    if(StringUtil.isEmpty(userChnlId)){
				channelUserInf.setExternalId(IdUtil.getNextId());
			}else{
				channelUserInf.setExternalId(userChnlId);
			}
			channelUserInf.setChannelCode(userChnl);
			channelUserInfService.save(channelUserInf);
		}

		IntfaceTransLog intfaceTransLog=intfaceTransLogService.newItfTransLog(personInf.getPersonalId(), user.getUserId(), transId, null, userType, transChnl,
				userChnl,userChnlId,null);
		intfaceTransLog.setTransDesc("开户");
		intfaceTransLog.setMchntCode(companyId);
		intfaceTransLogService.save(intfaceTransLog);  //保存接口处交易日志
		intfaceTransLog.setBIds(SpecAccountTypeEnum.getList());

		//执行开户操作
		boolean eflag=transLogService.execute(intfaceTransLog);
		if(eflag){
			return user.getUserId();
		}else {
			return "";
		}
	}
	
	/**
	 * 
	* @Description: 注册企业账户的用户信息
	*
	* @param:
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月4日 上午10:58:38 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月4日     zhuqi           v1.0.0
	 */
	public String registerUserInfForCompany(String companyName,String userType,String companyId){
		UserInf user =this.getUserInfByUserName(companyId);
		
		if(user ==null){
			user=new UserInf();
			user.setUserId(IdUtil.getNextId());
			user.setUserType(userType);
			user.setUserName(companyId);//公司名称
			user.setCompanyId(companyId);
			this.save(user);
			
			ChannelUserInf channelUserInf = new ChannelUserInf();
			channelUserInf.setUserId(user.getUserId());
			channelUserInf.setExternalId(companyId);
			channelUserInf.setChannelCode(UserChnlCode.USERCHNL1001.getCode());
			channelUserInfService.save(channelUserInf);
		}
		return user.getUserId();
	}
	
	
	@Override
	public boolean save(UserInf entity){
		entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		entity.setCreateTime(System.currentTimeMillis());
		entity.setCreateUser("99999999");
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setUpdateUser("99999999");
		entity.setLockVersion(0);
		return super.save(entity);
	}
	
	/**
	 * 
	* @Function: 
	* @Description: 手机号查找用戶信息
	*
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 下午8:06:50 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	public UserInf getUserInfByPhoneNo( String phoneNo, String channel){
		return userInfMapper.getUserInfByPhoneNo(phoneNo, channel);
	}

	/**
	 * 
	* @Function: 
	* @Description: 外部渠道號 查找用戶信息
	*
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 下午8:07:31 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	public UserInf getUserInfByExternalId (String externalId,String channel){
		return userInfMapper.getUserInfByExternalId(externalId, channel);
	}
	
	/**
	* @Description: 获取用户信息
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月5日 下午4:23:55 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月5日     zhuqi           v1.0.0
	 */
	public UserInf getUserInfByUserName(String userName){
		QueryWrapper<UserInf> queryWrapper = new QueryWrapper<UserInf>();
		queryWrapper.eq("user_name", userName);
		queryWrapper.eq("data_stat", DataStatEnum.TRUE_STATUS.getCode());
		return userInfMapper.selectOne(queryWrapper);
	}
	
	
	/**
	 * 
	* @Description: 手机号查找用户信息
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月14日 上午10:39:46 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月14日     zhuqi           v1.0.0
	 */
	public UserInf getUserInfByMobilePhone(String mobilePhone){
		return userInfMapper.getUserInfByMobilePhone(mobilePhone);
	}

}
