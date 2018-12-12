package com.ebeijia.zl.web.oms.specialAccount.mapper;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Mapper;

import com.ebeijia.zl.web.oms.specialAccount.model.SpeAccountBatchOrder;
import com.ebeijia.zl.web.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.github.pagehelper.PageInfo;

@Mapper
public interface SpeAccountBatchOrderMapper {

	List<SpeAccountBatchOrder> getSpeAccountBatchOrderList(SpeAccountBatchOrder order);
	
	int addSpeAccountBatchOrder(SpeAccountBatchOrder order);
	
	int updateSpeAccountBatchOrder(SpeAccountBatchOrder order);
	
	int deleteSpeAccountBatchOrder(String orderId);
	
	PageInfo<SpeAccountBatchOrder> getSpeAccountBatchOrderPage(int startNum, int pageSize, SpeAccountBatchOrder order,HttpServletRequest req);
	
	int addSpeAccountBatchOrder(LinkedList<SpeAccountBatchOrderList> personInfList);
	
	SpeAccountBatchOrder getSpeAccountBatchOrderById(String orderId);
	
	SpeAccountBatchOrder getSpeAccountBatchOrderByOrderId(String orderId);
	
}
