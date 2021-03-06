package com.ebeijia.zl.basics.wechat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.wechat.domain.MsgText;

/**
 *
 * 文本消息表 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Mapper
public interface MsgTextMapper extends BaseMapper<MsgText> {

	List<MsgText> listForPage(MsgText searchEntity);
	 
	MsgText getRandomMsg(String inputCode);
	
	MsgText getRandomMsg2();

	MsgText getMsgTextByInputCode(String inputcode);
}
