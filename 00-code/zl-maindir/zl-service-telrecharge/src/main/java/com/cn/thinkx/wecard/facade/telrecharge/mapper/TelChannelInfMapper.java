package com.cn.thinkx.wecard.facade.telrecharge.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.common.base.core.mapper.BaseMapper;
import com.cn.thinkx.wecard.facade.telrecharge.model.TelChannelInf;

@Mapper
public interface TelChannelInfMapper extends BaseMapper<TelChannelInf> {

	TelChannelInf getTelChannelInfByMchntCode(String mchntCode);
}
