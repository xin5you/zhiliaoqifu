package com.cn.thinkx.wecard.facade.telrecharge.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.wecard.facade.telrecharge.mapper.TelChannelItemListMapper;
import com.cn.thinkx.wecard.facade.telrecharge.model.TelChannelItemList;
import com.cn.thinkx.wecard.facade.telrecharge.service.TelChannelItemListFacade;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@com.alibaba.dubbo.config.annotation.Service(version = "1.0.0")
@Service
public class TelChannelItemListServiceImpl implements TelChannelItemListFacade {

	@Autowired
	private TelChannelItemListMapper telChannelItemListMapper;

	@Override
	public TelChannelItemList getTelChannelItemListById(String id) throws Exception {
		return telChannelItemListMapper.getById(id);
	}

	@Override
	public int saveTelChannelItemList(TelChannelItemList telChannelItemList) throws Exception {
		return telChannelItemListMapper.insert(telChannelItemList);
	}

	@Override
	public int updateTelChannelItemList(TelChannelItemList telChannelItemList) throws Exception {
		return telChannelItemListMapper.update(telChannelItemList);
	}

	@Override
	public int deleteTelChannelItemListById(String id) throws Exception {
		return telChannelItemListMapper.deleteById(id);
	}

	@Override
	public List<TelChannelItemList> getTelChannelItemList(TelChannelItemList telChannelItemList) throws Exception {
		return telChannelItemListMapper.getList(telChannelItemList);
	}

	@Override
	public PageInfo<TelChannelItemList> getTelChannelItemListPage(int startNum, int pageSize,
			TelChannelItemList telChannelItemList) throws Exception {
		PageHelper.startPage(startNum, pageSize);
		List<TelChannelItemList> telCItemList = getTelChannelItemList(telChannelItemList);
		for (TelChannelItemList telChannelItemList2 : telCItemList) {
			if (!StringUtil.isNullOrEmpty(telChannelItemList2.getOperId()))
				telChannelItemList2.setOperId(TelRechargeConstants.OperatorType.findByCode(telChannelItemList2.getOperId()));
		}
		PageInfo<TelChannelItemList> telChannelItemListPage = new PageInfo<TelChannelItemList>(telCItemList);
		return telChannelItemListPage;
	}

	@Override
	public int deleteByProductId(String id) throws Exception {
		return telChannelItemListMapper.deleteByProductId(id);
	}
}