package com.cn.thinkx.ecom.eshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.eshop.domain.EshopInf;
import com.cn.thinkx.ecom.eshop.service.EshopInfService;
import com.cn.thinkx.ecom.eshopbanner.domain.EshopBanner;
import com.cn.thinkx.ecom.eshopbanner.service.EshopBannerService;
import com.cn.thinkx.ecom.eshoproutes.domain.EshopRoutes;
import com.cn.thinkx.ecom.eshoproutes.service.EshopRoutesService;
import com.cn.thinkx.ecom.system.domain.User;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "eshop")
public class EshopInfController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EshopInfService eshopInfService;

	@Autowired
	private EshopBannerService eshopBannerService;

	@Autowired
	private EshopRoutesService eshopRoutesService;

	/**
	 * 商城信息列表查询
	 * 
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/listEshopInf")
	public ModelAndView listEshopInf(HttpServletRequest req, EshopInf eshopInf) {
		ModelAndView mv = new ModelAndView("eshop/listEshopInf");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<EshopInf> pageList = eshopInfService.getEshopInfPage(startNum, pageSize, eshopInf);
			List<EshopInf> boxList = eshopInfService.selectByComboBox();
			mv.addObject("boxList", boxList);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询商城列表信息出错", e);
		}
		return mv;
	}

	/**
	 * 商城信息列表查询
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@PostMapping(value = "/listEshopInf")
	public ModelAndView listEshopInf(HttpServletRequest req, HttpServletResponse resp, EshopInf eshopInf) {
		ModelAndView mv = new ModelAndView("eshop/listEshopInf");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<EshopInf> pageList = eshopInfService.getEshopInfPage(startNum, pageSize, eshopInf);
			List<EshopInf> boxList = eshopInfService.selectByComboBox();
			mv.addObject("boxList", boxList);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询商城列表信息出错", e);
		}
		return mv;
	}

	/**
	 * 根据主键查询商城信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/getEshopInf")
	public EshopInf getEshopInf(HttpServletRequest req, EshopInf eshopInf) {
		EshopInf es = new EshopInf();
		try {
			es = eshopInfService.selectByPrimaryKey(eshopInf.getId());
		} catch (Exception e) {
			logger.error("## 查询主键为[" + eshopInf.getId() + "]的商城信息出错", e);
		}
		return es;
	}

	/**
	 * 新增商城信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/addEshopInf")
	public BaseResult<Object> addEshopInf(HttpServletRequest req, EshopInf eshop) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			eshop.setCreateUser(StringUtils.trim(user.getId().toString()));
			eshop.setUpdateUser(StringUtils.trim(user.getId().toString()));
			// 根据页面上的商城名称查询商城信息
			EshopInf eshopInf = eshopInfService.selectByEshopInf(eshop);
			// 若无此商城信息则执行新增操作
			if (eshopInf == null) {
				if (eshopInfService.insert(eshop) > 0) {
					return ResultsUtil.success();
				} else {
					return ResultsUtil.error(ExceptionEnum.eshopInfNews.ES01.getCode(),
							ExceptionEnum.eshopInfNews.ES01.getMsg());
				}
			} else {
				return ResultsUtil.error(ExceptionEnum.eshopInfNews.ES05.getCode(),
						ExceptionEnum.eshopInfNews.ES05.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("## 新增商城出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增商城出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

	/**
	 * 修改商城信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/editEshopInf")
	public BaseResult<Object> editEshopInf(HttpServletRequest req, EshopInf eshop) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			EshopInf eshopInf = eshopInfService.selectByEshopInf(eshop);
			// 若无此商城信息则执行新增操作
			if (eshopInf == null) {
				eshop.setUpdateUser(StringUtils.trim(user.getId().toString()));
				if (eshopInfService.updateByPrimaryKey(eshop) > 0) {
					return ResultsUtil.success();
				} else {
					return ResultsUtil.error(ExceptionEnum.eshopInfNews.ES02.getCode(),
							ExceptionEnum.eshopInfNews.ES02.getMsg());
				}
			} else {
				return ResultsUtil.error(ExceptionEnum.eshopInfNews.ES05.getCode(),
						ExceptionEnum.eshopInfNews.ES05.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("## 编辑商城出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 编辑商城出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

	/**
	 * 删除商城信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/deleteEshopInf")
	public BaseResult<Object> deleteEshopInf(HttpServletRequest req, EshopInf eshop) {
		try {
			EshopBanner eb = new EshopBanner();
			eb.setEshopId(eshop.getId());
			List<EshopBanner> ebList = eshopBannerService.getList(eb);
			if (ebList.size() > 0) {
				for (EshopBanner e : ebList) {
					eshopBannerService.deleteByPrimaryKey(e.getId());
				}
			}
			EshopRoutes er = new EshopRoutes();
			er.setEshopId(eshop.getId());
			List<EshopRoutes> erList = eshopRoutesService.getList(er);
			if (erList.size() > 0) {
				for (EshopRoutes e : erList) {
					eshopRoutesService.deleteByPrimaryKey(e.getId());
				}
			}
			if (eshopInfService.deleteByPrimaryKey(eshop.getId()) < 0) {
				return ResultsUtil.error(ExceptionEnum.eshopInfNews.ES03.getCode(),
						ExceptionEnum.eshopInfNews.ES03.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("## 删除商城出错", e);
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除商城出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
		return ResultsUtil.success();
	}
	
	/**
	 * 商城下拉框改变事件
	 * （根据商户名称查询商城信息）
	 *  TB_MCHNT_ESHOP_INF
	 * @param req
	 * @param eshopInf
	 * @return
	 */
	@PostMapping(value = "/nameChange")
	public EshopInf nameChange(HttpServletRequest req, EshopInf eshopInf) {
		EshopInf eshop = eshopInfService.selectByEshopName(eshopInf);
		return eshop;
	}
	
	/**
	 * 商城下拉框改变事件
	 * （根据商户名称查询商城信息）
	 *  TB_ECOM_ESHOP_INF
	 * @param req
	 * @param eshopInf
	 * @return
	 */
	@PostMapping(value = "/eshopNameChange")
	public EshopInf eshopNameChange(HttpServletRequest req, EshopInf eshopInf) {
		EshopInf eshop = eshopInfService.selectByEshopInf(eshopInf);
		return eshop;
	}

}
