package com.cn.thinkx.ecom.platforder.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.OrderProductItem;
import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.basics.order.mapper.PlatfOrderMapper;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.platforder.service.OrderProductItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("orderProductItemInfService")
public class OrderProductItemServiceImpl implements OrderProductItemService{
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PlatfOrderMapper platfOrderMapper;

	@Override
	public PageInfo<PlatfOrder> getOrderShipListPage(int startNum, int pageSize, PlatfOrder entity) {
		PageHelper.startPage(startNum, pageSize);
		List<PlatfOrder> platfOrderList = platfOrderMapper.getPlatfOrderGoodsByMemberId(entity);
		for (PlatfOrder platfOrder : platfOrderList) {
			List<PlatfShopOrder> platfShopOrderList = platfOrder.getPlatfShopOrder();
			if(platfShopOrderList != null && platfShopOrderList.size() > 0){
				for (PlatfShopOrder platfShopOrder : platfShopOrderList) {
					List<OrderProductItem> orderProductItems = platfShopOrder.getOrderProductItems();
					logger.info("platfShopOrder.getEcomCode()："+platfShopOrder.getEcomCode());
					if(!StringUtil.isNullOrEmpty(platfShopOrder.getEcomCode()))
						platfShopOrder.setEcomCode(Constants.GoodsEcomCodeType.findByCode(platfShopOrder.getEcomCode()).getValue());
					for (OrderProductItem orderProductItem : orderProductItems) {
						if (!StringUtil.isNullOrEmpty(platfOrder.getOrderFreightAmt()))
							orderProductItem.setProductPrice(NumberUtils.RMBCentToYuan("" + orderProductItem.getProductPrice()));
					}
				}
			}
		}
		PageInfo<PlatfOrder> page = new PageInfo<PlatfOrder>(platfOrderList);
		return page;
	}
	
}
