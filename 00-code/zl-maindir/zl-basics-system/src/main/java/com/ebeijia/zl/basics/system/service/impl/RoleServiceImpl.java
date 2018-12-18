package com.ebeijia.zl.basics.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.system.domain.Role;
import com.ebeijia.zl.basics.system.mapper.RoleMapper;
import com.ebeijia.zl.basics.system.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

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
        List<Role> roleList = roleMapper.getRoleList(role);
        PageInfo<Role> rolePage = new PageInfo<Role>(roleList);
        return rolePage;
    }

	@Override
	public Role getRoleByName(Role role) {
		return roleMapper.getRoleByName(role);
	}

	@Override
	public Role getRoleBySeq(Role role) {
		return roleMapper.getRoleBySeq(role);
	}

}
