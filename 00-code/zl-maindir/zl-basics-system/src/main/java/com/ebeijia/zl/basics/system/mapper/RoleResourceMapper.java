package com.ebeijia.zl.basics.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.system.domain.RoleResource;

/**
 *
 * 平台角色资源关联表 Mapper 接口
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Mapper
public interface RoleResourceMapper extends BaseMapper<RoleResource> {

	int deleteRoleResourceByRoleId(String roleId);
}
