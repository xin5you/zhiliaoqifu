package com.ebeijia.zl.core.rocketmq.exceptions;


import com.ebeijia.zl.common.utils.exceptions.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * 
 * @author zhuqi
 * 
 */
public class RocketMQException extends BizException {


	private  final Logger log = LoggerFactory.getLogger(RocketMQException.class);

	public RocketMQException() {

	}

	public RocketMQException(int code, String msgFormat, Object... args) {
		super(code, msgFormat, args);
	}

	public RocketMQException(int code, String msg) {
		super(code, msg);
	}

	/**
	 * 实例化异常
	 * 
	 * @param msgFormat
	 * @param args
	 * @return
	 */
	public RocketMQException newInstance(String msgFormat, Object... args) {
		return new RocketMQException(this.code, msgFormat, args);
	}

	public RocketMQException print() {
		log.info("==>BizException, code:" + this.code + ", msg:" + this.msg);
		return new RocketMQException(this.code, this.msg);
	}
}
