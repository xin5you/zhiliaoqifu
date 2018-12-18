package com.ebeijia.zl.basics.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.basics.system.mapper.UserMapper;
import com.ebeijia.zl.basics.system.service.UserService;
import com.ebeijia.zl.common.utils.enums.LoginType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 *
 * 平台用户信息表 Service 实现类
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

	@Override
	public User getUserByName(String userName, String loginName, String loginType) {
		User user = new User();
		user.setUserName(userName);
		user.setLoginName(loginName);
		user.setLoginType(loginType);
		return userMapper.getUserByName(user);
	}

	@Override
	public PageInfo<User> getUserPage(int startNum, int pageSize, User user) {
		PageHelper.startPage(startNum, pageSize);
		List<User> userList = new ArrayList<>();
		if (LoginType.LoginType1.getCode().equals(user.getLoginType())) {
			userList = userMapper.getUserOmsList(user);
		} else if (LoginType.LoginType2.getCode().equals(user.getLoginType())) {
			
		} else if (LoginType.LoginType3.getCode().equals(user.getLoginType())) {
			userList = userMapper.getUserDiyList(user);
		}
		PageInfo<User> userPage = new PageInfo<User>(userList);
		return userPage;
	}

	@Override
	public User getUserByPhoneNo(User user) {
		return userMapper.getUserByPhoneNo(user);
	}

}
