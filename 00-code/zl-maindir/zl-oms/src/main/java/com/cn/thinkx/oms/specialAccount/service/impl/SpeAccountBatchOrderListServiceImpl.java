/*package com.cn.thinkx.oms.specialAccount.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.oms.specialAccount.mapper.SpeAccountBatchOrderListMapper;
import com.cn.thinkx.oms.specialAccount.mapper.SpeAccountBatchOrderMapper;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrder;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.cn.thinkx.oms.specialAccount.service.SpeAccountBatchOrderListService;
import com.cn.thinkx.oms.sys.model.User;
import com.cn.thinkx.pms.base.utils.BaseConstants;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import antlr.StringUtils;

@Service("speAccountBatchOrderListService")
public class SpeAccountBatchOrderListServiceImpl implements SpeAccountBatchOrderListService {
	
	@Autowired
	private SpeAccountBatchOrderListMapper speAccountBatchOrderListMapper;
	
	@Autowired
	private SpeAccountBatchOrderMapper speAccountBatchOrderMapper;
	

	@Override
	public List<SpeAccountBatchOrderList> getSpeAccountBatchOrderListList(String orderId) { 
		return speAccountBatchOrderListMapper.getSpeAccountBatchOrderListList(orderId);
	}

	@Override
	public int addSpeAccountBatchOrderList(List<SpeAccountBatchOrderList> list) { 
		return speAccountBatchOrderListMapper.addSpeAccountBatchOrderList(list);
	}

	@Override
	public PageInfo<SpeAccountBatchOrderList> getSpeAccountBatchOrderListPage(int startNum, int pageSize, String orderId) {
		PageHelper.startPage(startNum, pageSize);
		List<SpeAccountBatchOrderList> list = getSpeAccountBatchOrderListList(orderId);
		PageInfo<SpeAccountBatchOrderList> page = null;
		if (list != null) {
			list.forEach(batchOrderList ->{
				batchOrderList.setOrderStat(BaseConstants.OMSOrderListStat.findOrderListStat(batchOrderList.getOrderStat()));
				if(StringUtils.isNotNull(batchOrderList.getAmount())){
					batchOrderList.setAmount(NumberUtils.RMBCentToYuan(batchOrderList.getAmount()));
				}
			});
			page = new PageInfo<SpeAccountBatchOrderList>(list);
		}
		return page;
	}

	@Override
	public int addOrderList(SpeAccountBatchOrderList orderList) {
		SpeAccountBatchOrder bo = new SpeAccountBatchOrder();
		bo.setOrderId(orderList.getOrderId());
		bo.setUpdateTime(new Date());
		speAccountBatchOrderMapper.updateSpeAccountBatchOrder(bo);
		return speAccountBatchOrderListMapper.addOrderList(orderList);
	}

	@Override
	public int deleteSpeAccountBatchOrderList(HttpServletRequest req) {
		String orderListId = StringUtils.nullToString(req.getParameter("orderListId"));
		HttpSession session=req.getSession();
		User user=(User)session.getAttribute(Constants.SESSION_USER);
		SpeAccountBatchOrderList bol = getSpeAccountBatchOrderListByOrderListId(orderListId);
		SpeAccountBatchOrder bo = speAccountBatchOrderMapper.getSpeAccountBatchOrderById(bol.getOrderId());
		bo.setUpdateUser(user.getId().toString());
		bo.setUpdateTime(new Date());
		speAccountBatchOrderMapper.updateSpeAccountBatchOrder(bo);
		return speAccountBatchOrderListMapper.deleteSpeAccountBatchOrderList(orderListId);
	}

	@Override
	public int updateSpeAccountBatchOrderList(SpeAccountBatchOrderList orderList) {
		return speAccountBatchOrderListMapper.updateSpeAccountBatchOrderList(orderList);
	}

	@Override
	public List<SpeAccountBatchOrderList> getSpeAccountBatchOrderListFailList(String orderId) {
		return speAccountBatchOrderListMapper.getSpeAccountBatchOrderListFailList(orderId);
	}

	@Override
	public SpeAccountBatchOrderList getSpeAccountBatchOrderListByOrderIdAndPhoneNo(SpeAccountBatchOrderList orderList) {
		return speAccountBatchOrderListMapper.getSpeAccountBatchOrderListByOrderIdAndPhoneNo(orderList);
	}

	@Override
	public SpeAccountBatchOrderList getSpeAccountBatchOrderListByOrderListId(String orderListId) {
		return speAccountBatchOrderListMapper.getSpeAccountBatchOrderListByOrderListId(orderListId);
	}

}
*/