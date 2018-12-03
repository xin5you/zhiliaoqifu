package com.cn.thinkx.ecom.basics.order.mapper;

import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlatfOrderMapper extends BaseDao<PlatfOrder> {

	List<PlatfOrder> getPlatfOrderList(PlatfOrder po);

	String getPrimaryKey();

	/**
	 * 通过会员id查看订单信息
	 * 
	 * @param po
	 * @return
	 */
	List<PlatfOrder> getPlatfOrderGoodsByMemberId(PlatfOrder po);
	
	/**
	 * 将半个小时以前得订单修改为已取消状态
	 * @return
	 */
	int updateOlatfOrder();
}
