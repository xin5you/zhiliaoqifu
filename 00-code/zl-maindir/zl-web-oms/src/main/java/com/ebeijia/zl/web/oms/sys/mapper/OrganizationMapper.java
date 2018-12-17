package com.ebeijia.zl.web.oms.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.web.oms.sys.model.Organization;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
