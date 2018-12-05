package com.cn.thinkx.diy.api.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.diy.api.base.constants.Constants;
import com.cn.thinkx.diy.api.base.constants.ExceptionEnum.userNews;
import com.cn.thinkx.diy.api.base.exception.BizHandlerException;
import com.cn.thinkx.diy.api.base.service.impl.BaseServiceImpl;
import com.cn.thinkx.diy.api.base.utils.StringUtil;
import com.cn.thinkx.diy.api.system.domain.User;
import com.cn.thinkx.diy.api.system.domain.UserRole;
import com.cn.thinkx.diy.api.system.mapper.UserMapper;
import com.cn.thinkx.diy.api.system.mapper.UserRoleMapper;
import com.cn.thinkx.diy.api.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRoleMapper urMapper;

	@Override
	public PageInfo<User> getUserPage(int startNum, int pageSize, User user) {
		PageHelper.startPage(startNum, pageSize);
		List<User> userList = this.userMapper.getList(user);
		PageInfo<User> userPage = new PageInfo<User>(userList);
		userPage.getList().stream().filter(u->{
			if(u.getUserName().equals(u.getPhoneNo()))
				u.setUserName(u.getUserName().substring(0, 3) + "****" + u.getUserName().substring(8, 11));
			return true;
		}).collect(Collectors.toList());
		return userPage;
	}

	@Override
	public List<User> getList() {
		return userMapper.getList();
	}

	@Override
	public User getUserByUserName(User user) {
		return userMapper.getUserByUserName(user);
	}

	@Override
	public User getUserByPhoneNo(String phoneNo) {
		return userMapper.getUserByPhoneNo(phoneNo);
	}

	@Override
	public List<User> getShopListByUserId(String userId) {
		return userMapper.getShopListByUserId(userId);
	}

	@Override
	public void addUser(User user) throws BizHandlerException {
		try {
			if (userMapper.insert(user) > 0) {	//新增用户信息
				UserRole ur = new UserRole();
				ur.setUserId(user.getId());
				ur.setRoleId(user.getRoleId());
				ur.setCreateUser(user.getCreateUser());
				ur.setUpdateUser(user.getUpdateUser());
				if (urMapper.insert(ur) > 0) {	//新增用户角色信息
				} else {
					throw new BizHandlerException(userNews.UN03.getCode(), userNews.UN03.getMsg());
				}
			} else {
				throw new BizHandlerException(userNews.UN02.getCode(), userNews.UN02.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateUser(HttpServletRequest req,User user) {
		HttpSession session = req.getSession();
		User u = (User) session.getAttribute(Constants.SESSION_USER);	//获取的当前用户信息
		User userTemp = userMapper.selectByPrimaryKey(user.getId());
		if (StringUtil.isNullOrEmpty(user.getUserName())){
			userTemp.setUserName(user.getPhoneNo());
		}else{
			userTemp.setUserName(user.getUserName());
		}
		userTemp.setId(user.getId());
		userTemp.setRoleId(user.getRoleId());
		userTemp.setUpdateUser(u.getId());
		if(userMapper.updateByPrimaryKey(userTemp) > 0){

			UserRole userRole = new UserRole();		//修改用户角色表
			List<UserRole> userR = urMapper.getUserRoleByUserId(user.getId());
			for (UserRole userRole2 : userR) {
				userRole = userRole2;
			}
			userRole.setRoleId(user.getRoleId());
			userRole.setUpdateUser(user.getId().toString());
			if(urMapper.updateUserRole(userRole) < 0){
				throw new BizHandlerException(userNews.UN02.getCode(), userNews.UN02.getMsg());
			}

		}else{
			throw new BizHandlerException(userNews.UN02.getCode(), userNews.UN02.getMsg());
		}
	}

	@Override
	public User getUserCheckByPhoneNo(User user) {
		return userMapper.getUserCheckByPhoneNo(user);
	}

	@Override
	public String getMchntNameByMchntCode(String mchntCode) {
		return this.userMapper.getMchntNameByMchntCode(mchntCode);
	}

	@Override
	public String getShopNameByShopCode(String shopCode) {
		return this.userMapper.getShopNameByShopCode(shopCode);
	}

	@Override
	public User getShopMchntCodeByUserId(String userId) {
		return this.userMapper.getShopMchntCodeByUserId(userId);
	}

	@Override
	public int deleteUser(String userId) throws BizHandlerException {
		int count = 0;
		if(userMapper.deleteByPrimaryKey(userId) > 0){
			count = 1;
		}else{
			throw new BizHandlerException(userNews.UN06.getCode(), userNews.UN06.getMsg());
		}
		return count;
	}

	@Override
	public User getRoleByMchntCodeAndShopCode(User user) {
		return userMapper.getRoleByMchntCodeAndShopCode(user);
	}

}
