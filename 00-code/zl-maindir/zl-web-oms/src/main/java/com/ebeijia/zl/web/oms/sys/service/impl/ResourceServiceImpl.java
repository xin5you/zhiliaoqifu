package com.ebeijia.zl.web.oms.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.web.oms.sys.model.Resource;
import com.ebeijia.zl.web.oms.sys.mapper.ResourceMapper;
import com.ebeijia.zl.web.oms.sys.service.ResourceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * 平台资源表 Service 实现类
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    /**
     * 根据角色ID查询资源
     */
    public List<Resource> getRoleResourceByRoleId(String roleId) {
        return resourceMapper.getRoleResourceByRoleId(roleId);
    }
    public Resource getResourceByKey(String key, String loginType){
        Resource resource = new Resource();
        resource.setResourceKey(key);
        resource.setLoginType(loginType);
        return resourceMapper.getResourceByKey(resource);
    }
    public List<Resource> getResourceList(Resource entity){
        return resourceMapper.getResourceList(entity);
    }

    public PageInfo<Resource> getResourcePage(int startNum, int pageSize, Resource entity){
        PageHelper.startPage(startNum, pageSize);
        List<Resource> roleList = getResourceList(entity);
        PageInfo<Resource> userPage = new PageInfo<Resource>(roleList);
        return userPage;
    }

    public int deleteResource(String resourceId) {
        resourceMapper.deleteRoleResourceByResId(resourceId);
        return this.removeById(resourceId) ? 1:0;
    }

    @Override
    public List<Resource> getUserResourceByUserId(String userId) {
        return resourceMapper.getUserResourceByUserId(userId);
    }
}
