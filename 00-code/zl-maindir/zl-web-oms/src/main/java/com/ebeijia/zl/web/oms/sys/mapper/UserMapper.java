package com.ebeijia.zl.web.oms.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ebeijia.zl.web.oms.sys.model.User;
import com.ebeijia.zl.web.oms.sys.model.UserRole;

@Mapper
public interface UserMapper{
	
	int insertUser(User user);

	int updateUser(User user);
	
	int deleteUserById(String userId);
	
	User getUserById(String userId);
	
	User getUserByName(User user);

	List<User> getUserList(User order);
	
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
	 * 根据手机号查询
	 * @param user
	 * @return
	 */
	User getUserByPhoneNo(User user);
}
