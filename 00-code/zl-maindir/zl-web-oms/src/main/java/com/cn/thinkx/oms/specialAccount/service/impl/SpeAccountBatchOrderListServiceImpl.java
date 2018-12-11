package com.cn.thinkx.oms.specialAccount.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.cn.thinkx.oms.common.util.OmsEnum.BatchOrderStat;
import com.cn.thinkx.oms.specialAccount.mapper.SpeAccountBatchOrderListMapper;
import com.cn.thinkx.oms.specialAccount.mapper.SpeAccountBatchOrderMapper;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrder;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.cn.thinkx.oms.specialAccount.service.SpeAccountBatchOrderListService;
import com.cn.thinkx.oms.sys.model.User;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("speAccountBatchOrderListService")
public class SpeAccountBatchOrderListServiceImpl implements SpeAccountBatchOrderListService {
	
	@Autowired
	private SpeAccountBatchOrderListMapper speAccountBatchOrderListMapper;
	
	@Autowired
	private SpeAccountBatchOrderMapper speAccountBatchOrderMapper;
	
	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;

	@Override
	public List<SpeAccountBatchOrderList> getSpeAccountBatchOrderListByOrderId(String orderId) { 
		return speAccountBatchOrderListMapper.getSpeAccountBatchOrderListByOrderId(orderId);
	}

	@Override
	public int addSpeAccountBatchOrderList(List<SpeAccountBatchOrderList> list) { 
		return speAccountBatchOrderListMapper.addSpeAccountBatchOrderList(list);
	}

	@Override
	public PageInfo<SpeAccountBatchOrderList> getSpeAccountBatchOrderListPage(int startNum, int pageSize, String orderId) {
		PageHelper.startPage(startNum, pageSize);
		List<SpeAccountBatchOrderList> list = getSpeAccountBatchOrderListByOrderId(orderId);
		PageInfo<SpeAccountBatchOrderList> page = null;
		if (list != null) {
			for (SpeAccountBatchOrderList batchOrderList : list) {
				batchOrderList.setOrderStat(BatchOrderStat.findStat(batchOrderList.getOrderStat()));
				batchOrderList.setAccountTypeName(UserType.findByCode(batchOrderList.getAccountType()).getValue());
				batchOrderList.setBizTypeName(SpecAccountTypeEnum.findByBId(batchOrderList.getBizType()).getName());
				if(!StringUtil.isEmpty(batchOrderList.getAmount())){
					batchOrderList.setAmount(Double.valueOf(NumberUtils.RMBCentToYuan(batchOrderList.getAmount().toString())));
				}
			}
//			list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SpeAccountBatchOrderList::getPhoneNo))), ArrayList::new));
			page = new PageInfo<SpeAccountBatchOrderList>(list);
		}
		return page;
	}

	@Override
	public int addOrderList(SpeAccountBatchOrderList orderList, User user, String[] bizType) {
		SpeAccountBatchOrder batchOrder = new SpeAccountBatchOrder();
		batchOrder.setOrderId(orderList.getOrderId());
		batchOrder.setUpdateUser(user.getId());
		batchOrder.setUpdateTime(System.currentTimeMillis());
		speAccountBatchOrderMapper.updateSpeAccountBatchOrder(batchOrder);
		List<SpeAccountBatchOrderList> batchOrderList = new ArrayList<SpeAccountBatchOrderList>();
		for (String type : bizType) {
			orderList.setOrderListId(UUID.randomUUID().toString());
			orderList.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
			orderList.setBizType(type);
			orderList.setCreateUser(user.getId());
			orderList.setUpdateUser(user.getId());
			orderList.setCreateTime(System.currentTimeMillis());
			orderList.setUpdateTime(System.currentTimeMillis());
			batchOrderList.add(orderList);
		}
		return speAccountBatchOrderListMapper.addSpeAccountBatchOrderList(batchOrderList);
	}

	@Override
	public int deleteSpeAccountBatchOrderList(HttpServletRequest req) {
		String orderListId = StringUtil.nullToString(req.getParameter("orderListId"));
		HttpSession session=req.getSession();
		User user=(User)session.getAttribute(Constants.SESSION_USER);
		SpeAccountBatchOrderList bol = getSpeAccountBatchOrderListByOrderListId(orderListId);
		SpeAccountBatchOrder bo = speAccountBatchOrderMapper.getSpeAccountBatchOrderById(bol.getOrderId());
		bo.setUpdateUser(user.getId().toString());
		bo.setUpdateTime(System.currentTimeMillis());
		speAccountBatchOrderMapper.updateSpeAccountBatchOrder(bo);
		List<SpeAccountBatchOrderList> orderList = speAccountBatchOrderListMapper.getSpeAccountBatchOrderListByOrderId(bol.getOrderId());
		for (SpeAccountBatchOrderList list : orderList) {
			list.setOrderStat(BatchOrderStat.BatchOrderStat_20.getCode());
			list.setUpdateUser(user.getId());
			list.setUpdateTime(System.currentTimeMillis());
		}
		return speAccountBatchOrderListMapper.updateSpeAccountBatchOrderListByList(orderList);
	}

	@Override
	public int updateSpeAccountBatchOrderList(SpeAccountBatchOrderList orderList) {
		return speAccountBatchOrderListMapper.updateSpeAccountBatchOrderList(orderList);
	}

	@Override
	public List<SpeAccountBatchOrderList> getSpeAccountBatchOrderListFailListByOrderId(String orderId) {
		return speAccountBatchOrderListMapper.getSpeAccountBatchOrderListFailListByOrderId(orderId);
	}

	@Override
	public List<SpeAccountBatchOrderList> getSpeAccountBatchOrderListByOrder(SpeAccountBatchOrderList orderList) {
		return speAccountBatchOrderListMapper.getSpeAccountBatchOrderListByOrder(orderList);
	}

	@Override
	public SpeAccountBatchOrderList getSpeAccountBatchOrderListByOrderListId(String orderListId) {
		return speAccountBatchOrderListMapper.getSpeAccountBatchOrderListByOrderListId(orderListId);
	}

	@Override
	public int updateSpeAccountBatchOrderListByList(List<SpeAccountBatchOrderList> list) {
		return speAccountBatchOrderListMapper.updateSpeAccountBatchOrderListByList(list);
	}

	@Override
	public LinkedList<SpeAccountBatchOrderList> getRedisBatchOrderList(String bathOpen) {
		String getData = jedisClusterUtils.get(bathOpen); // 从缓存钟获取信息
		LinkedList<SpeAccountBatchOrderList> orderList = null;
		if (getData != null) {
			orderList = new LinkedList(JSONObject.parseArray(getData, SpeAccountBatchOrderList.class));
		}
		return orderList;
	}

}
