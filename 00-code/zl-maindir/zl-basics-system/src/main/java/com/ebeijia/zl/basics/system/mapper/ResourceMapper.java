package com.ebeijia.zl.basics.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.system.domain.Resource;

/**
 *
 * 平台资源表 Mapper 接口
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    Resource getResourceByKey(Resource resource);

    List<Resource> getResourceList(Resource entity);

    //根据用户Id获取该用户的权限
    List<Resource> getRoleResourceByRoleId(String roleId);

    /***
     * 根据用户Id 查询对应的所有资源
     * @param userId
     * @return
     */
    List<Resource> getUserResourceByUserId(String userId);

    /**
     * 高级查询
     * @param resource
     * @return
     */
    List<Resource> getResourceListByResource(Resource resource);

}
