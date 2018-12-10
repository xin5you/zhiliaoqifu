package com.cn.thinkx.oms.sys.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.oms.sys.mapper.UserMapper;
import com.cn.thinkx.oms.sys.model.User;
import com.cn.thinkx.oms.sys.model.UserRole;
import com.cn.thinkx.oms.sys.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service("userService")
public class UserServiceImpl implements UserService {
	
    @Autowired
    private UserMapper userMapper;
    
    
	/**
	 * 根据登录名得到用户对象
	 * @param loginName
	 * @return User
	 */
	public User getUserByName(String userName, String loginName, String loginType){
		User user = new User();
		user.setUserName(userName);
		user.setLoginName(loginName);
		user.setLoginType(loginType);
		return userMapper.getUserByName(user);
	}

	/**
	 * 根据Id得到用户对象
	 * @param userId
	 * @return User
	 */
	public User getUserById(String userId) throws Exception {
		return userMapper.getUserById(userId);
	}
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 */
	public int updateUser(User user) throws Exception{
		return userMapper.updateUser(user);
	}


	public int updateUser(User user,String [] rolesId)throws Exception {
		int operNum=userMapper.updateUser(user);
		this.deleteUserRoleByUserId(user.getId().toString());
		
		if(rolesId !=null && rolesId.length>0){
			UserRole userRole=null;
			for(int i=0;i<rolesId.length;i++){
				userRole=new UserRole();
				userRole.setUserId(user.getId());
				userRole.setRoleId(rolesId[i]);
				userMapper.saveUserRole(userRole);
			}
		}
		return operNum;
	}


	public int deleteUser(String userId) throws Exception{
		User user=userMapper.getUserById(userId);
		user.setDataStat("1");
		return userMapper.updateUser(user);
	}

	/**
	 * 添加用户
	 * @param user
	 * @param rolesId
	 * @return
	 * @throws Exception
	 */
	public int saveUser(User user,String [] rolesId) throws Exception {
		int operNum = userMapper.insertUser(user);
	
		if(rolesId != null && rolesId.length > 0){
			UserRole userRole = null;
			for(int i=0; i<rolesId.length; i++){
				userRole=new UserRole();
				userRole.setUserId(user.getId());
				userRole.setRoleId(rolesId[i]);
				userMapper.saveUserRole(userRole);
			}
		}
		return operNum;
	}
	/**
	 * 用户列表
	 * @param startNum
	 * @param pageSize
	 * @param user
	 * @return
	 */
    public PageInfo<User> getUserPage(int startNum, int pageSize, User user){
		PageHelper.startPage(startNum, pageSize);
		List<User> userList = userMapper.getUserList(user);
		for (User diyUser : userList) {
			if ("分销商管理员".equals(diyUser.getRoleName())) {
				diyUser.setRoleCheckflag("0");
			} else {
				diyUser.setRoleCheckflag("1");
			}
		}
		PageInfo<User> userPage = new PageInfo<User>(userList);
		return userPage;
    }
    
	/**
	 * 增加用户角色
	 * @param entity
	 */
	public void saveUserRole(UserRole entity) {
		userMapper.saveUserRole(entity);
	}


	/**
	 * 删除用户角色
	 * @param userId
	 */
	public void deleteUserRoleByUserId(String userId) {
		userMapper.deleteUserRoleByUserId(userId);
		
	}

	@Override
	public PageInfo<User> getDiyUserPage(int startNum, int pageSize, User user) {
		PageHelper.startPage(startNum, pageSize);
		List<User> userList = userMapper.getUserList(user);
		PageInfo<User> userPage = new PageInfo<User>(userList);
		return userPage;
	}

	@Override
	public User getUserByPhoneNo(User user) {
		return userMapper.getUserByPhoneNo(user);
	}

}
