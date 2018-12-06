package com.ebeijia.zl.service.account.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.facade.account.vo.IntfaceTransLog;
import com.ebeijia.zl.service.account.mapper.IntfaceTransLogMapper;
import com.ebeijia.zl.service.account.service.IIntfaceTransLogService;

/**
 *
 * itf接口平台流水表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,rollbackFor=Exception.class)
public class IntfaceTransLogServiceImpl extends ServiceImpl<IntfaceTransLogMapper, IntfaceTransLog> implements IIntfaceTransLogService{
	
	@Autowired
	private IntfaceTransLogMapper  intfaceTransLogMapper;
	
	public  boolean updateById(IntfaceTransLog entity,boolean respCode){
		 if(respCode){
			 entity.setRespCode("00");
		 }else{
			 entity.setRespCode("99");
		 }
		 
		 return super.updateById(entity);
	 }
		
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
	public IntfaceTransLog getItfTransLogDmsChannelTransId(String dmsRelatedKey,String transChnl){
		QueryWrapper<IntfaceTransLog> queryWrapper = new QueryWrapper<IntfaceTransLog>();
		queryWrapper.eq("dms_related_key", dmsRelatedKey);
		queryWrapper.eq("trans_chnl", transChnl);
		queryWrapper.eq("data_stat", DataStatEnum.TRUE_STATUS.getCode());
		IntfaceTransLog intfaceTransLog= intfaceTransLogMapper.selectOne(queryWrapper);
		return intfaceTransLog;
	}

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
			){
		IntfaceTransLog itfTransLog=new IntfaceTransLog();
		
		itfTransLog.setItfPrimaryKey(IdUtil.getNextId());
		itfTransLog.setOrgItfPrimaryKey(org_itf_primary_key);
		itfTransLog.setDmsRelatedKey(dms_related_key);
		itfTransLog.setUserId(user_id);
		itfTransLog.setTransId(trans_id);
		itfTransLog.setPriBId(pri_b_id);
		itfTransLog.setUserType(user_type);
		itfTransLog.setTransChnl(trans_chnl);
		itfTransLog.setTransSt("0");
		createSystemDataLog(itfTransLog);
		return itfTransLog;
	}
	
	
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
				String tfr_in_bId,
				String tfr_out_user_id,//转出用户Id
				String tfr_out_bId,
				String additional_info
			){
		itfTransLog.setTransAmt(trans_amt);
		itfTransLog.setUploadAmt(upload_amt);
		itfTransLog.setTransCurrCd(trans_curr_cd);
		itfTransLog.setTransFee(trans_fee);
		itfTransLog.setTransFeeType(trans_fee_type);
		itfTransLog.setTfrInUserId(tfr_in_user_id);
		itfTransLog.setTfrInBId(tfr_in_bId);
		itfTransLog.setTfrOutUserId(tfr_out_user_id);
		itfTransLog.setTfrOutBId(tfr_out_bId);
		itfTransLog.setAdditionalInfo(additional_info);
	}
	
	private void createSystemDataLog(IntfaceTransLog entity){
		entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		entity.setCreateTime(System.currentTimeMillis());
		entity.setCreateUser("99999999");
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setUpdateUser("99999999");
		entity.setLockVersion(0);
	}
}
