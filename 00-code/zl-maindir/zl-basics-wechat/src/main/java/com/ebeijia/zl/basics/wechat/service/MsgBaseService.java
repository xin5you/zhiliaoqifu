package com.ebeijia.zl.basics.wechat.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.wechat.domain.MsgBase;
import com.ebeijia.zl.basics.wechat.domain.MsgNews;
import com.ebeijia.zl.basics.wechat.domain.MsgText;


/**
 *
 * 消息基础表 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
public interface MsgBaseService extends IService<MsgBase> {

	public List<MsgBase> listForPage(MsgBase searchEntity);
	
	public List<MsgNews> listMsgNewsByBaseId(String[] ids);
	
	public MsgText getMsgTextByBaseId(String id);
}
