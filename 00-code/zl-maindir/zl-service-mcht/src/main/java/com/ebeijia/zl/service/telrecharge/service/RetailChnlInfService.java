package com.ebeijia.zl.service.telrecharge.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;


/**
 *
 * 分销商信息表 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
public interface RetailChnlInfService extends IService<RetailChnlInf> {

	List<RetailChnlInf> getList(RetailChnlInf retailChnlInf);
}
