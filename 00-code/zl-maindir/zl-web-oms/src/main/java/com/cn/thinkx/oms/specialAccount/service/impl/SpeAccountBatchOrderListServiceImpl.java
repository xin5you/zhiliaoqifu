package com.cn.thinkx.oms.specialAccount.service.impl;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.oms.common.util.OmsEnum.BatchOrderStat;
import com.cn.thinkx.oms.specialAccount.mapper.SpeAccountBatchOrderListMapper;
import com.cn.thinkx.oms.specialAccount.mapper.SpeAccountBatchOrderMapper;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrder;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.cn.thinkx.oms.specialAccount.service.SpeAccountBatchOrderListService;
import com.cn.thinkx.oms.sys.model.User;
import com.ebeijia.zl.common.utils.constants.Constants;
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
			list.forEach(batchOrderList ->{
				batchOrderList.setOrderStat(BatchOrderStat.findStat(batchOrderList.getOrderStat()));
				if(!StringUtil.isEmpty(batchOrderList.getAmount())){
					batchOrderList.setAmount(NumberUtils.RMBCentToYuan(batchOrderList.getAmount()));
				}
			});
			page = new PageInfo<SpeAccountBatchOrderList>(list);
		}
		return page;
	}

	@Override
	public int addOrderList(SpeAccountBatchOrderList orderList, User user) {
		SpeAccountBatchOrder batchOrder = new SpeAccountBatchOrder();
		batchOrder.setOrderId(orderList.getOrderId());
		batchOrder.setUpdateUser(user.getId());
		batchOrder.setUpdateTime(System.currentTimeMillis());
		speAccountBatchOrderMapper.updateSpeAccountBatchOrder(batchOrder);
		/*batchOrder.setOrderType(BatchOrderType.BatchOrderType_100.getCode());
		batchOrder.setOrderDate(System.currentTimeMillis());
		batchOrder.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
		batchOrder.setCreateTime(System.currentTimeMillis());
		batchOrder.setCreateUser(user.getId());
		int i = speAccountBatchOrderMapper.addSpeAccountBatchOrder(batchOrder);
		if (i < 1) {
			return 0;
		}*/
		orderList.setOrderListId(UUID.randomUUID().toString());
		orderList.setOrderId(batchOrder.getOrderId());
		orderList.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
		orderList.setCreateUser(user.getId());
		orderList.setUpdateUser(user.getId());
		orderList.setCreateTime(System.currentTimeMillis());
		orderList.setUpdateTime(System.currentTimeMillis());
		/*SpeAccountBatchOrder bo = new SpeAccountBatchOrder();
		bo.setOrderId(orderList.getOrderId());
		bo.setUpdateTime(System.currentTimeMillis());
		speAccountBatchOrderMapper.updateSpeAccountBatchOrder(bo);*/
		return speAccountBatchOrderListMapper.addOrderList(orderList);
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
		return speAccountBatchOrderListMapper.deleteSpeAccountBatchOrderList(orderListId);
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

}
