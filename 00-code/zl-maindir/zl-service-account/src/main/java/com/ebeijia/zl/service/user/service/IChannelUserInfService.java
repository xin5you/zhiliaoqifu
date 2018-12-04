package com.ebeijia.zl.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.user.vo.ChannelUserInf;


/**
 *
 * 渠道用户信息 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
public interface IChannelUserInfService extends IService<ChannelUserInf> {
	
	boolean save(ChannelUserInf entity);

}
