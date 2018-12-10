package com.ebeijia.zl.basics.wechat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.wechat.domain.MsgText;
import com.ebeijia.zl.basics.wechat.mapper.MsgTextMapper;
import com.ebeijia.zl.basics.wechat.service.MsgTextService;

/**
 *
 * 文本消息表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Service
public class MsgTextServiceImpl extends ServiceImpl<MsgTextMapper, MsgText> implements MsgTextService{
	


	@Autowired
	private MsgTextMapper msgTextMapper;

	@Override
	public List<MsgText> listForPage(MsgText searchEntity) {
		return msgTextMapper.listForPage(searchEntity);
	}

	@Override
	public MsgText getRandomMsg(String inputcode) {
		return msgTextMapper.getRandomMsg(inputcode);
	}

	@Override
	public MsgText getRandomMsg2() {
		return msgTextMapper.getRandomMsg2();
	}
	
	public MsgText getMsgTextByInputCode(String inputcode){
		return msgTextMapper.getMsgTextByInputCode(inputcode);
	}
}
