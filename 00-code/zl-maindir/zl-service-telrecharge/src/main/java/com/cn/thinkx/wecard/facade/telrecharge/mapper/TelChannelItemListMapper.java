package com.cn.thinkx.wecard.facade.telrecharge.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.common.base.core.mapper.BaseMapper;
import com.cn.thinkx.wecard.facade.telrecharge.model.TelChannelItemList;

@Mapper
public interface TelChannelItemListMapper extends BaseMapper<TelChannelItemList> {

	int deleteByProductId(String id) throws Exception;
}
