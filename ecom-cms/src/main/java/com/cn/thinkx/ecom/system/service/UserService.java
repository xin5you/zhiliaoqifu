package com.cn.thinkx.ecom.system.service;

import java.util.List;

import com.cn.thinkx.ecom.common.service.BaseService;
import com.cn.thinkx.ecom.system.domain.User;
import com.github.pagehelper.PageInfo;

public interface UserService extends BaseService<User> {

	/**
	 * 用户列表（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param user
	 * @return
	 */
	public PageInfo<User> getUserPage(int startNum, int pageSize, User user);

	/**
	 * 查询所有用户信息
	 * 
	 * @return
	 */
	List<User> getList();

	/**
	 * 删除用户信息
	 * 
	 * @param userId
	 * @return
	 */
	int deleteUser(String userId);

	/**
	 * 根据登录名得到用户对象
	 * 
	 * @param loginName
	 * @return User
	 */
	User getUserByLoginName(String loginName);

	/**
	 * 新增用户角色信息
	 * 
	 * @param user
	 * @param rolesIds
	 * @return
	 */
	int addUserRole(String userId, String roleId);

	/**
	 * 删除用户角色信息
	 * @param userId
	 */
	void deleteUserRoleByUserId(String userId);
	
}
