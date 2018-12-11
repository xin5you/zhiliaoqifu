package com.cn.thinkx.oms.specialAccount.service;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrder;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.cn.thinkx.oms.sys.model.User;
import com.github.pagehelper.PageInfo;

public interface SpeAccountBatchOrderService {
	
	/**
	 * 查询订单列表
	 * @param order
	 * @return
	 */
	List<SpeAccountBatchOrder> getSpeAccountBatchOrderList(SpeAccountBatchOrder order);
	
	/**
	 * 添加订单
	 * @param order
	 * @return
	 */
	int addSpeAccountBatchOrder(SpeAccountBatchOrder order);
	
	/**
	 * 修改订单
	 * @param order
	 * @return
	 */
	int updateSpeAccountBatchOrder(SpeAccountBatchOrder order);
	
	/**
	 * 删除订单
	 * @param orderId
	 * @return
	 */
	int deleteSpeAccountBatchOrder(String orderId);
	
	/**
	 * 查询订单列表(带分页)
	 * @param startNum
	 * @param pageSize
	 * @param order
	 * @param req
	 * @return
	 */
	PageInfo<SpeAccountBatchOrder> getSpeAccountBatchOrderPage(int startNum, int pageSize, SpeAccountBatchOrder order,HttpServletRequest req);
	
	/**
	 * 保存订单、订单明细
	 * @param order
	 * @param personInfList
	 * @return
	 */
	int addSpeAccountBatchOrder(HttpServletRequest req ,LinkedList<SpeAccountBatchOrderList> personInfList, String orderType);
	
	SpeAccountBatchOrder getSpeAccountBatchOrderByOrderId(String orderId);
	
	/**
	 * 根据主键查询订单
	 * @param orderId
	 * @return
	 */
	SpeAccountBatchOrder getSpeAccountBatchOrderById(String orderId);
	
	
	/**
	 * 批量开户
	 * @param orderId 订单
	 * @param user 用户 
	 */
	int batchSpeAccountBatchOpenAccountITF(String orderId, User user,String commitStat);
	
	
	/**
	 * 批量充值
	 * @param orderId
	 * @param user
	 * @param commitStat
	 */
	int batchSpeAccountRechargeITF(String orderId, User user,String commitStat);
	
	int deleteOpenAccountCommit(String orderId, User user);
}
