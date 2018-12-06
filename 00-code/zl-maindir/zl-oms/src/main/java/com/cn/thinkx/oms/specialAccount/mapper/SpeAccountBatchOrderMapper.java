package com.cn.thinkx.oms.specialAccount.mapper;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrder;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.github.pagehelper.PageInfo;

@Mapper
public interface SpeAccountBatchOrderMapper {

	List<SpeAccountBatchOrder> getSpeAccountBatchOrderList(SpeAccountBatchOrder order);
	
	int addSpeAccountBatchOrder(SpeAccountBatchOrder order);
	
	int updateSpeAccountBatchOrder(SpeAccountBatchOrder order);
	
	int deleteSpeAccountBatchOrder(String orderId);
	
	PageInfo<SpeAccountBatchOrder> getSpeAccountBatchOrderPage(int startNum, int pageSize, SpeAccountBatchOrder order,HttpServletRequest req);
	
	int addSpeAccountBatchOrder(SpeAccountBatchOrder order ,LinkedList<SpeAccountBatchOrderList> personInfList);
	
	SpeAccountBatchOrder getSpeAccountBatchOrderByOrderId(String orderId);
	
	SpeAccountBatchOrder getSpeAccountBatchOrderById(String orderId);
}
