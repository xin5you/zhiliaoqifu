package com.ebeijia.zl.basics.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.system.domain.UserRole;

/**
 *
 * 平台用户角色关联表 Mapper 接口
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

	int deleteUserRoleByUserId (String userId);
	
	List<UserRole> getUserRoleByRoleId(String roleId);
}
