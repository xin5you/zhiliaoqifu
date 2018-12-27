package com.ebeijia.zl.web.cms.platforder.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.GoodsEcomCodeTypeEnum;
import com.ebeijia.zl.common.utils.enums.PlatfOrderPayStatEnum;
import com.ebeijia.zl.common.utils.enums.SubOrderStatusEnum;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfShopOrder;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPlatfOrderService;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPlatfShopOrderService;
import com.ebeijia.zl.web.cms.platforder.service.PlatfOrderInfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("platfOrderInfService")
public class PlatfOrderInfServiceImpl implements PlatfOrderInfService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ITbEcomPlatfOrderService platfOrderService;

	@Autowired
	private ITbEcomPlatfShopOrderService platfShopOrderService;


	@Override
	public PageInfo<TbEcomPlatfOrder> getPlatforderListPage(int startNum, int pageSize, TbEcomPlatfOrder entity) {
		List<TbEcomPlatfOrder> platfOrderList = new ArrayList<TbEcomPlatfOrder>();

		PageHelper.startPage(startNum, pageSize);
		platfOrderList = platfOrderService.getPlatfOrderList(entity);
		if (platfOrderList != null && platfOrderList.size() > 0) {
			for (TbEcomPlatfOrder platfOrder : platfOrderList) {
				platfOrder.setPayStatus(PlatfOrderPayStatEnum.findByCode(platfOrder.getPayStatus()).getValue());
			}
		}
		PageInfo<TbEcomPlatfOrder> page = new PageInfo<TbEcomPlatfOrder>(platfOrderList);
		return page;
	}

	@Override
	public PageInfo<TbEcomPlatfShopOrder> getPlatfShopOrderListPageByPlatOrder(int startNum, int pageSize, TbEcomPlatfShopOrder entity) {
		List<TbEcomPlatfShopOrder> platfShopOrderList = new ArrayList<TbEcomPlatfShopOrder>();

		PageHelper.startPage(startNum, pageSize);
		platfShopOrderList = platfShopOrderService.getPlatfShopOrderListByPlatfOrder(entity);
		if (platfShopOrderList != null && platfShopOrderList.size() > 0) {
			for (TbEcomPlatfShopOrder platfShopOrder : platfShopOrderList) {
				platfShopOrder.setEcomCode(GoodsEcomCodeTypeEnum.findByCode(platfShopOrder.getEcomCode()).getValue());
				platfShopOrder.setSubOrderStatusName(SubOrderStatusEnum.findByCode(platfShopOrder.getSubOrderStatus()).getValue());
			}
		}
		PageInfo<TbEcomPlatfShopOrder> page = new PageInfo<TbEcomPlatfShopOrder>(platfShopOrderList);
		return page;
	}

	@Override
	public PageInfo<TbEcomPlatfShopOrder> getPlatfShopOrderListPage(int startNum, int pageSize, TbEcomPlatfShopOrder entity) {
		List<TbEcomPlatfShopOrder> platfShopOrderList = new ArrayList<TbEcomPlatfShopOrder>();

		PageHelper.startPage(startNum, pageSize);
		platfShopOrderList = platfShopOrderService.getPlatfShopOrderList(entity);
		if (platfShopOrderList != null && platfShopOrderList.size() > 0) {
			for (TbEcomPlatfShopOrder platfShopOrder : platfShopOrderList) {
				platfShopOrder.setEcomCode(GoodsEcomCodeTypeEnum.findByCode(platfShopOrder.getEcomCode()).getValue());
				platfShopOrder.setSubOrderStatusName(SubOrderStatusEnum.findByCode(platfShopOrder.getSubOrderStatus()).getValue());
				platfShopOrder.setPayStatus(PlatfOrderPayStatEnum.findByCode(platfShopOrder.getPayStatus()).getValue());
			}
		}
		PageInfo<TbEcomPlatfShopOrder> page = new PageInfo<TbEcomPlatfShopOrder>(platfShopOrderList);
		return page;
	}

}
