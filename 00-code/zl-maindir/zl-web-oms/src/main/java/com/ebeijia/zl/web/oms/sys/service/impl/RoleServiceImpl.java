package com.ebeijia.zl.web.oms.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.web.oms.sys.model.Role;
import com.ebeijia.zl.web.oms.sys.mapper.RoleMapper;
import com.ebeijia.zl.web.oms.sys.model.RoleResource;
import com.ebeijia.zl.web.oms.sys.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * 平台角色表 Service 实现类
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;




    /**
     * 查询所有角色
     */
    public List<Role> getRoleList(Role entity){
        return roleMapper.getRoleList(entity);
    }

    /**
     * 删除角色
     * @param roleId
     */
    public void deleteRoleById(String roleId) {

        /**删除用户与角色关系*/
        roleMapper.deleteUserRoleByRoleId(roleId);

        /**删除角色与资源关联关系表*/
        roleMapper.deleteRoleResourceByRoleId(roleId);

        /**删除角色*/
        this.removeById(roleId);

    }
    /**
     * 获取某个用户的角色
     * @param userId
     * @return
     */
    public List<Role> getUserRoleByUserId(String userId){
        return roleMapper.getUserRoleByUserId(userId);
    }

    /**
     * 查询角色分页查询
     * @param startNum
     * @param pageSize
     * @param role
     * @return
     */
    public PageInfo<Role> getRolePage(int startNum, int pageSize, Role role){
        PageHelper.startPage(startNum, pageSize);
        List<Role> roleList = getRoleList(role);
        PageInfo<Role> rolePage = new PageInfo<Role>(roleList);
        return rolePage;
    }

    /**
     * 角色授权
     * @param roleId
     * @param resourceIds
     * @return
     */
    public void editRoleResource(String roleId ,String[] resourceIds){

        roleMapper.deleteRoleResourceByRoleId(roleId);

        if(resourceIds !=null && resourceIds.length>0){
            RoleResource roleResource=null;
            for(int i=0;i<resourceIds.length;i++){
                roleResource=new RoleResource();
                roleResource.setRoleId(roleId);
                roleResource.setResourceId(resourceIds[i]);
                roleMapper.saveRoleResource(roleResource);
            }
        }
    }
}
