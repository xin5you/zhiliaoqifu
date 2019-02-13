package com.ebeijia.zl.web.cms.platforder.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import com.ebeijia.zl.shop.dao.order.domain.*;
import com.ebeijia.zl.shop.dao.order.service.*;
import com.ebeijia.zl.web.cms.platforder.service.PlatfOrderInfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service("platfOrderInfService")
public class PlatfOrderInfServiceImpl implements PlatfOrderInfService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ITbEcomPlatfOrderService platfOrderService;

	@Autowired
	private ITbEcomPlatfShopOrderService platfShopOrderService;

	@Autowired
	private ITbEcomOrderProductItemService ecomOrderProductItemService;

	@Autowired
	private ITbEcomExpressPlatfService ecomExpressPlatfService;

	@Autowired
	private ITbEcomOrderExpressPlatfService ecomOrderExpressPlatfService;

	@Autowired
	private ProviderInfFacade providerInfFacade;

	@Override
	public PageInfo<TbEcomPlatfOrder> getPlatfOrderListPage(int startNum, int pageSize, TbEcomPlatfOrder entity) {
		PageHelper.startPage(startNum, pageSize);
		List<TbEcomPlatfOrder> platfOrderList = platfOrderService.getPlatfOrderList(entity);
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
		PageHelper.startPage(startNum, pageSize);
		List<TbEcomPlatfShopOrder> platfShopOrderList = platfShopOrderService.getPlatfShopOrderListByPlatfOrder(entity);
		if (platfShopOrderList != null && platfShopOrderList.size() > 0) {
			for (TbEcomPlatfShopOrder platfShopOrder : platfShopOrderList) {
				ProviderInf providerInf = null;
				try {
					providerInf = providerInfFacade.getProviderInfById(platfShopOrder.getEcomCode());
				} catch (Exception e) {
					logger.error("根据ecom_code查询供应商信息异常", platfShopOrder.getEcomCode());
				}
				if (!StringUtil.isNullOrEmpty(providerInf)) {
					platfShopOrder.setEcomCode(providerInf.getProviderName());
				}
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
				ProviderInf providerInf = null;
				try {
					providerInf = providerInfFacade.getProviderInfById(platfShopOrder.getEcomCode());
				} catch (Exception e) {
					logger.error("根据ecom_code查询供应商信息异常", platfShopOrder.getEcomCode());
				}
				if (!StringUtil.isNullOrEmpty(providerInf)) {
					platfShopOrder.setProviderName(providerInf.getProviderName());
				}
				platfShopOrder.setSubOrderStatusName(SubOrderStatusEnum.findByCode(platfShopOrder.getSubOrderStatus()).getValue());
				platfShopOrder.setPayStatus(PlatfOrderPayStatEnum.findByCode(platfShopOrder.getPayStatus()).getValue());
			}
		}
		PageInfo<TbEcomPlatfShopOrder> page = new PageInfo<TbEcomPlatfShopOrder>(platfShopOrderList);
		return page;
	}

	@Override
	public BaseResult<Object> updateOrderGoodsDeliverCheck(HttpServletRequest req) {
		String sOrderId = req.getParameter("sOrderId");
		if (StringUtil.isNullOrEmpty(sOrderId)) {
			logger.error("## sOrderId{}为空", sOrderId);
			return ResultsUtil.error(ExceptionEnum.PlatfOrderNewsEnum.PlatfOrderNews_02.getCode(), ExceptionEnum.PlatfOrderNewsEnum.PlatfOrderNews_02.getMsg());
		}
		//查询二级订单信息
		TbEcomPlatfShopOrder platfShopOrder = platfShopOrderService.getById(sOrderId);
		if (platfShopOrder == null ) {
			return ResultsUtil.error(ExceptionEnum.PlatfOrderNewsEnum.PlatfOrderNews_01.getCode(), ExceptionEnum.PlatfOrderNewsEnum.PlatfOrderNews_01.getMsg());
		}
		//查询二级订单货品信息
		TbEcomOrderProductItem orderProductItem = ecomOrderProductItemService.getOrderProductItemBySOrderId(sOrderId);
		if (orderProductItem == null) {
			logger.error("## 根据sOrderId{}查询货品信息为空", sOrderId);
			return ResultsUtil.error(ExceptionEnum.PlatfOrderNewsEnum.PlatfOrderNews_03.getCode(), ExceptionEnum.PlatfOrderNewsEnum.PlatfOrderNews_03.getMsg());
		}
		//封装二级订单物流信息
		TbEcomExpressPlatf ecpressPlatf = new TbEcomExpressPlatf();
		ecpressPlatf.setPackId(IdUtil.getNextId());
		ecpressPlatf.setSOrderId(sOrderId);
		ecpressPlatf.setPackageNo("");
		ecpressPlatf.setDeliveryTime(System.currentTimeMillis());
		ecpressPlatf.setPackageStat(PackageStatEnum.packageStat_10.getCode());
		ecpressPlatf.setIsSign(IsSignEnum.packageStat_0.getCode());
		ecpressPlatf.setEcomCode(platfShopOrder.getEcomCode());
		//封装二级订单物流关联信息
		TbEcomOrderExpressPlatf orderExpressPlatf = new TbEcomOrderExpressPlatf();
		orderExpressPlatf.setOPackId(IdUtil.getNextId());
		orderExpressPlatf.setOItemId(orderProductItem.getOItemId());
		orderExpressPlatf.setSkuCode(orderProductItem.getProductId());
		orderExpressPlatf.setSaleCount(orderProductItem.getProductNum());
		orderExpressPlatf.setPackId(ecpressPlatf.getPackId());
		//新增二级订单物流信息
		if (!ecomExpressPlatfService.save(ecpressPlatf)) {
			logger.error("## 添加订单sOrderId{}物流信息失败", sOrderId);
			return ResultsUtil.error(ExceptionEnum.PlatfOrderNewsEnum.PlatfOrderNews_04.getCode(), ExceptionEnum.PlatfOrderNewsEnum.PlatfOrderNews_04.getMsg());
		}
		//新增二级订单物流关联信息
		if (!ecomOrderExpressPlatfService.save(orderExpressPlatf)) {
			logger.error("## 添加订单sOrderId{}物流关联信息失败", sOrderId);
			return ResultsUtil.error(ExceptionEnum.PlatfOrderNewsEnum.PlatfOrderNews_04.getCode(), ExceptionEnum.PlatfOrderNewsEnum.PlatfOrderNews_04.getMsg());
		}

		//更新二级订单订单状态为已发货
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		platfShopOrder.setSubOrderStatus(SubOrderStatusEnum.SOS12.getCode());
		platfShopOrder.setUpdateTime(System.currentTimeMillis());
		platfShopOrder.setUpdateUser(user.getId());
		platfShopOrder.setLockVersion(platfShopOrder.getLockVersion() + 1);
		if (!platfShopOrderService.updateById(platfShopOrder)) {
			return ResultsUtil.error(ExceptionEnum.PlatfOrderNewsEnum.PlatfOrderNews_01.getCode(), ExceptionEnum.PlatfOrderNewsEnum.PlatfOrderNews_01.getMsg());
		}

		return ResultsUtil.success();
	}

}
