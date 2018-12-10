package com.ebeijia.zl.basics.wechat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.wechat.domain.MsgNews;

/**
 *
 * 消息表 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Mapper
public interface MsgNewsMapper extends BaseMapper<MsgNews> {


	public List<MsgNews> listForPage(MsgNews searchEntity);
	
	public List<MsgNews> getRandomMsg(Integer num);

	public List<MsgNews> getRandomMsgByContent(String inputcode ,Integer num);
	
	public List<MsgNews> getMsgNewsByIds(String[] array);

	/**
	 * @return
	 */
	public List<MsgNews>  getMsgNewsByCode(String code);


}
