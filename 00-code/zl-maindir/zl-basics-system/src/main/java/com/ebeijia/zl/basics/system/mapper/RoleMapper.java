package com.ebeijia.zl.basics.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.system.domain.Role;

/**
 *
 * 平台角色表 Mapper 接口
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 查询所有角色
     * @param role
     * @return
     */
    List<Role> getRoleList(Role role);

    /**
     * 获取某个用户的角色
     * @param userId
     * @return
     */
    List<Role> getUserRoleByUserId(String userId);
    
    Role getRoleByName(Role role);
	
	Role getRoleBySeq(Role role);

}
