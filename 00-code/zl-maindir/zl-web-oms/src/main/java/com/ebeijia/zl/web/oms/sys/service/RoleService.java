package com.ebeijia.zl.web.oms.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.web.oms.sys.model.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;


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

    /**
     * 角色授权
     * @param roleId
     * @param resourceIds
     * @return
     */
    public void editRoleResource(String roleId ,String[] resourceIds);
}
