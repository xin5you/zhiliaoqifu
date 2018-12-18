package com.ebeijia.zl.basics.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.system.domain.Organization;


/**
 *
 * oms组织机构表 Service 接口类
 *
 * @User myGen
 * @Date 2018-12-17
 */
public interface OrganizationService extends IService<Organization> {

    List<Organization> getOrganizationList(Organization entity);
}
