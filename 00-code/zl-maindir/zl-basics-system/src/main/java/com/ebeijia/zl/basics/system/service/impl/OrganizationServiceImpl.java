package com.ebeijia.zl.basics.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.system.domain.Organization;
import com.ebeijia.zl.basics.system.mapper.OrganizationMapper;
import com.ebeijia.zl.basics.system.service.OrganizationService;

/**
 *
 * oms组织机构表 Service 实现类
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public List<Organization> getOrganizationList(Organization entity) {
        return organizationMapper.getOrganizationList(entity);
    }
}
