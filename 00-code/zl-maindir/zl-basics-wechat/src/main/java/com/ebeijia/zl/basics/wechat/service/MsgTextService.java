package com.ebeijia.zl.basics.wechat.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.wechat.domain.MsgText;


/**
 *
 * 文本消息表 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
public interface MsgTextService extends IService<MsgText> {
	

	public List<MsgText> listForPage(MsgText searchEntity);

	// 根据用户发送的文本消息，随机获取一条文本消息
	public MsgText getRandomMsg(String inputcode);

	public MsgText getRandomMsg2();
	
	public MsgText getMsgTextByInputCode(String inputcode);

}
