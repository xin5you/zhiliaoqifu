package com.ebeijia.zl.facade.account.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ebeijia.zl.common.utils.exceptions.BizException;



/**
 * 账户服务业务异常类,异常代码8位数字组成,前4位固定2008打头,后4位自定义
 * 
 * @author zhuqi
 * 
 */
public class AccountBizException extends BizException {

	private static final long serialVersionUID = 1L;

	private  final Logger log = LoggerFactory.getLogger(AccountBizException.class);

	public static final AccountBizException ACCOUNT_NOT_EXIT = new AccountBizException(20080001, "账户不存在");


	public static final AccountBizException ACCOUNT_AVAILABLEBALANCE_IS_NOT_ENOUGH = new AccountBizException(20080100, "可用余额不足");

	public static final AccountBizException ACCOUNT_STATUS_IS_INACTIVE = new AccountBizException(20080101, "账户状态不可用");

	public static final AccountBizException ACCOUNT_TRADEPWD_ERROR = new AccountBizException(20080104, "交易密码错误");

	public static final AccountBizException ACCOUNT_CANNOT_TRANSFER_TO_MYSELF = new AccountBizException(20080105, "不能给自己转账");

	public static final AccountBizException ACCOUNT_CANNOT_TRADE_TO_SELF = new AccountBizException(20080106, "不能给自己交易");

	public static final AccountBizException ACCOUNT_AMOUNT_ERROR = new AccountBizException(20080107, "金额错误");

	public static final AccountBizException ACCOUNT_CREATE_FAILED = new AccountBizException(20080108, "创建账户失败");

	public static final AccountBizException ACCOUNT_TRANS_FAILED = new AccountBizException(20080109, "账户交易失败");

	public static final AccountBizException ACCOUNT_AMOUNT_LESS_THAN_SETTLE_AMOUNT = new AccountBizException(20080111,
			"结算账户余额 小于不可用余额，不能结算");

	public static final AccountBizException ACCOUNT_POS_TRXTYPE_INVALID = new AccountBizException(20080112, "POS交易类型无效");
	
	
	public static final AccountBizException ACCOUNT_CARD_ATTR_ERROR = new AccountBizException(20089999, "当前交易类型未知");


	public AccountBizException() {
	}

	public AccountBizException(int code, String msgFormat, Object... args) {
		super(code, msgFormat, args);
	}

	public AccountBizException(int code, String msg) {
		super(code, msg);
	}

	/**
	 * 实例化异常
	 * 
	 * @param msgFormat
	 * @param args
	 * @return
	 */
	public AccountBizException newInstance(String msgFormat, Object... args) {
		return new AccountBizException(this.code, msgFormat, args);
	}

	public AccountBizException print() {
		log.info("==>BizException, code:" + this.code + ", msg:" + this.msg);
		return new AccountBizException(this.code, this.msg);
	}
}
