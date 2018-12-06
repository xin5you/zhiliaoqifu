package com.cn.thinkx.oms.specialAccount.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.github.pagehelper.PageInfo;

@Mapper
public interface SpeAccountBatchOrderListMapper {

	List<SpeAccountBatchOrderList> getSpeAccountBatchOrderListList(String orderId);
	
	int addSpeAccountBatchOrderList(List<SpeAccountBatchOrderList> list);
	
	PageInfo<SpeAccountBatchOrderList> getSpeAccountBatchOrderListPage(int startNum, int pageSize, String orderId);
	
	int addOrderList(SpeAccountBatchOrderList orderList);
	
	int deleteSpeAccountBatchOrderList(String orderListId);
	
	int updateSpeAccountBatchOrderList(SpeAccountBatchOrderList orderList);
	
	List<SpeAccountBatchOrderList> getSpeAccountBatchOrderListFailList(String orderId);
	
	SpeAccountBatchOrderList getSpeAccountBatchOrderListByOrderIdAndPhoneNo(SpeAccountBatchOrderList orderList);
	
	SpeAccountBatchOrderList getSpeAccountBatchOrderListByOrderListId(String orderListId);
}
