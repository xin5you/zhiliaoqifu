package com.cn.thinkx.ecom.refund.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.basics.order.domain.ReturnsOrder;
import com.cn.thinkx.ecom.basics.order.service.PlatfShopOrderService;
import com.cn.thinkx.ecom.basics.order.service.ReturnsOrderService;
import com.cn.thinkx.ecom.common.constants.Constants.RefundStatus;
import com.cn.thinkx.ecom.common.constants.Constants.ReturnsStatus;
import com.cn.thinkx.ecom.common.constants.Constants.ReturnsType;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum.EcomOrderRefundNews;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.refund.service.RefundService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class RefundServiceImpl implements RefundService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ReturnsOrderService returnsOrderService;

	@Autowired
	private PlatfShopOrderService platfShopOrderService;

	@Override
	public PageInfo<ReturnsOrder> getRefundsOrderPage(int startNum, int pageSize, ReturnsOrder refund) {
		PageHelper.startPage(startNum, pageSize);
		List<ReturnsOrder> list = null;
		try {
			list = returnsOrderService.getReturnsOrderListByReturnsOrder(refund);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PageInfo<ReturnsOrder> page = new PageInfo<ReturnsOrder>(list);
		page.getList().stream().filter(ret -> {
			ret.setRetStatType(ReturnsStatus.findByCode(ret.getReturnsStatus()).getValue());
			ret.setRetType(ReturnsType.findByCode(ret.getReturnsType()).getValue());
			if(!StringUtil.isNullOrEmpty(ret.getRefundStatus())){
				ret.setRefStatType(RefundStatus.findByCode(ret.getRefundStatus()).getValue());
			}
			if(!StringUtil.isNullOrEmpty(ret.getRefundAmt())){
				ret.setRefundAmt(NumberUtils.RMBCentToYuan(ret.getRefundAmt()));
			}
			return true;
		}).collect(Collectors.toList());
		return page;
	}

	@Override
	public BaseResult<Object> updateRefund(HttpServletRequest req, HttpServletResponse resp) {
		String sOrderId = req.getParameter("sOrderId");
		try {
			if (StringUtil.isNullOrEmpty(sOrderId)) {
				logger.error("## 退款出错,二级订单号sOrderId是空：[{}]", sOrderId);
				return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
			}
			PlatfShopOrder platfShopOrder = platfShopOrderService.selectByPrimaryKey(sOrderId);
			if (!StringUtil.isNullOrEmpty(platfShopOrder)) {
				/** 调用退款 */
				if(!platfShopOrderService.doHkbStoreRefund(platfShopOrder)){
					return ResultsUtil.error(EcomOrderRefundNews.PON01.getCode(), EcomOrderRefundNews.PON01.getMsg());
				}
			} else {
				logger.error("## 退款出错,查询退款订单是空，platfShopOrder：[{}]", platfShopOrder.toString());
				return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
			}
		} catch (Exception e) {
			logger.error("## 退款出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
		return ResultsUtil.success();
	}
}
