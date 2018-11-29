package com.cn.thinkx.ecom.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.common.mapper.BaseDao;
import com.cn.thinkx.ecom.system.domain.User;
import com.cn.thinkx.ecom.system.domain.UserRole;

@Mapper
public interface UserMapper extends BaseDao<User> {

	/**
	 * 查询所有用户信息
	 * 
	 * @return
	 */
	List<User> getList();

	/**
	 * 根据登录名查找用户
	 * 
	 * @param loginname
	 * @return
	 */
	User getUserByLoginName(String loginName);

	/**
	 * 增加用户角色
	 * 
	 * @param entity
	 */
	int addUserRole(UserRole userRole);

	/**
	 * 根据用户编号删除用户角色
	 * 
	 * @param userId
	 */
	void deleteUserRoleByUserId(String userId);

}
