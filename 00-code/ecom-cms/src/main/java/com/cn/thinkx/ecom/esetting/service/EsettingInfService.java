package com.cn.thinkx.ecom.esetting.service;

import javax.servlet.http.HttpServletRequest;

import com.cn.thinkx.ecom.basics.goods.domain.Esetting;
import com.cn.thinkx.ecom.basics.goods.domain.SettingBanner;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.service.BaseService;
import com.github.pagehelper.PageInfo;

public interface EsettingInfService extends BaseService<Esetting> {

	/**
	 * 查询电商服务设置信息（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<Esetting> getSettingListPage(int startNum, int pageSize, Esetting entity);

	/**
	 * 添加电商服务设置
	 * 
	 * @param req
	 * @param esetting
	 * @return
	 */
	BaseResult<Object> addEsetting(HttpServletRequest req, Esetting esetting);

	/**
	 * 修改电商服务设置
	 * 
	 * @param req
	 * @param esetting
	 * @return
	 */
	BaseResult<Object> editEsetting(HttpServletRequest req, Esetting esetting);
	
	/**
	 * 删除电商服务设置
	 * 
	 * @param settingId
	 * @return
	 */
	BaseResult<Object> deleteEsetting(String settingId);

	/**
	 * 查询商城服务对应的banner
	 * 
	 * @param entity
	 * @return
	 */
	PageInfo<SettingBanner> getSettingBannerListPage(int startNum, int pageSize, SettingBanner entity, String type);

	/**
	 * 添加商城服务的banner
	 * 
	 * @param floorId
	 * @param ids
	 * @param req
	 * @return
	 */
	boolean addSettingBanner(String settingId, String ids, HttpServletRequest req);

	/**
	 * 刪除商城服务的banner
	 * 
	 * @param settingId
	 * @return
	 */
	BaseResult<Object> deleteSettingBanner(String settingId, String bannerId);

}
