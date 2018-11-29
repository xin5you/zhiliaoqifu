package com.cn.thinkx.ecom.esetting.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.Esetting;
import com.cn.thinkx.ecom.basics.goods.domain.SettingBanner;
import com.cn.thinkx.ecom.basics.goods.mapper.SettingBannerMapper;
import com.cn.thinkx.ecom.basics.goods.service.EsettingService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.esetting.service.EsettingInfService;
import com.cn.thinkx.ecom.system.domain.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("settingInfService")
public class EsettingInfServiceImpl extends BaseServiceImpl<Esetting> implements EsettingInfService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EsettingService esettingService;
	
	@Autowired
	private SettingBannerMapper settingBannerMapper;

	@Override
	public PageInfo<Esetting> getSettingListPage(int startNum, int pageSize, Esetting entity) {
		PageHelper.startPage(startNum, pageSize);
		List<Esetting> settingList = null;
		try {
			settingList = esettingService.getList(entity);
			for (Esetting esetting : settingList) {
				esetting.setFullMoney(NumberUtils.RMBCentToYuan(esetting.getFullMoney()));
				esetting.setEcomFreight(NumberUtils.RMBCentToYuan(esetting.getEcomFreight()));
				esetting.setEcomType(Constants.EcomType.findByCode(esetting.getEcomType()).getValue());
			}
		} catch (Exception e) {
			logger.error("## 查询电商服务列表异常", e);
		}
		PageInfo<Esetting> page = new PageInfo<Esetting>(settingList);
		return page;
	}

	@Override
	public BaseResult<Object> addEsetting(HttpServletRequest req, Esetting esetting) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			if (!StringUtil.isNullOrEmpty(esetting.getEcomCode())) {
				Esetting eset = esettingService.getSettingByEcomCode(esetting.getEcomCode());
				if (eset != null) {
					return ResultsUtil.error(ExceptionEnum.eshopInfNews.ES05.getCode(),
							ExceptionEnum.eshopInfNews.ES05.getMsg());
				}
			}
			esetting.setCreateUser(user.getId().toString());
			esetting.setUpdateUser(user.getId().toString());
			esetting.setFullMoney(NumberUtils.RMBYuanToCent(esetting.getFullMoney()));
			esetting.setEcomFreight(NumberUtils.RMBYuanToCent(esetting.getEcomFreight()));
			if (esettingService.insert(esetting) > 0) {
				return ResultsUtil.success();
			} else {
				return ResultsUtil.error(ExceptionEnum.eshopInfNews.ES01.getCode(),
						ExceptionEnum.eshopInfNews.ES01.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("## 添加商城服务设置出错：[{}]", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 添加商城服务设置出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

	@Override
	public BaseResult<Object> editEsetting(HttpServletRequest req, Esetting esetting) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			if (!StringUtil.isNullOrEmpty(esetting.getEcomCode())) {
				Esetting eset = esettingService.getSettingByEcomCode(esetting.getEcomCode());
				if (eset != null && !(esetting.getId().equals(eset.getId()))) {
					return ResultsUtil.error(ExceptionEnum.eshopInfNews.ES05.getCode(),
							ExceptionEnum.eshopInfNews.ES05.getMsg());
				}
			}
			esetting.setFullMoney(NumberUtils.RMBYuanToCent(esetting.getFullMoney()));
			esetting.setEcomFreight(NumberUtils.RMBYuanToCent(esetting.getEcomFreight()));
			esetting.setCreateUser(user.getId().toString());
			if (esettingService.updateByPrimaryKey(esetting) > 0) {
				return ResultsUtil.success();
			} else {
				return ResultsUtil.error(ExceptionEnum.eshopInfNews.ES02.getCode(),
						ExceptionEnum.eshopInfNews.ES02.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("## 编辑电商服务设置出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 编辑电商服务设置出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

	@Override
	public PageInfo<SettingBanner> getSettingBannerListPage(int startNum, int pageSize, SettingBanner entity, String type) {
		PageHelper.startPage(startNum, pageSize);
		List<SettingBanner> settingBannerList = null;
		try {
			if ("0".equals(type)) {
				settingBannerList = esettingService.getSettingBannerList(entity);
			}else{
				settingBannerList = esettingService.getNotSettingBannerList(entity);
			}
		} catch (Exception e) {
			logger.error("## 查询电商服务列表异常", e);
		}
		PageInfo<SettingBanner> page = new PageInfo<SettingBanner>(settingBannerList);
		return page;
	}

	@Override
	public boolean addSettingBanner(String settingId, String ids, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			User user = (User) session.getAttribute(Constants.SESSION_USER);
			String[] bannerIds = ids.split(",");
			for (int i = 0; i < bannerIds.length; i++) {
				SettingBanner settingBanner = new SettingBanner();
				settingBanner.setBannerId(bannerIds[i]);
				settingBanner.setSettingId(settingId);
				settingBanner.setUpdateUser(user.getId().toString());
				settingBanner.setCreateUser(user.getId().toString());
				settingBannerMapper.insert(settingBanner);
			}
		} catch (Exception e) {
			logger.error("## 添加商城服务的banner", e);
			return false;
		}
		return true;
	}

	@Override
	public BaseResult<Object> deleteSettingBanner(String settingId,String bannerId) {
		try {
			SettingBanner settingBanner = new SettingBanner();
			if(StringUtil.isNullOrEmpty(settingId) || StringUtil.isNullOrEmpty(bannerId)){
				return ResultsUtil.error(ExceptionEnum.bannerNews.BA12.getCode(), ExceptionEnum.bannerNews.BA12.getMsg());
			}
			settingBanner.setBannerId(bannerId);
			settingBanner.setSettingId(settingId);
			if (settingBannerMapper.deleteBySettingIdAndBannerId(settingBanner) < 0) {
				return ResultsUtil.error(ExceptionEnum.bannerNews.BA12.getCode(), ExceptionEnum.bannerNews.BA12.getMsg());
			}
		} catch (Exception e) {
			logger.error("## 删除电商banner失败，id：[{}]", settingId, e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> deleteEsetting(String settingId) {
		try {
			if(StringUtil.isNullOrEmpty(settingId)){
				return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
			}
			if (esettingService.deleteByPrimaryKey(settingId) < 0) {
				return ResultsUtil.error(ExceptionEnum.eshopInfNews.ES03.getCode(),
						ExceptionEnum.eshopInfNews.ES03.getMsg());
			}
			SettingBanner settingBanner = new SettingBanner();
			settingBanner.setSettingId(settingId);
			if (settingBannerMapper.deleteBySettingIdAndBannerId(settingBanner)< 0) {
				return ResultsUtil.error(ExceptionEnum.eshopInfNews.ES03.getCode(),
						ExceptionEnum.eshopInfNews.ES03.getMsg());
			}
		} catch (Exception e) {
			logger.error("## 删除商城出错，settingId:[{}]", settingId, e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
		return ResultsUtil.success();
	}

}
