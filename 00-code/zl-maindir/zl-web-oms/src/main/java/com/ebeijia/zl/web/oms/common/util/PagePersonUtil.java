package com.ebeijia.zl.web.oms.common.util;

import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;

import java.util.LinkedList;
import java.util.List;

public class PagePersonUtil {
	
	public static List<BatchOrderList> getPersonInfPageList1(int startNum,int pageSize,LinkedList<BatchOrderList> personInfList){
		List<BatchOrderList> list = new LinkedList<BatchOrderList>();
		if(personInfList != null && personInfList.size()>0){
			int startLimit = (startNum-1)*pageSize;
			int endLimit = startNum*pageSize-1;
			int total = personInfList.size();
			
			if(total <= endLimit){
				endLimit = total-1;
			}
			for(int i = startLimit; i <= endLimit; i++){
				list.add(personInfList.get(i));
			}
		}
		return list;
	}
}
