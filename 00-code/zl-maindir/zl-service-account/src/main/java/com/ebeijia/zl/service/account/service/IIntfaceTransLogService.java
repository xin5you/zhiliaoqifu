package com.ebeijia.zl.service.account.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.account.vo.IntfaceTransLog;


/**
 *
 * itf接口平台流水表 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
public interface IIntfaceTransLogService extends IService<IntfaceTransLog> {
	
	/**
	 * 
	* @Description: 查询外部渠道订单号
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月4日 下午2:23:50 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月4日     zhuqi           v1.0.0
	 */
	public IntfaceTransLog getItfTransLogDmsChannelTransId(String dmsRelatedKey,String transChnl);
	
	/**
	 * 
	* @Description: 创建接口层流水
	*
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月4日 下午1:35:55 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月4日     zhuqi           v1.0.0
	 */
	public IntfaceTransLog newItfTransLog(
			String dms_related_key, //外部交易订单号
			String user_id,
			String trans_id,//交易类型
			String pri_b_id,//专项类型
			String user_type,//用户类型
			String trans_chnl,//交易渠道
			String org_itf_primary_key
			);
	
	/**
	 * 
	* @Description: 增加接口层流水业务数据
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月4日 下午1:37:03 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月4日     zhuqi           v1.0.0
	 */
	public void addBizItfTransLog(
				IntfaceTransLog itfTransLog,
				BigDecimal trans_amt,
				BigDecimal upload_amt,
				String trans_curr_cd, //交易货币代码
				BigDecimal trans_fee,  //手续费
				String trans_fee_type,
				String tfr_in_user_id, //转入用户Id
				String tfr_out_user_id,//转出用户Id
				String additional_info
			);

}
