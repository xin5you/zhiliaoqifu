package com.ebeijia.zl.web.oms.batchOrder.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrder;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.github.pagehelper.PageInfo;

public interface BatchOrderService extends IService<BatchOrder> {

	/**
	 * 查看批量订单信息列表
	 * @param order
	 * @return
	 */
	List<BatchOrder> getBatchOrderList(BatchOrder order);

	/**
	 * 查看批量订单信息列表（含分页）
	 * @param startNum
	 * @param pageSize
	 * @param order
	 * @param req
	 * @return
	 */
	PageInfo<BatchOrder> getBatchOrderPage(int startNum, int pageSize, BatchOrder order, HttpServletRequest req);

	/**
	 * 添加开户批量订单及批量订单明细信息
	 * @param req
	 * @param personInfList
	 * @param orderType
	 * @param accountType
	 * @param isPlatform
	 * @return
	 */
	int addBatchOrderAndOrderList(HttpServletRequest req, LinkedList<BatchOrderList> personInfList, String orderType, String accountType, String isPlatform);

	/**
	 * 根据批量订单号查询批量订单信息
	 * @param orderId
	 * @return
	 */
	BatchOrder getBatchOrderById(String orderId);

	/**
	 * 删除批量订单信息
	 * @param orderId
	 * @param user
	 * @return
	 */
	int deleteOpenAccountOrRechargeCommit(String orderId, User user);

	/**
	 * 编辑批量订单及订单明细状态
	 * @param orderId
	 * @param orderStat
	 * @param user
	 * @return
	 */
	int updateBatchOrderAndOrderListByOrderStat(String orderId, String orderStat, User user);

	/**
	 * 开户接口
	 * @param orderId
	 * @param user
	 * @param orderStat
	 * @return
	 */
	Map<String, Object> batchOpenAccountITF(String orderId, User user, String orderStat);

	/**
	 * 充值接口
	 * @param orderId
	 * @param user
	 * @param orderStat
	 * @return
	 */
	int batchRechargeITF(String orderId, User user, String orderStat);

	/**
	 * 企业用户充值（由企业账户转账至用户账户）
	 * @param orderId
	 * @param user
	 * @param orderStat
	 * @return
	 */
	Map<String, Object> batchTransferAccountITF(String orderId, User user, String orderStat);

	/**
	 * 添加批量订单信息
	 * @param order
	 * @return
	 */
	int addBatchOrder(BatchOrder order);

	/**
	 * 根据批量订单号 查询批量订单信息
	 * @param orderId
	 * @return
	 */
	BatchOrder getBatchOrderByOrderId(String orderId);
}
