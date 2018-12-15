package com.ebeijia.zl.service.account.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.AccountCardAttrEnum;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.account.dto.IntfaceTransLog;
import com.ebeijia.zl.facade.account.dto.TransLog;
import com.ebeijia.zl.facade.account.exceptions.AccountBizException;
import com.ebeijia.zl.facade.account.req.AccountTxnVo;
import com.ebeijia.zl.service.account.mapper.TransLogMapper;
import com.ebeijia.zl.service.account.service.IAccountInfService;
import com.ebeijia.zl.service.account.service.ITransLogService;

/**
 *
 * 账户交易流水 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,rollbackFor=Exception.class)
public class TransLogServiceImpl extends ServiceImpl<TransLogMapper, TransLog> implements ITransLogService{

	private  final Logger log = LoggerFactory.getLogger(TransLogServiceImpl.class);
	
	
	@Autowired
	private IAccountInfService accountInfService;
	
	/**
	* 
	* @Description: 创建账户交易流水
	* @param:intfaceTransLog 接口層流水
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 上午11:35:33 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	public boolean execute(IntfaceTransLog intfaceTransLog) throws AccountBizException{
		log.info("==>execute create transLog begin...");
		
		/**
		 * 创建交易流水记录
		 */
		List<TransLog> voList=doTransLog(intfaceTransLog);
		
		boolean b=this.execute(voList);
		
		log.info("==>execute create transLog end...<==");
		return b;
	}
	
	/**
	* @Description: 交易流水批量操作账户
	* @param:voList 交易记录列表
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 下午1:08:48 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	public boolean execute(List<TransLog> voList) throws AccountBizException{
		
		List<TransLog> sortList = new ArrayList<>();
		if(voList !=null && voList.size()>1){
	    	//交易流水顺序执行
	    	voList.stream().sorted((e1, e2) -> {
				return Integer.compare(e1.getOrder(), e2.getOrder());
			}).forEach(e -> sortList.add(e));
		}else{
			sortList.addAll(voList);
		}
		voList.clear();
		
		boolean f=	this.saveBatch(sortList); //批量保存交易流水

		if(f){
			return accountInfService.execute(sortList);
		}
		return f;
	}
	
	/**
	* @param:intfaceTransLog 接口调用流水信息
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 下午2:06:03 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	private List<TransLog> doTransLog(IntfaceTransLog intfaceTransLog)throws AccountBizException{
		
		List<TransLog> voList=new ArrayList<TransLog>();
		
		/**
			MB10("B10", "商户消费"),
			MB20("B20", "商户充值"),
			MB80("B80", "商户开户"),
			MB40("B40", "商户转账"),
			MB50("B50", "企业员工充值"),
			MB90("B90", "商户收款"), 
		
			CW80("W80", "企业员工开户"),
			CW81("W81", "密码重置"),
			CW10("W10", "商品消费"),
			CW50("W50", "员工充值"),
			CW71("W71", "微信支付"),
			CW20("W20", "购买代金券"),
			CW11("W11", "退款"),
			CW74("W74", "退款（快捷）"),
			CW40("W40", "员工转账"),
			CW90("W90", "权益转让"),
			CW91("W91", "商户收款");
		 */
		
		if (TransCode.MB10.getCode().equals(intfaceTransLog.getTransId())){
			//商户消费
			this.addToVoList(voList, intfaceTransLog,null,null, AccountCardAttrEnum.SUB.getValue(), 0);
			
			TransLog transLog2=null;
			int order=0;
			 List<AccountTxnVo> transList= JSON.parseArray(intfaceTransLog.getRemarks(), AccountTxnVo.class);
			 
			for (AccountTxnVo txnVo : transList) {
				transLog2=new TransLog();
				this.newTransLog(intfaceTransLog, transLog2);
				transLog2.setTxnPrimaryKey(IdUtil.getNextId());
				transLog2.setPriBId(txnVo.getTxnBId());
				transLog2.setUploadAmt(txnVo.getTxnAmt());
				transLog2.setCardAttr(AccountCardAttrEnum.OPER.getValue());
				addToVoList(voList,transLog2,order);
				order++;
			}
			
			
		}else if (TransCode.MB20.getCode().equals(intfaceTransLog.getTransId())){
			//商户充值
			this.addToVoList(voList, intfaceTransLog,null,null, AccountCardAttrEnum.ADD.getValue(), 0);
			
		}else if (TransCode.MB40.getCode().equals(intfaceTransLog.getTransId())){
			
			//商户转账
			this.addToVoList(voList, intfaceTransLog, intfaceTransLog.getTfrOutUserId(), intfaceTransLog.getTfrOutBId(), AccountCardAttrEnum.SUB.getValue(), 0);
			
			this.addToVoList(voList, intfaceTransLog, intfaceTransLog.getTfrInUserId(), intfaceTransLog.getTfrInBId(), AccountCardAttrEnum.ADD.getValue(), 1);
		}
		else if (TransCode.MB50.getCode().equals(intfaceTransLog.getTransId())){
			//企业员工充值 从企业的通卡账户，转入到员工的专项账户里面
			this.addToVoList(voList, intfaceTransLog, intfaceTransLog.getTfrOutUserId(), intfaceTransLog.getTfrOutBId(), AccountCardAttrEnum.SUB.getValue(), 0);
			
			TransLog transLog2=new TransLog();
			this.newTransLog(intfaceTransLog, transLog2);
			transLog2.setTxnPrimaryKey(IdUtil.getNextId());
			transLog2.setUserId(intfaceTransLog.getTfrInUserId());
			transLog2.setPriBId(intfaceTransLog.getTfrInBId());
			transLog2.setCardAttr(AccountCardAttrEnum.ADD.getValue());
			transLog2.setTransId(TransCode.CW50.getCode());
			transLog2.setUserType(UserType.TYPE100.getCode());
			addToVoList(voList,transLog2,1);
		}else if (TransCode.CW71.getCode().equals(intfaceTransLog.getTransId())){
			//快捷支付 先充值到通卡账户，再从通卡账户扣除
			this.addToVoList(voList, intfaceTransLog, null, null, AccountCardAttrEnum.ADD.getValue(), 0);
			this.addToVoList(voList, intfaceTransLog, null, null, AccountCardAttrEnum.SUB.getValue(), 1);
			
		}else if (TransCode.CW10.getCode().equals(intfaceTransLog.getTransId())){
			//企业员工消费
			this.addToVoList(voList, intfaceTransLog, null, null, AccountCardAttrEnum.SUB.getValue(), 0);
			
		}else if (TransCode.CW80.getCode().equals(intfaceTransLog.getTransId()) || TransCode.MB80.getCode().equals(intfaceTransLog.getTransId())){
			//企业员工开户，多个专项类型开户
			TransLog transLog2=null;
			int order=0;
			Set<String> bIds=intfaceTransLog.getBIds();
			for (String bId : bIds) {
				transLog2=new TransLog();
				this.newTransLog(intfaceTransLog, transLog2);
				transLog2.setTxnPrimaryKey(IdUtil.getNextId());
				transLog2.setPriBId(bId);
				transLog2.setCardAttr(AccountCardAttrEnum.OPER.getValue());
				addToVoList(voList,transLog2,order);
				order++;
			}
			
		}else{
			this.addToVoList(voList, intfaceTransLog, null, null, null, 0);
		}
		return voList;
	}
	
	private void addToVoList(List<TransLog> voList,IntfaceTransLog intfaceTransLog,String userId,String bId,String cardAttr ,int order){
		TransLog transLog=new TransLog();
		transLog.setTxnPrimaryKey(IdUtil.getNextId());
		this.newTransLog(intfaceTransLog, transLog);
		if(StringUtil.isNotEmpty(userId)){
			transLog.setUserId(userId);
		}
		if(StringUtil.isNotEmpty(bId)){
			transLog.setPriBId(bId);
		}
		if(StringUtil.isNotEmpty(cardAttr)){

			transLog.setCardAttr(cardAttr);
		}
		addToVoList(voList,transLog,order);
	}

	
	private void addToVoList(List<TransLog> voList,TransLog transLog,int order){
		transLog.setOrder(order);
		createTransLog(transLog);//基本信息
		voList.add(transLog);
	}
	
	
	
	private void newTransLog(IntfaceTransLog intfaceTransLog,TransLog transLog){
		
		transLog.setItfPrimaryKey(intfaceTransLog.getItfPrimaryKey()); //接口层流水
		transLog.setTransDesc(intfaceTransLog.getTransDesc());
		transLog.setTransNumber(intfaceTransLog.getTransNumber());
		transLog.setInsCode(intfaceTransLog.getInsCode());
		transLog.setMchntCode(intfaceTransLog.getMchntCode());
		transLog.setShopCode(intfaceTransLog.getShopCode());
		transLog.setTransAmt(intfaceTransLog.getTransAmt());
		transLog.setUploadAmt(intfaceTransLog.getTransAmt());
		transLog.setTransChnl(intfaceTransLog.getTransChnl());
		transLog.setTransCurrCd(intfaceTransLog.getTransCurrCd());
		transLog.setProductCode(intfaceTransLog.getProductCode());
		transLog.setTransId(intfaceTransLog.getTransId());
		transLog.setUserId(intfaceTransLog.getUserId());
		transLog.setUserType(intfaceTransLog.getUserType());
		transLog.setPriBId(intfaceTransLog.getPriBId());
		transLog.setCardAttr(AccountCardAttrEnum.OPER.getValue());
	}
	

	
	private void createTransLog(TransLog transLog){
		transLog.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		transLog.setCreateTime(System.currentTimeMillis());
		transLog.setCreateUser("99999999");
		transLog.setUpdateTime(System.currentTimeMillis());
		transLog.setUpdateUser("99999999");
		transLog.setLockVersion(0);
	}
}
