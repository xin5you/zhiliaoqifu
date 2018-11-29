package com.cn.thinkx.ecom.refund.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn.thinkx.ecom.basics.order.domain.ReturnsOrder;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.github.pagehelper.PageInfo;

public interface RefundService {

	PageInfo<ReturnsOrder> getRefundsOrderPage(int startNum, int pageSize, ReturnsOrder refund);

	BaseResult<Object> updateRefund(HttpServletRequest req, HttpServletResponse resp);
}
