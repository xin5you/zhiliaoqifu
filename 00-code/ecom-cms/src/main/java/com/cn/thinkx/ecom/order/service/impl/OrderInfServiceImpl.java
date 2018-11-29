package com.cn.thinkx.ecom.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.common.util.DateUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.eshop.domain.EshopInf;
import com.cn.thinkx.ecom.eshop.mapper.EshopInfMapper;
import com.cn.thinkx.ecom.order.domain.OrderInf;
import com.cn.thinkx.ecom.order.mapper.OrderInfMapper;
import com.cn.thinkx.ecom.order.service.OrderInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("orderInfService")
public class OrderInfServiceImpl extends BaseServiceImpl<OrderInf> implements OrderInfService {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private OrderInfMapper orderInfMapper;
	
	@Autowired
	private EshopInfMapper eshopInfMapper;
	
	@Override
	public List<OrderInf> listOrderDetail(OrderInf orderInf) {
		return this.orderInfMapper.listOrderDetail(orderInf);
	}

	@Override
	public PageInfo<OrderInf> getOrderDetailPage(int startNum, int pageSize, OrderInf entity) {
		List<OrderInf> list = new ArrayList<OrderInf>();
		if (!StringUtil.isNullOrEmpty(entity.getBeginTime()) && !StringUtil.isNullOrEmpty(entity.getEndTime())) {
			PageHelper.startPage(startNum, pageSize);
			list = orderInfMapper.listOrderDetail(entity);
		}
		if (list != null && list.size() > 0) {
			for (OrderInf o : list){
				o.setChannel(Constants.ChannelEcomType.findByCode(o.getChannel()).getValue());
				o.setOrderType(Constants.OrderType.findByCode(o.getOrderType()).getValue());
				EshopInf eshop = new EshopInf();
				eshop.setMchntCode(o.getMerchantNo());
				eshop.setShopCode(o.getShopNo());
				EshopInf eshopInf = this.eshopInfMapper.selectByEshopInf(eshop);
				if (!StringUtil.isNullOrEmpty(eshopInf)) 
					o.setEshopName(eshopInf.getEshopName());
				else
					o.setEshopName("查无此商城");
				if (StringUtil.isNullOrEmpty(o.getPersonalName())) 
					o.setPersonalName("查无此用户");
				try {
					o.setSettleDate(DateUtil.getHalfFormatStr(o.getCreateTime()));
				} catch (Exception e) {
					logger.error("## 时间转换出错", e);
				}
			}
		}
		PageInfo<OrderInf> page = new PageInfo<OrderInf>(list);
		return page;
	}

	@Override
	public List<OrderInf> listOrderSummarizing(OrderInf orderInf) {
		return this.orderInfMapper.listOrderSummarizing(orderInf);
	}

	@Override
	public PageInfo<OrderInf> getOrderSummarizingPage(int startNum, int pageSize, OrderInf entity) {
		List<OrderInf> list = new ArrayList<OrderInf>();
		if (!StringUtil.isNullOrEmpty(entity.getBeginTime()) && !StringUtil.isNullOrEmpty(entity.getEndTime())) {
			PageHelper.startPage(startNum, pageSize);
			list = orderInfMapper.listOrderSummarizing(entity);
		}
		if (list != null && list.size() > 0) {
			for (OrderInf o : list){
				o.setChannel(Constants.ChannelEcomType.findByCode(o.getChannel()).getValue());
				EshopInf eshop = new EshopInf();
				eshop.setMchntCode(o.getMerchantNo());
				eshop.setShopCode(o.getShopNo());
				EshopInf eshopInf = this.eshopInfMapper.selectByEshopInf(eshop);
				if (!StringUtil.isNullOrEmpty(eshopInf)) 
					o.setEshopName(eshopInf.getEshopName());
				else
					o.setEshopName("查无此商城");
			}
		}
		PageInfo<OrderInf> page = new PageInfo<OrderInf>(list);
		return page;
	}

	@Override
	public OrderInf getPersonInf(OrderInf orderInf) {
		return this.orderInfMapper.getPersonInf(orderInf);
	}

}
