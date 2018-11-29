package com.cn.thinkx.ecom.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.system.domain.User;
import com.cn.thinkx.ecom.system.domain.UserRole;
import com.cn.thinkx.ecom.system.mapper.UserMapper;
import com.cn.thinkx.ecom.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public PageInfo<User> getUserPage(int startNum, int pageSize, User user) {
		PageHelper.startPage(startNum, pageSize);
		List<User> userList = this.userMapper.getList(user);
		PageInfo<User> userPage = new PageInfo<User>(userList);
		return userPage;
	}

	@Override
	public List<User> getList() {
		return userMapper.getList();
	}

	@Override
	public int deleteUser(String userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		user.setState("1");
		return userMapper.updateByPrimaryKey(user);
	}

	@Override
	public User getUserByLoginName(String loginName) {
		return userMapper.getUserByLoginName(loginName);
	}

	@Override
	public int addUserRole(String userId, String roleId) {
		int count = 0;
		if (roleId != null && roleId != "") {
			UserRole userRole = new UserRole();
			userRole.setUserId(userId);
			userRole.setRoleId(roleId);
			count = this.userMapper.addUserRole(userRole);
		}
		return count;
	}

	@Override
	public void deleteUserRoleByUserId(String userId) {
		this.userMapper.deleteUserRoleByUserId(userId);
	}

}
