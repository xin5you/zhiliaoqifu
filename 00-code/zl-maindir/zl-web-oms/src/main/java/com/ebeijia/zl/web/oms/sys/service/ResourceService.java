package com.ebeijia.zl.web.oms.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.web.oms.sys.model.Resource;
import com.github.pagehelper.PageInfo;

import java.util.List;


/**
 *
 * 平台资源表 Service 接口类
 *
 * @User myGen
 * @Date 2018-12-17
 */
public interface ResourceService extends IService<Resource> {

    Resource getResourceByKey(String key, String loginType);

    // <!-- 根据角色Id获取该用户的权限-->
    List<Resource> getRoleResourceByRoleId(String roleId);

    int deleteResource(String resourceId);

    /***
     * 根据用户Id 查询对应的所有资源
     * @param userId
     * @return
     */
    public List<Resource> getUserResourceByUserId(String userId);

    List<Resource> getResourceList(Resource entity);

    PageInfo<Resource> getResourcePage(int startNum, int pageSize, Resource entity);

}
