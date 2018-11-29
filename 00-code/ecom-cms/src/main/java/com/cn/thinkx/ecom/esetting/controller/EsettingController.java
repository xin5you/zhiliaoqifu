package com.cn.thinkx.ecom.esetting.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.goods.domain.Esetting;
import com.cn.thinkx.ecom.basics.goods.domain.SettingBanner;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.esetting.service.EsettingInfService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "eSetting")
public class EsettingController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EsettingInfService settingService;

	/**
	 * 电商服务设置信息列表查询
	 * 
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/listEsetting")
	public ModelAndView listEsetting(HttpServletRequest req, Esetting esetting) {
		ModelAndView mv = new ModelAndView("esetting/listEsetting");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<Esetting> pageList = settingService.getSettingListPage(startNum, pageSize, esetting);
			mv.addObject("channelList", Constants.GoodsEcomCodeType.values());
			mv.addObject("ecomType", Constants.EcomType.values());
			mv.addObject("pageInfo", pageList);
			mv.addObject("esetting", esetting);
		} catch (Exception e) {
			logger.error("## 查询电商服务设置信息列表时出错", e);
		}
		return mv;
	}

	/**
	 * 电商服务设置信息列表查詢
	 * 
	 * @param req
	 * @retrun
	 */
	@PostMapping(value = "/listEsetting")
	public ModelAndView listEsetting(HttpServletRequest req, HttpServletResponse resp, Esetting esetting) {
		ModelAndView mv = new ModelAndView("esetting/listEsetting");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<Esetting> pageList = settingService.getSettingListPage(startNum, pageSize, esetting);
			mv.addObject("channelList", Constants.GoodsEcomCodeType.values());
			mv.addObject("ecomType", Constants.EcomType.values());
			mv.addObject("pageInfo", pageList);
			mv.addObject("esetting", esetting);
		} catch (Exception e) {
			logger.error("## 查询电商服务设置信息列表时出错", e);
		}
		return mv;
	}

	/**
	 * 根据主键查询电商服务信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/getEsetting")
	public Esetting getEsetting(HttpServletRequest req) {
		String id = req.getParameter("id");
		Esetting esetting = null;
		try {
			esetting = settingService.selectByPrimaryKey(id);
			if (esetting != null) {
				esetting.setFullMoney(NumberUtils.RMBCentToYuan(esetting.getFullMoney()));
				esetting.setEcomFreight(NumberUtils.RMBCentToYuan(esetting.getEcomFreight()));
			}
		} catch (Exception e) {
			logger.error("## 查询主键为[" + id + "]的商城信息出错", e);
		}
		return esetting;
	}

	/**
	 * 新增商城服务设置
	 * 
	 * @param req
	 * @param ecomName
	 * @return
	 * 
	 */
	@PostMapping(value = "/addEsetting")
	public BaseResult<Object> addEsetting(HttpServletRequest req, Esetting esetting) {
		return settingService.addEsetting(req, esetting);
	}

	/**
	 * 修改商城服务设置
	 * 
	 * @param req
	 * @param ecomName
	 * @return
	 * 
	 */
	@PostMapping(value = "/editEsetting")
	public BaseResult<Object> editEsetting(HttpServletRequest req, Esetting esetting) {
		return settingService.editEsetting(req, esetting);
	}

	/**
	 * 刪除商城服务设置
	 * 
	 * @param req
	 * @param ecomName
	 * @return
	 * 
	 */
	@PostMapping(value = "/deleteEserve")
	public BaseResult<Object> deleteEsetting(HttpServletRequest req) {
		String id = req.getParameter("id");
		return settingService.deleteEsetting(id);
	}

	/**
	 * 进入添加商城服务banner
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "intoAddBanner")
	public ModelAndView intoAddBanner(HttpServletRequest req, SettingBanner entity) {
		ModelAndView mv = new ModelAndView("esetting/listSettingBanner");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		String settingId = req.getParameter("settingId");
		try {
			if (StringUtil.isNullOrEmpty(settingId)) {
				return null;
			}
			entity.setBannerId(settingId);
			PageInfo<SettingBanner> pageList = settingService.getSettingBannerListPage(startNum, pageSize, entity, "0");
			mv.addObject("pageInfo", pageList);
			mv.addObject("entity", entity);
			mv.addObject("settingId", settingId);
		} catch (Exception e) {
			logger.error("## 查询商城服务banner列表时出错", e);
		}
		return mv;

	}

	/**
	 * 进入添加商城服务banner
	 * 
	 * @param req
	 * @return
	 */
	@GetMapping(value = "intoAddBanner")
	public ModelAndView intoAddBanner(HttpServletRequest req, HttpServletResponse resp, SettingBanner entity) {
		ModelAndView mv = new ModelAndView("esetting/listSettingBanner");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		String settingId = req.getParameter("settingId");
		try {
			if (StringUtil.isNullOrEmpty(settingId)) {
				return null;
			}
			entity.setBannerId(settingId);
			PageInfo<SettingBanner> pageList = settingService.getSettingBannerListPage(startNum, pageSize, entity, "0");
			mv.addObject("pageInfo", pageList);
			mv.addObject("entity", entity);
			mv.addObject("settingId", settingId);
		} catch (Exception e) {
			logger.error("## 查询商城服务banner列表时出错", e);
		}
		return mv;
	}

	/**
	 * 进入没有添加商城服务的banner
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/intoNotSettingBannerList")
	public ModelAndView intoNotSettingBannerList(HttpServletRequest req, SettingBanner entity) {
		ModelAndView mv = new ModelAndView("esetting/listNotSettingBanner");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		String settingId = req.getParameter("settingId");
		try {
			if (StringUtil.isNullOrEmpty(settingId)) {
				return null;
			}
			if (!StringUtil.isNullOrEmpty(entity.getSettingId())) {
				settingId = entity.getSettingId();
			} else {
				entity.setSettingId(settingId);
			}
			PageInfo<SettingBanner> pageList = settingService.getSettingBannerListPage(startNum, pageSize, entity, "1");
			mv.addObject("pageInfo", pageList);
			mv.addObject("entity", entity);
			mv.addObject("settingId", settingId);
		} catch (Exception e) {
			logger.error("## 查询没有添加商城服务的banner列表时出错", e);
		}
		return mv;
	}

	/**
	 * 进入没有添加商城服务的banner
	 * 
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/intoNotSettingBannerList")
	public ModelAndView intoNotSettingBannerList(HttpServletRequest req, HttpServletResponse resp,
			SettingBanner entity) {
		ModelAndView mv = new ModelAndView("esetting/listNotSettingBanner");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		String settingId = req.getParameter("settingId");
		try {
			if (StringUtil.isNullOrEmpty(settingId)) {
				return null;
			}
			if (!StringUtil.isNullOrEmpty(entity.getSettingId())) {
				settingId = entity.getSettingId();
			} else {
				entity.setSettingId(settingId);
			}
			PageInfo<SettingBanner> pageList = settingService.getSettingBannerListPage(startNum, pageSize, entity, "1");
			mv.addObject("pageInfo", pageList);
			mv.addObject("entity", entity);
			mv.addObject("settingId", settingId);
		} catch (Exception e) {
			logger.error("## 查询没有添加商城服务的banner列表时出错", e);
		}
		return mv;
	}

	/**
	 * 添加商城服务的banner
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/addSettingBanner")
	public BaseResult<Object> addSettingBanner(HttpServletRequest req) {
		try {
			String settingId = req.getParameter("settingId");
			String ids = req.getParameter("ids");
			if (StringUtils.isEmpty(ids)) {
				return ResultsUtil.error(ExceptionEnum.userNews.UN10.getCode(), ExceptionEnum.userNews.UN10.getMsg());
			} else {
				if (settingService.addSettingBanner(settingId, ids, req)) {
					return ResultsUtil.success();
				} else {
					return ResultsUtil.error(ExceptionEnum.bannerNews.BA06.getCode(),
							ExceptionEnum.bannerNews.BA06.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error("## 添加商城服务的banner", e);
			return ResultsUtil.error(ExceptionEnum.bannerNews.BA06.getCode(), ExceptionEnum.bannerNews.BA06.getMsg());
		}
	}

	/**
	 * 删除商城服务的banner
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "deleteSettingBanner")
	public BaseResult<Object> deleteSettingBanner(HttpServletRequest req) {
		String settingId = req.getParameter("settingId");
		String bannerId = req.getParameter("bannerId");
		return settingService.deleteSettingBanner(settingId, bannerId);
	}
}