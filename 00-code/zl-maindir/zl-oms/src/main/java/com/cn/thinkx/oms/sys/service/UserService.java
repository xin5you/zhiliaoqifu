package com.cn.thinkx.oms.sys.service;

import com.cn.thinkx.oms.sys.model.User;
import com.cn.thinkx.oms.sys.model.UserRole;
import com.github.pagehelper.PageInfo;

public interface UserService {
	/**
	 * 根据名称查询用户信息
	 * @param user
	 * @return
	 */
	User getUserByName(String userName, String loginName, String loginType);
	
	/**
	 * 根据Id获取用户对象
	 * @param userId
	 * @return
	 */
	User getUserById(String userId) throws Exception;
	
	/**
	 * 保存用户信息
	 * @param user
	 * @param rolesIds
	 * @return
	 */
	int saveUser(User user,String [] rolesIds) throws Exception;
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 */
	int updateUser(User user,String [] rolesId) throws Exception;
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 */
	int updateUser(User user) throws Exception;
	
	/**
	 * 删除用户信息
	 * @param userId
	 * @return
	 */
	int deleteUser(String userId) throws Exception;
	
	/**
	 * 用户列表
	 * @param startNum
	 * @param pageSize
	 * @param user
	 * @return
	 */
    public PageInfo<User> getUserPage(int startNum, int pageSize, User user);
	
	/**
	 * 增加用户角色
	 * @param entity
	 */
	void saveUserRole(UserRole entity);
	
	/**
	 * 删除用户角色
	 * @param userId
	 */
	void deleteUserRoleByUserId(String userId);
	
	/**
	 * 用户列表（商户自助服务）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param user
	 * @return
	 */
	public PageInfo<User> getDiyUserPage(int startNum, int pageSize, User user);
	
	/**
	 * 根据手机号查询用户信息
	 * @param user
	 * @return
	 */
	User getUserByPhoneNo(User user);
	
}
