package com.ebeijia.zl.basics.wechat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.wechat.domain.MsgBase;
import com.ebeijia.zl.basics.wechat.domain.MsgNews;
import com.ebeijia.zl.basics.wechat.domain.MsgText;
import com.ebeijia.zl.basics.wechat.mapper.MsgBaseMapper;
import com.ebeijia.zl.basics.wechat.service.MsgBaseService;

/**
 *
 * 消息基础表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Service
public class MsgBaseServiceImpl extends ServiceImpl<MsgBaseMapper, MsgBase> implements MsgBaseService{
	
	@Autowired
	private MsgBaseMapper msgBaseMapper;

	public List<MsgBase> listForPage(MsgBase searchEntity){
		return msgBaseMapper.listForPage(searchEntity);
	}
	
	
	public List<MsgNews> listMsgNewsByBaseId(String[] ids){
		return msgBaseMapper.listMsgNewsByBaseId(ids);
	}
	
	public MsgText getMsgTextByBaseId(String id){
		return msgBaseMapper.getMsgTextByBaseId(id);
	}
}
