package com.ebeijia.zl.basics.wechat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.wechat.domain.MsgNews;
import com.ebeijia.zl.basics.wechat.mapper.MsgNewsMapper;
import com.ebeijia.zl.basics.wechat.service.MsgNewsService;

/**
 *
 * 消息表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Service
public class MsgNewsServiceImpl extends ServiceImpl<MsgNewsMapper, MsgNews> implements MsgNewsService{
	
	@Autowired
	private MsgNewsMapper msgNewsMapper;

	@Override
	public List<MsgNews>  getMsgNewsByCode(String inputCode) {
		return msgNewsMapper.getMsgNewsByCode(inputCode);
	}

	@Override
	public List<MsgNews> listForPage(MsgNews searchEntity) {
		return msgNewsMapper.listForPage(searchEntity);
	}

	public List<MsgNews> getRandomMsgByContent(String inputcode ,Integer num){
		return msgNewsMapper.getRandomMsgByContent(inputcode, num);
	}



}
