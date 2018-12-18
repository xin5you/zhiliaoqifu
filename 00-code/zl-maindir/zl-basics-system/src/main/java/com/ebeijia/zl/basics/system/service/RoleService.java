package com.ebeijia.zl.basics.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.system.domain.Role;
import com.github.pagehelper.PageInfo;


/**
 *
 * 平台角色表 Service 接口类
 *
 * @User myGen
 * @Date 2018-12-17
 */
public interface RoleService extends IService<Role> {

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
     * 查询角色分页查询
     * @param startNum
     * @param pageSize
     * @param role
     * @return
     */
    public PageInfo<Role> getRolePage(int startNum, int pageSize, Role role);
    
    Role getRoleByName(Role role);
	
	Role getRoleBySeq(Role role);

}
