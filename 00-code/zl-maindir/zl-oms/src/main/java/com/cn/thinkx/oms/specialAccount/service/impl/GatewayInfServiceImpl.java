package com.cn.thinkx.oms.specialAccount.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cn.thinkx.oms.specialAccount.mapper.GatewayBillingTypeInfMapper;
import com.cn.thinkx.oms.specialAccount.mapper.GatewayInfMapper;
import com.cn.thinkx.oms.specialAccount.model.BillingTypeInf;
import com.cn.thinkx.oms.specialAccount.model.GatewayBillingTypeInf;
import com.cn.thinkx.oms.specialAccount.model.GatewayInf;
import com.cn.thinkx.oms.specialAccount.service.BillingTypeInfService;
import com.cn.thinkx.oms.specialAccount.service.GatewayInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("gatewayInfService")
public class GatewayInfServiceImpl implements GatewayInfService {
	
	@Autowired
	@Qualifier("gatewayInfMapper")
	private GatewayInfMapper gatewayInfMapper;
	
	@Autowired
	@Qualifier("gatewayBillingTypeInfMapper")
	private GatewayBillingTypeInfMapper gatewayBillingTypeInfMapper;
	
	@Autowired
	@Qualifier("billingTypeInfService")
	private BillingTypeInfService billingTypeInfService;

	/**
	 * 查找网关信息
	 * @param gId
	 *        网关id
	 * @return 对应网关信息对象
	 */
	public GatewayInf getGatewayInfById(String gId) {
		// TODO Auto-generated method stub
		GatewayInf gatewayInf = gatewayInfMapper.getGatewayInfById(gId);
		if (gatewayInf == null) {//如果是空说明该网关下没有专用账户类型，就从列表中查找这个网关
			List<GatewayInf> gList = getGatewayInfList(null);
			for (GatewayInf g : gList) {
				if (g.getgId().equals(gId)) {
					gatewayInf = g;
				}
			}
		}
		return gatewayInf;
	}

	/**
	 * 通过网关name,code得到网关信息
	 * @param gatewayInf 设置了name,code属性的网关对象
	 * @return 对应网关信息对象
	 */
	public GatewayInf getGatewayInfByGatewayInf(GatewayInf gatewayInf) {
		// TODO Auto-generated method stub
		return gatewayInfMapper.getGatewayInfByGatewayInf(gatewayInf);
	}

	/**
	 * 保存网关信息
	 * @param gatewayInf
	 *        网关对象
	 * @param bIds
	 *        从前台得到的专有账户类型的id数组
	 * @return 布尔值，返回true为新增成功
	 */
	public Boolean insertGatewayInf(GatewayInf gatewayInf, String[] bIds) {
		// TODO Auto-generated method stub
		int row = gatewayInfMapper.insertGatewayInf(gatewayInf);//新增网关
		GatewayBillingTypeInf gatewayBillingTypeInf = new GatewayBillingTypeInf();
		gatewayBillingTypeInf.setgId(gatewayInf.getgId());
		gatewayBillingTypeInf.setCreateUser(gatewayInf.getCreateUser());
		
		if (bIds == null || bIds.length == 0) {
			return row>0;
		} else {
			try {
				Arrays.asList(bIds).stream().forEach(bId -> {//新增网关下的专用账户类型
					gatewayBillingTypeInf.setbId(bId);
					gatewayBillingTypeInfMapper.insertGatewayBillingTypeInf(gatewayBillingTypeInf);
				});
				
			} catch (Exception e) {
				return false;
			}

			return true;
		}
	   
	}

	/**
	 * 修改网关信息
	 * @param gatewayInf
	 *        网关对象
	 * @param bIds
	 *        从前台得到的专有账户类型的id数组
	 * @return 布尔值，返回true为修改成功
	 */
	public Boolean updateGatewayInf(GatewayInf gatewayInf, String[] bIds) {
		// TODO Auto-generated method stub
				try {
					gatewayInfMapper.updateGatewayInf(gatewayInf);//修改网关
					
					String gId = gatewayInf.getgId();
					gatewayInf = getGatewayInfById(gId);
					if (gatewayInf == null) {//如果是空说明该网关下没有专用账户类型，就从列表中查找这个网关
						List<GatewayInf> gList = gatewayInfMapper.getGatewayInfList(null);
						for (GatewayInf g : gList) {
							if (g.getgId().equals(gId)) {
								gatewayInf = g;
							}
						}
					}
					
					List<BillingTypeInf> bList = gatewayInf.getbList();//得到该网管拥有的专有账户类型列表
					
					GatewayBillingTypeInf gatewayBillingTypeInf = new GatewayBillingTypeInf();
					gatewayBillingTypeInf.setgId(gatewayInf.getgId());
					gatewayBillingTypeInf.setCreateUser(gatewayInf.getCreateUser());
					
					if (( bIds == null || bIds.length == 0 ) && bList.size() > 0) {//修改后的专有账户类型列表为空，但是之前拥有的专有账户类型列表不是空，就将之前拥有的全部删除
						bList.stream().forEach(b -> {
							gatewayBillingTypeInf.setbId(b.getbId());
							gatewayBillingTypeInfMapper.deleteGatewayBillingTypeInf(gatewayBillingTypeInf);
						});
					}else if (( bIds != null && bIds.length > 0 ) && (bList.size() == 0 || bList==null)) {//修改后的专有账户类型列表不是空，但是之前拥有的专有账户类型列表是空，就将需要修改的专有账户类型列表全部执行新增
						Arrays.asList(bIds).stream().forEach(bId -> {
							gatewayBillingTypeInf.setbId(bId);
							gatewayBillingTypeInfMapper.insertGatewayBillingTypeInf(gatewayBillingTypeInf);
						});
					}else if (( bIds != null && bIds.length > 0 ) && bList.size() > 0) {//两者都不为空的话，进行判断
					
					for (String bId : bIds) {//如果修改前没有但是修改后拥有的专有账户类型，执行新增
						int count = 0;
						for (BillingTypeInf b : bList) {
							if (b.getbId().equals(bId)) {
								count++;
							}
						}
						if (count == 0) {
							gatewayBillingTypeInf.setbId(bId);
							gatewayBillingTypeInfMapper.insertGatewayBillingTypeInf(gatewayBillingTypeInf);
						}
					}
					
					for (BillingTypeInf b : bList) {//如果修改前有但是修改后没有的专有账户类型，执行删除
						int count =0;
						for (String bId : bIds) {
							if (b.getbId().equals(bId)) {
								count++;
							}
						}
						if (count == 0) {
							gatewayBillingTypeInf.setbId(b.getbId());
							gatewayBillingTypeInfMapper.deleteGatewayBillingTypeInf(gatewayBillingTypeInf);
						}
					}
					
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			
		return true;
	}

	/**
	 * 删除网关信息
	 * @param gatewayInf
	 *        网关对象
	 * @return 布尔值，返回true为删除成功
	 */
	public Boolean deleteGatewayInf(GatewayInf gatewayInf) {
		// TODO Auto-generated method stub
		GatewayBillingTypeInf gatewayBillingTypeInf = new GatewayBillingTypeInf();
		gatewayBillingTypeInf.setgId(gatewayInf.getgId());
		gatewayBillingTypeInfMapper.deleteGatewayBillingTypeInfByGId(gatewayBillingTypeInf);
		return gatewayInfMapper.deleteGatewayInf(gatewayInf)>0;
	}

	/**
	 * 查询所有企业
	 * @param gatewayInf
	 *        包含查询条件的网关对象
	 * @return 包含所有符合条件的网关对象集合
	 */
	public List<GatewayInf> getGatewayInfList(GatewayInf gatewayInf) {
		// TODO Auto-generated method stub
		return gatewayInfMapper.getGatewayInfList(gatewayInf);
	}

	/**
	 * 结合分页查询所有企业
	 * @param startNum 起始页
	 * @param pageSize 每页数据量
	 * @param gatewayInf 包含查询条件的网关对象
	 * @return 包含所有符合条件的网关对象PageInfo对象
	 */
	public PageInfo<GatewayInf> getGatewayInfList(int startNum, int pageSize, GatewayInf gatewayInf) {
		// TODO Auto-generated method stub
		PageHelper.startPage(startNum, pageSize);
		List<GatewayInf> list = getGatewayInfList(gatewayInf);
		PageInfo<GatewayInf> page = new PageInfo<GatewayInf>(list);
		return page;
	}

	
}
