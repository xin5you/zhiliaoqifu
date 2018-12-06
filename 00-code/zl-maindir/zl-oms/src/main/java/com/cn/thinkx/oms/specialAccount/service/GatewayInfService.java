package com.cn.thinkx.oms.specialAccount.service;

import java.util.List;

import com.cn.thinkx.oms.specialAccount.model.GatewayInf;
import com.github.pagehelper.PageInfo;

public interface GatewayInfService {


	/**
	 * 查找网关信息
	 * @param gId
	 *        网关id
	 * @return 对应网关信息对象
	 */
	public GatewayInf getGatewayInfById(String gId);
	
	/**
	 * 通过网关name,code得到网关信息
	 * @param gatewayInf 设置了name,code属性的网关对象
	 * @return 对应网关信息对象
	 */
	public GatewayInf getGatewayInfByGatewayInf(GatewayInf gatewayInf);
	
	/**
	 * 保存网关信息
	 * @param gatewayInf
	 *        网关对象
	 * @param bIds
	 *        从前台得到的专有账户类型的id数组
	 * @return 布尔值，返回true为新增成功
	 */
	public Boolean insertGatewayInf(GatewayInf gatewayInf,String[] bIds);
	
	/**
	 * 修改网关信息
	 * @param gatewayInf
	 *        网关对象
	 * @param bIds
	 *        从前台得到的专有账户类型的id数组
	 * @return 布尔值，返回true为修改成功
	 */
	public Boolean updateGatewayInf(GatewayInf gatewayInf,String[] bIds);
	
	
	/**
	 * 删除网关信息
	 * @param gatewayInf
	 *        网关对象
	 * @return 布尔值，返回true为删除成功
	 */
	public Boolean deleteGatewayInf(GatewayInf gatewayInf);
	
	/**
	 * 查询所有企业
	 * @param gatewayInf
	 *        包含查询条件的网关对象
	 * @return 包含所有符合条件的网关对象集合
	 */
	public List<GatewayInf> getGatewayInfList(GatewayInf gatewayInf);
	
	/**
	 * 结合分页查询所有企业
	 * @param startNum 起始页
	 * @param pageSize 每页数据量
	 * @param gatewayInf 包含查询条件的网关对象
	 * @return 包含所有符合条件的网关对象PageInfo对象
	 */
	public PageInfo<GatewayInf> getGatewayInfList(int startNum, int pageSize,GatewayInf gatewayInf);
	
}
