package com.ebeijia.zl.web.oms.batchOrder.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.web.oms.batchOrder.mapper.BatchOrderListMapper;
import com.ebeijia.zl.web.oms.batchOrder.mapper.BatchOrderMapper;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrder;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderListService;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("batchOrderListService")
public class BatchOrderListServiceImpl extends ServiceImpl<BatchOrderListMapper, BatchOrderList> implements BatchOrderListService {
	
	Logger logger = LoggerFactory.getLogger(BatchOrderListServiceImpl.class);
	
	@Autowired
	private BatchOrderListMapper batchOrderListMapper;
	
	@Autowired
	private BatchOrderMapper batchOrderMapper;
	
	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;

	@Override
	public LinkedList<BatchOrderList> getRedisBatchOrderList(String bathOpen) {
		String getData = jedisClusterUtils.get(bathOpen); // 从缓存钟获取信息
		LinkedList<BatchOrderList> orderList = null;
		if (getData != null) {
			orderList = new LinkedList(JSONObject.parseArray(getData, BatchOrderList.class));
		}
		return orderList;
	}

	@Override
	public PageInfo<BatchOrderList> getBatchOrderListPage(int startNum, int pageSize, String orderId) {
		PageHelper.startPage(startNum, pageSize);
		List<BatchOrderList> list = batchOrderListMapper.getBatchOrderListByOrderId(orderId);
		PageInfo<BatchOrderList> page = null;
		if (list != null) {
			for (BatchOrderList batchOrderList : list) {
				batchOrderList.setOrderStat(BatchOrderStat.findStat(batchOrderList.getOrderStat()));
				batchOrderList.setAccountTypeName(UserType.findByCode(batchOrderList.getAccountType()).getValue());
				batchOrderList.setBizTypeName(SpecAccountTypeEnum.findByBId(batchOrderList.getBizType()).getName());
				if(!StringUtil.isEmpty(batchOrderList.getAmount())){
					batchOrderList.setAmount(new BigDecimal(NumberUtils.RMBCentToYuan(batchOrderList.getAmount().toString())));
				}
			}
			page = new PageInfo<BatchOrderList>(list);
		}
		return page;
	}

	@Override
	public List<BatchOrderList> getBatchOrderListByOrderId(String orderId) {
		return batchOrderListMapper.getBatchOrderListByOrderId(orderId);
	}

	@Override
	public int addOrderListByUpdateOpenAccount(BatchOrderList orderList, User user, String[] bizType) {
		BatchOrder batchOrder = new BatchOrder();
		batchOrder.setOrderId(orderList.getOrderId());
		batchOrder.setUpdateUser(user.getId());
		batchOrder.setUpdateTime(System.currentTimeMillis());
		
		List<BatchOrderList> batchOrderList = new ArrayList<BatchOrderList>();
		for (String type : bizType) {
			orderList.setOrderListId(IdUtil.getNextId());
			orderList.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
			orderList.setBizType(type);
			orderList.setCreateUser(user.getId());
			orderList.setUpdateUser(user.getId());
			orderList.setCreateTime(System.currentTimeMillis());
			orderList.setUpdateTime(System.currentTimeMillis());
			batchOrderList.add(orderList);
		}
		int i = batchOrderMapper.updateBatchOrder(batchOrder);
		if (i < 1) {
			logger.error("## 更新订单信息失败，orderId--->{}", orderList.getOrderId());
			return 0;
		}
		int j = batchOrderListMapper.addBatchOrderListByList(batchOrderList);
		if (i < 1) {
			logger.error("## 更新订单明细信息失败，orderId--->{}", orderList.getOrderId());
			return 0;
		}
		return 1;
	}

	@Override
	public List<BatchOrderList> getBatchOrderListByOrder(BatchOrderList orderList) {
		return batchOrderListMapper.getBatchOrderListByOrder(orderList);
	}

	@Override
	public int deleteBatchOrderList(HttpServletRequest req) {
		String orderListId = StringUtil.nullToString(req.getParameter("orderListId"));
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		BatchOrderList orderList = batchOrderListMapper.getBatchOrderListByOrderListId(orderListId);
		BatchOrder order = batchOrderMapper.getBatchOrderById(orderList.getOrderId());
		order.setUpdateUser(user.getId().toString());
		order.setUpdateTime(System.currentTimeMillis());
		
		BatchOrderList batchOrderList = new BatchOrderList();
		batchOrderList.setOrderId(order.getOrderId());
		batchOrderList.setPhoneNo(orderList.getPhoneNo());
		batchOrderList.setAccountType(orderList.getAccountType());
		List<BatchOrderList> batchOrderLists = batchOrderListMapper.getBatchOrderListByOrder(batchOrderList);
		for (BatchOrderList list : batchOrderLists) {
			list.setOrderStat(BatchOrderStat.BatchOrderStat_20.getCode());
			list.setUpdateUser(user.getId());
			list.setUpdateTime(System.currentTimeMillis());
		}
		int orderResult = batchOrderMapper.updateBatchOrder(order);
		if (orderResult < 1) {
			logger.error("## 更新批量订单信息失败,orderId--->{}", order.getOrderId());
			return 0;
		}
		int orderListResult = batchOrderListMapper.updateBatchOrderListByList(batchOrderLists);
		if (orderListResult < 1) {
			logger.error("## 更新批量订单明细信息失败,orderId--->{},batchOrderLists--->{}", order.getOrderId(), JSONArray.toJSONString(batchOrderLists));
			return 0;
		}
		return 1;
	}


}
