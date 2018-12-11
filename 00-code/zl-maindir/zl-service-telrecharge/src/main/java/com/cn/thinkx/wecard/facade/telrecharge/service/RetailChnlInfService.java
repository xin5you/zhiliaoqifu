package com.cn.thinkx.wecard.facade.telrecharge.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.thinkx.wecard.facade.telrecharge.domain.RetailChnlInf;


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
