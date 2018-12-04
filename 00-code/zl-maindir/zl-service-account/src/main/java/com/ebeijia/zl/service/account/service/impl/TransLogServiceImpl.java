package com.ebeijia.zl.service.account.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.enums.AccountCardAttrEnum;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.facade.account.vo.IntfaceTransLog;
import com.ebeijia.zl.facade.account.vo.TransLog;
import com.ebeijia.zl.service.account.mapper.TransLogMapper;
import com.ebeijia.zl.service.account.service.ITransLogService;

/**
 *
 * 账户交易流水 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Service
public class TransLogServiceImpl extends ServiceImpl<TransLogMapper, TransLog> implements ITransLogService{

	private static final Logger log = LoggerFactory.getLogger(TransLogServiceImpl.class);
	/**
	* 
	* @Function: ITransLogService.java
	* @Description: 创建账户交易流水
	*
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
	public boolean execute(IntfaceTransLog intfaceTransLog) throws Exception{
		log.info("==>execute create transLog begin...");
		
		/**
		 * 创建交易流水记录
		 */
		List<TransLog> voList=this.createTransLog(intfaceTransLog);
		this.execute(voList);
		
		log.info("==>execute create transLog end...<==");
		return false;
	}
	
	/**
	 * 
	* @Function: TransLogServiceImpl.java
	* @Description: 交易流水批量操作账户
	*
	* @param:voList 交易记录列表
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 下午1:08:48 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	public void execute(List<TransLog> voList) {
			
		if(voList !=null && voList.size()>1){
	    	List<TransLog> sortList = new ArrayList<>();
	    	
	    	//交易流水顺序执行
	    	voList.stream().sorted((e1, e2) -> {
				return Integer.compare(e1.getOrder(), e2.getOrder());
			}).forEach(e -> sortList.add(e));
	    	
	    	voList.clear(); //
	    	voList=sortList; //重新赋值
	    	sortList.clear(); //释放资源
		}
		
		
	}
	
	/**
	 * 
	* @Function: TransLogServiceImpl.java
	* @Description: 创建交易流水
	*
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
	private List createTransLog(IntfaceTransLog intfaceTransLog){
		
		List<TransLog> voList=new ArrayList<TransLog>();
		
		/**
			MB80("B80", "商户开户"), 
			MB81("B81", "企业开户"),
			MB82("B82", "供应商开户"),
			MB83("B83", "分销商开户"),
			
			
			MB40("B40", "商户账户转账"),
			MB50("B50", "企业员工充值"),
			MB90("B90", "商户提现"), 
			
			CW80("W80", "企业员工开户"),
			CW81("W81", "密码重置"),
			CW10("W10", "消费"),
			CW71("W71", "快捷消费"),
			CW11("W11", "退款"),
			CW74("W74", "退款（快捷）"),
			
			CW90("W90", "权益转让"),
			CW91("W91", "现金账户转移");
		 */
		
		TransLog transLog=new TransLog();
		transLog.setItfPrimaryKey(intfaceTransLog.getItfPrimaryKey()); //接口层流水
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

		if (TransCode.MB50.getCode().equals(intfaceTransLog.getTransId())){
			transLog.setCardAttr(AccountCardAttrEnum.ADD.getValue());
			
		}else if (TransCode.CW71.getCode().equals(intfaceTransLog.getTransId())){
			transLog.setCardAttr(AccountCardAttrEnum.SUB.getValue());
			transLog.setOrder(1);
			
			//快捷消费 先充值，再消费
			TransLog transLog2=transLog;
			transLog2.setCardAttr(AccountCardAttrEnum.ADD.getValue());
			addToVoList(voList,transLog2,0);
		}
		
		addToVoList(voList,transLog,1);
		return voList;
	}
	
	
	private void addToVoList(List<TransLog> voList,TransLog transLog,int order){
		transLog.setOrder(order);
		createTransLog(transLog);//基本信息
		voList.add(transLog);
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
