package com.ebeijia.zl.basics.wechat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.wechat.domain.MsgBase;
import com.ebeijia.zl.basics.wechat.domain.MsgNews;
import com.ebeijia.zl.basics.wechat.domain.MsgText;

/**
 *
 * 消息基础表 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Mapper
public interface MsgBaseMapper extends BaseMapper<MsgBase> {


	public List<MsgBase> listForPage(MsgBase searchEntity);

	public List<MsgNews> listMsgNewsByBaseId(String[] ids);
	
	public MsgText getMsgTextByBaseId(String id);
	
	/**
	 * 首次关注欢迎语
	 * @return
	 */
	public MsgText getMsgTextBySubscribe();
	/**
	 * 在次关注欢迎语
	 * @return
	 */
	public MsgText getMsgTextByAgainSubscribe();
	
	public MsgText getMsgTextByInputCode(String inputcode);
	
	
	public void updateInputcode(MsgBase entity);

}
