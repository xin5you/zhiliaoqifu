package com.ebeijia.zl.web.oms.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.web.oms.sys.model.Role;
import com.ebeijia.zl.web.oms.sys.model.RoleResource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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


    /**
     * 删除用户角色
     */
    void deleteUserRoleByRoleId(String roleId);

    /**
     * 删除角色资源
     */
    void deleteRoleResourceByRoleId(String roleId);


    void saveRoleResource(RoleResource roleResource);
}
