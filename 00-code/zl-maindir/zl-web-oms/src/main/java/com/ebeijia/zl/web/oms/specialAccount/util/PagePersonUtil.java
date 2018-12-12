package com.ebeijia.zl.web.oms.specialAccount.util;

import java.util.LinkedList;
import java.util.List;

import com.ebeijia.zl.web.oms.specialAccount.model.SpeAccountBatchOrderList;

public class PagePersonUtil {
	
	/*@Autowired
	@Qualifier("jedisClusterUtils")
	private static JedisClusterUtils jedisClusterUtils;*/
	
	public static List<SpeAccountBatchOrderList> getPersonInfPageList(int startNum,int pageSize,LinkedList<SpeAccountBatchOrderList> personInfList){
		List<SpeAccountBatchOrderList> list = new LinkedList<SpeAccountBatchOrderList>();
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
	
	/*public static LinkedList<SpeAccountBatchOrderList> getRedisBatchOrderList(String bathOpen){
		String getData = jedisClusterUtils.get(bathOpen); // 从缓存钟获取信息
		LinkedList<SpeAccountBatchOrderList> orderList = null;
		if (getData != null) {
			orderList = new LinkedList(JSONObject.parseArray(getData, SpeAccountBatchOrderList.class));
		}
		return orderList;
	}*/
}
