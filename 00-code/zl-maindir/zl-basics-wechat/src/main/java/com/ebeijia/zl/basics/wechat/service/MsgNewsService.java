package com.ebeijia.zl.basics.wechat.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.wechat.domain.MsgNews;


/**
 *
 * 消息表 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
public interface MsgNewsService extends IService<MsgNews> {
	
	
	/**
	 * 关注欢迎语
	 * @return
	 */
	public List<MsgNews>  getMsgNewsByCode(String inputCode);

	public List<MsgNews> listForPage(MsgNews searchEntity);
	
	
	public List<MsgNews> getRandomMsgByContent(String inputcode ,Integer num);

}
