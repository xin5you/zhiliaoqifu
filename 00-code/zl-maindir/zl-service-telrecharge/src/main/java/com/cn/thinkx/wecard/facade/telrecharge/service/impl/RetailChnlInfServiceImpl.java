package com.cn.thinkx.wecard.facade.telrecharge.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.thinkx.wecard.facade.telrecharge.domain.RetailChnlInf;
import com.cn.thinkx.wecard.facade.telrecharge.mapper.RetailChnlInfMapper;
import com.cn.thinkx.wecard.facade.telrecharge.service.RetailChnlInfService;

/**
 *
 * 分销商信息表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Service
public class RetailChnlInfServiceImpl extends ServiceImpl<RetailChnlInfMapper, RetailChnlInf> implements RetailChnlInfService{

	@Autowired
	private RetailChnlInfMapper retailChnlInfMapper;
	
	public List<RetailChnlInf> getList(RetailChnlInf retailChnlInf){
		return retailChnlInfMapper.getList(retailChnlInf);
	}
}
