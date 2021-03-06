package com.ebeijia.zl.service.account.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.core.domain.BillingType;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.tools.AmountUtil;
import com.ebeijia.zl.common.utils.tools.SnowFlake;
import com.ebeijia.zl.core.redis.constants.RedisDictKey;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawOrder;
import com.ebeijia.zl.facade.account.enums.WithDrawReceiverTypeEnum;
import com.ebeijia.zl.facade.account.enums.WithDrawStatusEnum;
import com.ebeijia.zl.facade.account.enums.WithDrawSuccessEnum;
import com.ebeijia.zl.facade.account.req.AccountQuickPayVo;
import com.ebeijia.zl.facade.account.req.AccountRefundVo;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import com.ebeijia.zl.facade.user.vo.PersonInf;
import com.ebeijia.zl.facade.user.vo.UserInf;
import com.ebeijia.zl.service.account.service.*;
import com.ebeijia.zl.service.user.service.IUserInfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.account.dto.IntfaceTransLog;
import com.ebeijia.zl.facade.account.dto.TransLog;
import com.ebeijia.zl.facade.account.exceptions.AccountBizException;
import com.ebeijia.zl.facade.account.req.AccountTxnVo;
import com.ebeijia.zl.service.account.mapper.TransLogMapper;
import redis.clients.jedis.JedisCluster;

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

	@Autowired
	private IAccountWithdrawOrderService accountWithdrawOrderService;

	@Autowired
	private IAccountWithdrawDetailService accountWithdrawDetailService;

	@Autowired
	private JedisCluster jedisCluster;

	@Autowired
	private TransLogMapper transLogMapper;

	@Autowired
	private ProviderInfFacade providerInfFacade;

	@Autowired
	private IUserInfService userInfService;

	@Autowired
	private IIntfaceTransLogService intfaceTransLogService;
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
	 *
	 * @Function: ITransLogService.java
	 * @Description: 創建賬戶交易流水
	 *
	 * @param:intfaceTransLogs 接口層流水集合
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
	public boolean executeList(List<IntfaceTransLog> intfaceTransLogs) throws AccountBizException{
		log.info("==>execute create intfaceTransLogs begin...");


		List<TransLog> voList=new ArrayList<TransLog>();
		for(IntfaceTransLog intfaceTransLog:intfaceTransLogs){
			voList.addAll(doTransLog(intfaceTransLog));
		}
		boolean b=this.execute(voList);
		voList.clear();
		log.info("==>execute create transLog end...<==");
		return true;
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
		 MB90("B90", "商户提款"),
		 MB95("B95", "商户收款"),

		 CW80("W80", "企业员工开户"),
		 CW81("W81", "密码重置"),
		 CW10("W10", "商品消费"),
		 CW50("W50", "员工充值"),
		 CW71("W71", "微信支付"),
		 CW20("W20", "购买代金券"),
		 CW11("W11", "退款"),
		 CW74("W74", "微信退款"),
		 CW40("W40", "员工转账"),
		 CW90("W90", "权益转让"),
		 CW91("W91", "用户提款"),
		 CW92("W92", "解冻扣款"),
		 CW93("W93", "解冻撤销")，
		 CW99("W99", "快捷充值);
		 */

		if (TransCode.MB20.getCode().equals(intfaceTransLog.getTransId())){
			List<AccountTxnVo> transList=intfaceTransLog.getTransList();
			if(transList !=null && transList.size()>0){
				//商户 多账户类型充值
				for (AccountTxnVo accountTxnVo : transList) {
					this.addToVoList(IdUtil.getNextId(),null,voList, intfaceTransLog, null, accountTxnVo.getTxnBId(), AccountCardAttrEnum.ADD.getValue(), accountTxnVo.getTxnAmt(),accountTxnVo.getUpLoadAmt(),null);
				}
			}else {
				//商户充值
				this.addToVoList(IdUtil.getNextId(),null,voList, intfaceTransLog, null, null, AccountCardAttrEnum.ADD.getValue(), 0);
			}

		}else if (TransCode.MB40.getCode().equals(intfaceTransLog.getTransId())) {
			List<AccountTxnVo> transList = intfaceTransLog.getTransList();
			if (transList != null && transList.size() > 0) {
				//商户 多账户类型之间转账
				for (AccountTxnVo accountTxnVo : transList) {
					String txnPrimaryKey=IdUtil.getNextId();
					//商户转账
					this.addToVoList(txnPrimaryKey,null,voList, intfaceTransLog, intfaceTransLog.getTfrOutUserId(), accountTxnVo.getTxnBId(), AccountCardAttrEnum.SUB.getValue(), accountTxnVo.getTxnAmt(),accountTxnVo.getUpLoadAmt(),null);
					this.addToVoList(IdUtil.getNextId(),txnPrimaryKey,voList, intfaceTransLog, intfaceTransLog.getTfrInUserId(), accountTxnVo.getTxnBId(), AccountCardAttrEnum.ADD.getValue(), accountTxnVo.getTxnAmt(),accountTxnVo.getUpLoadAmt(),null);
				}
			} else {
				//商户转账
				String txnPrimaryKey=IdUtil.getNextId();
				this.addToVoList(txnPrimaryKey,null,voList, intfaceTransLog, intfaceTransLog.getTfrOutUserId(), intfaceTransLog.getTfrOutBId(), AccountCardAttrEnum.SUB.getValue(), 0);
				this.addToVoList(IdUtil.getNextId(),txnPrimaryKey,voList, intfaceTransLog, intfaceTransLog.getTfrInUserId(), intfaceTransLog.getTfrInBId(), AccountCardAttrEnum.ADD.getValue(), 1);
			}
		}
		else if (TransCode.CW50.getCode().equals(intfaceTransLog.getTransId())){
			this.addToVoList(IdUtil.getNextId(),null,voList, intfaceTransLog,null,null, AccountCardAttrEnum.ADD.getValue(), 0);

		}else if (TransCode.MB50.getCode().equals(intfaceTransLog.getTransId())){
			//企业员工充值 从企业的通卡账户，转入到员工的专项账户里面
			String txnPrimaryKey=IdUtil.getNextId();
			this.addToVoList(txnPrimaryKey,null,voList, intfaceTransLog, intfaceTransLog.getTfrOutUserId(), intfaceTransLog.getTfrOutBId(), AccountCardAttrEnum.SUB.getValue(), 0);

			TransLog transLog2=new TransLog();
			this.newTransLog(intfaceTransLog, transLog2);
			transLog2.setTxnPrimaryKey(IdUtil.getNextId());
			transLog2.setSourceTxnPrimaryKey(txnPrimaryKey);
			transLog2.setUserId(intfaceTransLog.getTfrInUserId());
			transLog2.setPriBId(intfaceTransLog.getTfrInBId());
			transLog2.setCardAttr(AccountCardAttrEnum.ADD.getValue());
			transLog2.setTransId(TransCode.CW50.getCode());
			transLog2.setUserType(UserType.TYPE100.getCode());
			addToVoList(voList,transLog2,1);
		}else if( TransCode.CW71.getCode().equals(intfaceTransLog.getTransId())
				|| TransCode.CW10.getCode().equals(intfaceTransLog.getTransId())
				|| TransCode.CW20.getCode().equals(intfaceTransLog.getTransId())
				|| TransCode.MB10.getCode().equals(intfaceTransLog.getTransId())
				|| TransCode.MB71.getCode().equals(intfaceTransLog.getTransId())){

			if (TransCode.CW71.getCode().equals(intfaceTransLog.getTransId()) || TransCode.MB71.getCode().equals(intfaceTransLog.getTransId())){
				//快捷消费，先充值
				List<AccountQuickPayVo> addList = intfaceTransLog.getAddList();
				if (addList != null && addList.size() > 0) {
                    String txnPrimaryKey=null;
					for (AccountQuickPayVo accountQuickPayVo : addList) {
                        if(accountQuickPayVo.getTfrOutAmt() !=null  && AmountUtil.bigger(accountQuickPayVo.getTfrOutAmt(),new BigDecimal(0))) {
                            txnPrimaryKey=IdUtil.getNextId();
                            this.addToVoList(txnPrimaryKey,null,voList, intfaceTransLog, null, accountQuickPayVo.getTfrOutBId(), AccountCardAttrEnum.SUB.getValue(), accountQuickPayVo.getTfrOutAmt(), accountQuickPayVo.getTfrOutAmt(),TransCode.CW71.getCode());
                        }
						this.addToVoList(IdUtil.getNextId(),txnPrimaryKey,voList, intfaceTransLog, null, accountQuickPayVo.getTfrInBId(), AccountCardAttrEnum.ADD.getValue(), accountQuickPayVo.getTfrInAmt(),accountQuickPayVo.getTfrInAmt(),TransCode.CW50.getCode());
				    }
				}

	}

			//消费扣款
			List<AccountTxnVo> transList = intfaceTransLog.getTransList();
			TransLog transLog2=null;
			if (transList != null && transList.size() > 0) {

				ProviderInf providerInf=null;
				UserInf tarMchntUserInf=null;
				if(StringUtil.isNotEmpty(intfaceTransLog.getTargetMchntCode())) {
					try {
						providerInf = providerInfFacade.getProviderInfById(intfaceTransLog.getTargetMchntCode());
						if(providerInf!=null){
							tarMchntUserInf=userInfService.getUserInfByUserName(providerInf.getProviderId());
						}
					} catch (Exception e) {
						throw AccountBizException.ACCOUNT_TARGET_MCHNT_ERROR.newInstance("查找供应商异常,供应商Id{%s}", intfaceTransLog.getTargetMchntCode()).print();
					}
				}

				for (AccountTxnVo accountTxnVo : transList) {
					String transId=null;
					String txnPrimaryKey=IdUtil.getNextId();
					if (TransCode.CW71.getCode().equals(intfaceTransLog.getTransId())) {
						transId=TransCode.CW10.getCode();
					}
					this.addToVoList(txnPrimaryKey,null,voList, intfaceTransLog, null, accountTxnVo.getTxnBId(), AccountCardAttrEnum.SUB.getValue(), accountTxnVo.getTxnAmt(),accountTxnVo.getUpLoadAmt(),transId);

					//商户收款
					transLog2=new TransLog();
					this.newTransLog(intfaceTransLog, transLog2);
					transLog2.setTxnPrimaryKey(IdUtil.getNextId());
                    transLog2.setSourceTxnPrimaryKey(txnPrimaryKey);
					transLog2.setUserId(intfaceTransLog.getTfrInUserId());
					transLog2.setPriBId(accountTxnVo.getTxnBId());
					transLog2.setCardAttr(AccountCardAttrEnum.ADD.getValue());
					transLog2.setTransId(TransCode.MB95.getCode());
					transLog2.setUserType(UserType.TYPE500.getCode());
					transLog2.setTransAmt(accountTxnVo.getTxnAmt());
					transLog2.setUploadAmt(accountTxnVo.getUpLoadAmt());
					addToVoList(voList,transLog2,voList.size());

					//目标商户实时冲账
					if(providerInf !=null){
						BigDecimal providerRate=providerInf.getProviderRate() !=null ? providerInf.getProviderRate():new BigDecimal(1);
						BigDecimal providerTxnAmt=AmountUtil.mul(accountTxnVo.getUpLoadAmt(),providerRate); //结算金额 等于上送的金额 * 折扣率

						transLog2=new TransLog();
                        txnPrimaryKey=IdUtil.getNextId();
						this.newTransLog(intfaceTransLog, transLog2);
						transLog2.setTxnPrimaryKey(txnPrimaryKey);
						transLog2.setUserId(intfaceTransLog.getTfrInUserId());
						transLog2.setPriBId(accountTxnVo.getTxnBId());
						transLog2.setCardAttr(AccountCardAttrEnum.SUB.getValue());
						transLog2.setTransId(TransCode.MB10.getCode());
						transLog2.setUserType(UserType.TYPE500.getCode());
						transLog2.setTransAmt(providerTxnAmt);
						transLog2.setUploadAmt(providerTxnAmt);
						addToVoList(voList,transLog2,voList.size());

						transLog2=new TransLog();
						this.newTransLog(intfaceTransLog, transLog2);
						transLog2.setTxnPrimaryKey(IdUtil.getNextId());
						transLog2.setSourceTxnPrimaryKey(txnPrimaryKey); //加的数据来源于减少的数据
						transLog2.setUserId(tarMchntUserInf.getUserId()); //目标商户
						transLog2.setPriBId(accountTxnVo.getTxnBId());
						transLog2.setCardAttr(AccountCardAttrEnum.ADD.getValue());
						transLog2.setTransId(TransCode.MB95.getCode());
						transLog2.setUserType(UserType.TYPE300.getCode());
						transLog2.setTransAmt(providerTxnAmt);
						transLog2.setUploadAmt(providerTxnAmt);
						addToVoList(voList,transLog2,voList.size());
					}
				}
			}

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
		}else if (TransCode.CW90.getCode().equals(intfaceTransLog.getTransId())){
			//卡券转卖 充值到托管账户
			BigDecimal loseFee=new BigDecimal(0.04); //默认折损率
			List<AccountTxnVo> addList = intfaceTransLog.getTransList();
			TransLog transLog2=null;

			String zlChannelCode=jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,RedisDictKey.zlqf_mchnt_code);
			UserInf mchntUserInf=userInfService.getUserInfByUserName(zlChannelCode);
			if (addList != null && addList.size() > 0) {
				for (AccountTxnVo accountTxnVo : addList) {
					BillingType billingType = getBillingTypeForCache(accountTxnVo.getTxnBId());
					if (billingType != null) {
						loseFee = billingType.getLoseFee(); //指的是账户转卖代金券的折损率，
					}
					BigDecimal transAmt = AmountUtil.mul(accountTxnVo.getTxnAmt(), AmountUtil.sub(new BigDecimal(1), loseFee)); //扣除折损率后，到账金额
					String txnPrimaryKey=IdUtil.getNextId();
					transLog2=new TransLog();
					this.newTransLog(intfaceTransLog, transLog2);
					transLog2.setTxnPrimaryKey(txnPrimaryKey);
					transLog2.setUserId(mchntUserInf.getUserId()); //平台用户
					transLog2.setPriBId(SpecAccountTypeEnum.A01.getbId());
					transLog2.setCardAttr(AccountCardAttrEnum.SUB.getValue());
					transLog2.setTransId(TransCode.MB40.getCode());
					transLog2.setUserType(UserType.TYPE400.getCode());
					transLog2.setPriBId(SpecAccountTypeEnum.A01.getbId());
					transLog2.setTransAmt(transAmt);
					transLog2.setUploadAmt(accountTxnVo.getUpLoadAmt());
					addToVoList(voList,transLog2,voList.size());

					this.addToVoList(IdUtil.getNextId(),txnPrimaryKey,voList, intfaceTransLog,null,SpecAccountTypeEnum.A01.getbId(), AccountCardAttrEnum.ADD.getValue(), transAmt,accountTxnVo.getUpLoadAmt(),null);

				}
			}
		}else if (TransCode.MB90.getCode().equals(intfaceTransLog.getTransId()) || TransCode.CW91.getCode().equals(intfaceTransLog.getTransId())){

			String batchNO=String.valueOf(SnowFlake.getInstance().nextId());
			//用户或者商户withdraw操作
			this.withDarwAddToVoList(voList, intfaceTransLog,intfaceTransLog.getUserId(),SpecAccountTypeEnum.A01.getbId(), AccountCardAttrEnum.FROZEN.getValue(), batchNO);


			AccountWithdrawDetail withdrawDetail=intfaceTransLog.getWithdrawDetail();
			AccountWithdrawOrder accountWithdrawOrder=new AccountWithdrawOrder();
			accountWithdrawOrder.setBatchNo(batchNO);
			accountWithdrawOrder.setTotalAmount(withdrawDetail.getTransAmount());
			accountWithdrawOrder.setTxnPrimaryKey(voList.get(0).getTxnPrimaryKey());
			accountWithdrawOrder.setTotalNum(1);
			accountWithdrawOrder.setStatus(WithDrawStatusEnum.Status00.getCode()); //新建状态

			withdrawDetail.setBatchNo(accountWithdrawOrder.getBatchNo());
			withdrawDetail.setReceiverCurrency("CNY"); //人民币标识
			withdrawDetail.setSuccess(WithDrawSuccessEnum.Processing.getCode()); //提现中

			if(TransCode.CW91.getCode().equals(intfaceTransLog.getTransId())){
				withdrawDetail.setReceiverType(WithDrawReceiverTypeEnum.PERSON.toString()); //用户提现
			}else{
				withdrawDetail.setReceiverType(WithDrawReceiverTypeEnum.CORP.toString());//企业提现
			}
			boolean eflag=accountWithdrawOrderService.save(accountWithdrawOrder);
			if(eflag) {
				eflag=accountWithdrawDetailService.save(withdrawDetail);
			}
			if(!eflag) {
				throw AccountBizException.ACCOUNT_WITHDRID_SAVE_FAILED.newInstance("提现操作异常,用户Id{%s},当前交易请求订单号{%s}",withdrawDetail.getUserId(),intfaceTransLog.getDmsRelatedKey()).print();
			}
		}else if (TransCode.CW92.getCode().equals(intfaceTransLog.getTransId()) || TransCode.CW93.getCode().equals(intfaceTransLog.getTransId())){

			List<TransLog>  transLogs =getTransLogListByItfPrikey(intfaceTransLog.getOrgItfPrimaryKey(),intfaceTransLog.getUserId(),AccountCardAttrEnum.FROZEN.getValue());
			TransLog transLog=null;
			if (transLogs !=null && transLogs.size()>0) {
				for (TransLog orgTransLog: transLogs) {
					transLog=new TransLog();
					this.newTransLog(intfaceTransLog, transLog);
					transLog.setTxnPrimaryKey(IdUtil.getNextId());
					transLog.setUserId(intfaceTransLog.getUserId());
					transLog.setPriBId(intfaceTransLog.getPriBId());
					transLog.setOrgTxnPrimaryKey(orgTransLog.getTxnPrimaryKey());
					if(TransCode.CW92.getCode().equals(intfaceTransLog.getTransId())){
						transLog.setCardAttr(AccountCardAttrEnum.COMMINFROZEN.getValue());
					}else if(TransCode.CW93.getCode().equals(intfaceTransLog.getTransId())){
						transLog.setCardAttr(AccountCardAttrEnum.UNFROZEN.getValue());
					}
					addToVoList(voList,transLog,voList.size());
			}
		}else {
			throw AccountBizException.ACCOUNT_REFUND_FAILED.newInstance("冻结退款操作异常,原交易流水Id{%s}的专项交易不存在",intfaceTransLog.getOrgItfPrimaryKey()).print();
		}
		}else if (TransCode.CW99.getCode().equals(intfaceTransLog.getTransId())){
			//快捷充值
			this.addToVoList(IdUtil.getNextId(),null,voList, intfaceTransLog,null,SpecAccountTypeEnum.A01.getbId(), AccountCardAttrEnum.ADD.getValue(), 0);

		}else if (TransCode.CW11.getCode().equals(intfaceTransLog.getTransId())
				|| TransCode.CW74.getCode().equals(intfaceTransLog.getTransId())
				|| TransCode.MB74.getCode().equals(intfaceTransLog.getTransId())){

				List<AccountRefundVo> refundList = intfaceTransLog.getRefundList();

				//退回到支付用户账户
				List<TransLog>  transLogs=getTransLogListByItfPrikey(intfaceTransLog.getOrgItfPrimaryKey(),intfaceTransLog.getTfrInUserId(),AccountCardAttrEnum.SUB.getValue());
				if (transLogs !=null && transLogs.size()>0) {
					for (AccountRefundVo accountTxnVo : refundList) {
						boolean 	priBidVal=false;
						for (TransLog orgTransLog: transLogs) {
							if (accountTxnVo.getTxnBId().equals(orgTransLog.getPriBId())) {
								priBidVal=true;
								this.addToVoListOrgTransLog(voList, intfaceTransLog, orgTransLog,AccountCardAttrEnum.ADD.getValue(), accountTxnVo.getTxnAmt(), accountTxnVo.getTxnAmt());
							}
						}
						if(!priBidVal){
							throw AccountBizException.ACCOUNT_REFUND_FAILED.newInstance("消费1退款操作异常,原交易流水Id{%s}的专项交易类型{%s}不存在",intfaceTransLog.getOrgItfPrimaryKey(),accountTxnVo.getTxnBId()).print();
						}
					}
				}else {
					throw AccountBizException.ACCOUNT_REFUND_FAILED.newInstance("消费1退款操作异常,原交易流水Id{%s}的专项交易不存在",intfaceTransLog.getOrgItfPrimaryKey()).print();
				}

				//A类账户消费，商户退款只能是从消费的专项类型退款 即购买 B06的商品，使用A01消费，商户退款也只能是从B06类型退
				for(AccountRefundVo accountTxnVo : refundList){
					if ("A".equals(SpecAccountTypeEnum.findByBId(accountTxnVo.getTxnBId()).getType())){
						if(StringUtil.isNotEmpty(accountTxnVo.getPriConsumeBId())) {
							accountTxnVo.setTxnBId(accountTxnVo.getPriConsumeBId());
						}
					}
				}

				//从收款商户退款
				transLogs=getTransLogListByItfPrikey(intfaceTransLog.getOrgItfPrimaryKey(),intfaceTransLog.getTfrOutUserId(),AccountCardAttrEnum.ADD.getValue());
				if (transLogs !=null && transLogs.size()>0) {
					for (AccountRefundVo accountTxnVo : refundList) {
						boolean priBidVal=false;
						for (TransLog orgTransLog: transLogs) {
							if (accountTxnVo.getTxnBId().equals(orgTransLog.getPriBId())) {
								priBidVal=true;
								this.addToVoListOrgTransLog(voList, intfaceTransLog, orgTransLog,AccountCardAttrEnum.SUB.getValue(), accountTxnVo.getTxnAmt(), accountTxnVo.getTxnAmt());
							}
						}
						if(!priBidVal){
							throw AccountBizException.ACCOUNT_REFUND_FAILED.newInstance("商户退款操作异常,原交易流水Id{%s}的专项交易类型{%s}不存在",intfaceTransLog.getOrgItfPrimaryKey(),accountTxnVo.getTxnBId()).print();
						}
					}
				}else {
					throw AccountBizException.ACCOUNT_REFUND_FAILED.newInstance("商户退款操作异常,原交易流水Id{%s}的专项交易不存在",intfaceTransLog.getOrgItfPrimaryKey()).print();
				}

				//step3:
			ProviderInf providerInf=null;
			UserInf tarMchntUserInf=null;
			IntfaceTransLog orgIntfaceTransLog=intfaceTransLogService.getById(intfaceTransLog.getOrgItfPrimaryKey());
			if(StringUtil.isNotEmpty(orgIntfaceTransLog.getTargetMchntCode())){
				try {
					providerInf = providerInfFacade.getProviderInfById(orgIntfaceTransLog.getTargetMchntCode());
					if(providerInf!=null){
						tarMchntUserInf=userInfService.getUserInfByUserName(providerInf.getProviderId());
					}
				} catch (Exception e) {
					throw AccountBizException.ACCOUNT_TARGET_MCHNT_ERROR.newInstance("查找供应商异常,供应商Id{%s}", orgIntfaceTransLog.getTargetMchntCode()).print();
				}

				//如果有目标商户（供应商）即时冲账
				transLogs=getTransLogListByItfPrikey(intfaceTransLog.getOrgItfPrimaryKey(),intfaceTransLog.getTfrOutUserId(),AccountCardAttrEnum.SUB.getValue());

				if (transLogs !=null && transLogs.size()>0) {
					for (AccountRefundVo accountTxnVo : refundList) {
						boolean priBidVal=false;
						BigDecimal providerRate=providerInf.getProviderRate() !=null ? providerInf.getProviderRate():new BigDecimal(1);
						BigDecimal providerTxnAmt=AmountUtil.mul(accountTxnVo.getUpLoadAmt(),providerRate); //结算金额 等于上送的金额 * 折扣率
						for (TransLog orgTransLog: transLogs) {
							if (accountTxnVo.getTxnBId().equals(orgTransLog.getPriBId())) {
								priBidVal=true;
								this.addToVoListOrgTransLog(voList, intfaceTransLog, orgTransLog,AccountCardAttrEnum.ADD.getValue(), providerTxnAmt, providerTxnAmt);
							}
						}
						if(!priBidVal){
							throw AccountBizException.ACCOUNT_REFUND_FAILED.newInstance("收款商户退款操作异常,原交易流水Id{%s}的专项交易类型{%s}不存在",intfaceTransLog.getOrgItfPrimaryKey(),accountTxnVo.getTxnBId()).print();
						}
					}
				}else {
					throw AccountBizException.ACCOUNT_REFUND_FAILED.newInstance("收款商户退款操作异常,原交易流水Id{%s}的专项交易不存在",intfaceTransLog.getOrgItfPrimaryKey()).print();
				}

				transLogs=getTransLogListByItfPrikey(intfaceTransLog.getOrgItfPrimaryKey(),tarMchntUserInf.getUserId(),AccountCardAttrEnum.ADD.getValue());
				if (transLogs !=null && transLogs.size()>0) {
					for (AccountRefundVo accountTxnVo : refundList) {
						boolean priBidVal=false;
						BigDecimal providerRate=providerInf.getProviderRate() !=null ? providerInf.getProviderRate():new BigDecimal(1);
						BigDecimal providerTxnAmt=AmountUtil.mul(accountTxnVo.getUpLoadAmt(),providerRate); //结算金额 等于上送的金额 * 折扣率
						for (TransLog orgTransLog: transLogs) {
							if (accountTxnVo.getTxnBId().equals(orgTransLog.getPriBId())) {
								priBidVal=true;
								this.addToVoListOrgTransLog(voList, intfaceTransLog, orgTransLog,AccountCardAttrEnum.SUB.getValue(), providerTxnAmt, providerTxnAmt);
							}
						}
						if(!priBidVal){
							throw AccountBizException.ACCOUNT_REFUND_FAILED.newInstance("供应商商户退款操作异常,原交易流水Id{%s}的专项交易类型{%s}不存在",intfaceTransLog.getOrgItfPrimaryKey(),accountTxnVo.getTxnBId()).print();
						}
					}
				}else {
					throw AccountBizException.ACCOUNT_REFUND_FAILED.newInstance("供应商商户退款操作异常,原交易流水Id{%s}的专项交易不存在",intfaceTransLog.getOrgItfPrimaryKey()).print();
				}
			}
		}
		return voList;
	}

	/**
	 *
	 * @param voList  交易日志列表
	 * @param intfaceTransLog  交易流水记录表
	 * @param userId  用户ID
	 * @param bId  专项类型
	 * @param cardAttr  操作类型
	 * @param order 操作顺序
	 */
	private void addToVoList(String txnPrimaryKey,String sourceTxnPrimaryKey,List<TransLog> voList,IntfaceTransLog intfaceTransLog,String userId,String bId,String cardAttr ,int order){
		TransLog transLog=new TransLog();
		transLog.setTxnPrimaryKey(txnPrimaryKey);
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
		if(StringUtil.isNotEmpty(sourceTxnPrimaryKey)){
			transLog.setSourceTxnPrimaryKey(sourceTxnPrimaryKey);
		}
		addToVoList(voList,transLog,order);
	}

	/**
     * @param txnPrimaryKey
     * @param sourceTxnPrimaryKey
	 * @param voList  交易日志列表
	 * @param intfaceTransLog  交易流水记录表
	 * @param userId  用户ID
	 * @param bId  专项类型
	 * @param cardAttr  操作类型
	 * @param transAmt  上送金额
	 * @param upLoadAmt  交易金额
	 * @param transId 交易Id
	 */
	private void addToVoList(String txnPrimaryKey,String sourceTxnPrimaryKey,List<TransLog> voList,IntfaceTransLog intfaceTransLog,String userId,String bId,String cardAttr ,BigDecimal transAmt,BigDecimal upLoadAmt,String transId){
		TransLog transLog=new TransLog();
		transLog.setTxnPrimaryKey(txnPrimaryKey);
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
		if(transAmt !=null){
			transLog.setTransAmt(transAmt);
		}
		if(upLoadAmt !=null){
			transLog.setUploadAmt(upLoadAmt);
		}

		if(StringUtil.isNotEmpty(transId)){
			transLog.setTransId(transId);
		}
        if(StringUtil.isNotEmpty(sourceTxnPrimaryKey)){
            transLog.setSourceTxnPrimaryKey(sourceTxnPrimaryKey);
        }
		addToVoList(voList,transLog,voList.size());
	}

	/**
	 *
	 * @param voList  交易日志列表
	 * @param intfaceTransLog  交易流水记录表
	 * @param orgTransLog  原交易日志
	 * @param attr      交易类型
	 * @param transAmt  交易金额
	 * @param upLoadAmt 上送金额
	 */
	private void addToVoListOrgTransLog(List<TransLog> voList,IntfaceTransLog intfaceTransLog,TransLog orgTransLog,String attr,BigDecimal transAmt,BigDecimal upLoadAmt){
		TransLog transLog=new TransLog();
		transLog.setTxnPrimaryKey(IdUtil.getNextId());
		this.newTransLog(intfaceTransLog, transLog);
		transLog.setCardAttr(attr);
		transLog.setUserId(orgTransLog.getUserId());
		transLog.setOrgTxnPrimaryKey(orgTransLog.getTxnPrimaryKey());
		transLog.setPriBId(orgTransLog.getPriBId());
		transLog.setTransAmt(transAmt);
		transLog.setUploadAmt(upLoadAmt);
		transLog.setUserType(orgTransLog.getUserType());
		addToVoList(voList,transLog,voList.size());
	}

	/**
	 *  提现操作
	 * @param voList  交易日志列表
	 * @param intfaceTransLog  交易流水记录表
	 * @param userId  用户ID
	 * @param bId  专项类型
	 * @param cardAttr  操作类型
	 * @param batchNo
	 */
	private void withDarwAddToVoList(List<TransLog> voList,IntfaceTransLog intfaceTransLog,String userId,String bId,String cardAttr ,String batchNo){
		//用户提现 账户扣款
		TransLog transLog=new TransLog();
		transLog.setTxnPrimaryKey(IdUtil.getNextId());
		transLog.setBatchNo(batchNo);
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
		addToVoList(voList,transLog,voList.size());
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

	private BillingType getBillingTypeForCache(String bId){
		String billingTypeSting=jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BILLING_TYPE,bId);
		BillingType billingType=JSONObject.parseObject(billingTypeSting,BillingType.class);
		return  billingType;
	}
	/**
	 *  查找原交易的交易日志
	 * @param itfPrikey
	 * @param userId
	 * @param cardAttr 交易类型
	 * @return
	 * @throws AccountBizException
	 */
	public List<TransLog> getTransLogListByItfPrikey(String itfPrikey,String userId,String cardAttr)throws AccountBizException{
		QueryWrapper<TransLog> queryWrapper = new QueryWrapper<TransLog>();
		queryWrapper.eq("itf_primary_key", itfPrikey);
		queryWrapper.eq("user_id", userId);
		if(StringUtil.isNotEmpty(cardAttr)){
			queryWrapper.eq("card_attr", cardAttr);
		}
		return transLogMapper.selectList(queryWrapper);
	}
}
