package com.ebeijia.zl.basics.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.system.domain.Organization;

/**
 *
 * oms组织机构表 Mapper 接口
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Mapper
public interface OrganizationMapper extends BaseMapper<Organization> {

    List<Organization> getOrganizationList(Organization entity);
}
