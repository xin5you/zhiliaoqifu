package com.ebeijia.zl.web.cms.goodsmanage.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.GoodsSpecTypeEnum;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomSpecValues;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomSpecification;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomSpecValuesService;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomSpecificationService;
import com.ebeijia.zl.web.cms.base.exception.BizHandlerException;
import com.ebeijia.zl.web.cms.goodsmanage.service.GoodsManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "goodsManage")
public class GoodsManageController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GoodsManageService goodsManageService;

	@Autowired
	private ITbEcomSpecificationService ecomSpecificationService;

	@Autowired
	private ITbEcomSpecValuesService ecomSpecValuesService;

	/**
	 * 商品规格信息列表（分页）
	 * @param req
	 * @param ecomSpecification
	 * @return
	 */
	@RequestMapping(value = "/goodsSpec/getGoodsSpecList")
	public ModelAndView getGoodsSpecList(HttpServletRequest req, TbEcomSpecification ecomSpecification) {
		ModelAndView mv = new ModelAndView("goodsManage/listGoodsSpec");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<TbEcomSpecification> pageInfo = new PageInfo<>();
		try {
			pageInfo = goodsManageService.getGodosSpecListPage(startNum, pageSize, ecomSpecification);
		} catch (Exception e) {
			logger.error("## 查询商品规格信息异常[{}]", e);
		}
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("ecomSpecification", ecomSpecification);
		return mv;
	}

	/**
	 * 根据主键查询商品规格信息
	 * @param req
	 * @param ecomSpecification
	 * @return
	 */
	@PostMapping(value = "/goodsSpec/getGoodsSpec")
	public TbEcomSpecification getGoodsSpec(HttpServletRequest req, TbEcomSpecification ecomSpecification) {
		TbEcomSpecification goodsSpec = new TbEcomSpecification();
		try {
			goodsSpec = ecomSpecificationService.getById(ecomSpecification.getSpecId());
		} catch (Exception e) {
			logger.error("## 查询主键为[{}]的商品规格信息出错", ecomSpecification.getSpecId(), e);
		}
		return goodsSpec;
	}

	/**
	 * 新增商品规格信息
	 * @param specImgFile
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsSpec/addGoodsSpec")
	public BaseResult<Object> addGoodsSpec(@RequestParam("specImgFile") MultipartFile specImgFile, HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomSpecification ecomSpecification = getTbEcomSpecification(req);
			result = goodsManageService.addGoodsSpec(ecomSpecification, specImgFile);
		} catch (BizHandlerException e) {
			logger.error("## 商品规格图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增商品规格信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews01.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews01.getMsg());
		}
		return result;
	}

	/**
	 * 编辑商品规格信息
	 * @param specImgFile
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsSpec/editGoodsSpec")
	public BaseResult<Object> editGoodsSpec(@RequestParam("specImgFile") MultipartFile specImgFile, HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomSpecification ecomSpecification = getTbEcomSpecification(req);
			result = goodsManageService.editGoodsSpec(ecomSpecification, specImgFile);
		} catch (BizHandlerException e) {
			logger.error("## 商品规格图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 编辑商品规格信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews02.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews02.getMsg());
		}
		return result;
	}

	/**
	 * 删除商品规格信息
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsSpec/deleteGoodsSpec")
	public BaseResult<Object> deleteGoodsSpec(HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomSpecification ecomSpecification = getTbEcomSpecification(req);
			result = goodsManageService.deleteGoodsSpec(ecomSpecification);
		} catch (BizHandlerException e) {
			logger.error("## 删除商品规格图片异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除商品规格信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews03.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews03.getMsg());
		}
		return result;
	}

	/**
	 * 商品规格信息封装类方法
	 * @param req
	 * @return
	 */
	private TbEcomSpecification getTbEcomSpecification(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);

		String specId = req.getParameter("spec_id");
		String specName = req.getParameter("spec_name");
		String specImg = req.getParameter("spec_img");
		String specOrder = req.getParameter("spec_order");
		String remarks = req.getParameter("remarks");

		TbEcomSpecification spec = null;
		if (!StringUtil.isNullOrEmpty(specId)) {
			spec = ecomSpecificationService.getById(specId);
			spec.setLockVersion(spec.getLockVersion() + 1);
		} else {
			spec = new TbEcomSpecification();
			spec.setSpecId(IdUtil.getNextId());
			spec.setCreateUser(user.getId());
			spec.setCreateTime(System.currentTimeMillis());
			spec.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			spec.setIsDel("0");
			spec.setLockVersion(0);
		}
		spec.setUpdateUser(user.getId());
		spec.setUpdateTime(System.currentTimeMillis());
		spec.setSpecName(specName);
		spec.setSpecImg(specImg);
		spec.setSpecOrder(Integer.valueOf(specOrder));
		spec.setRemarks(remarks);
		return spec;
	}

	/**
	 * 查询商品规格值列表（分页）
	 * @param req
	 * @param ecomSpecValues
	 * @return
	 */
	@RequestMapping(value = "/goodsSpec/getGoodsSpecValuesList")
	public ModelAndView getGoodsSpecValuesList(HttpServletRequest req, TbEcomSpecValues ecomSpecValues) {
		ModelAndView mv = new ModelAndView("goodsManage/listGoodsSpecValues");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<TbEcomSpecValues> pageInfo = new PageInfo<>();
		try {
			ecomSpecValues.setSpecId(req.getParameter("specId"));
			pageInfo = goodsManageService.getGodosSpecValuesListPage(startNum, pageSize, ecomSpecValues);
		} catch (Exception e) {
			logger.error("## 查询商品规格值信息异常[{}]", e);
		}
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("ecomSpecValues", ecomSpecValues);
		mv.addObject("goodsSpecTypeList", GoodsSpecTypeEnum.values());
		return mv;
	}

	/**
	 * 根据主键查询商品规格值信息
	 * @param req
	 * @param ecomSpecValues
	 * @return
	 */
	@PostMapping(value = "/goodsSpec/getGoodsSpecValues")
	public TbEcomSpecValues getGoodsSpecValues(HttpServletRequest req, TbEcomSpecValues ecomSpecValues) {
		TbEcomSpecValues goodsSpecValues = new TbEcomSpecValues();
		try {
			goodsSpecValues = ecomSpecValuesService.getById(ecomSpecValues.getSpecValueId());
		} catch (Exception e) {
			logger.error("## 查询主键为[{}]的商品规格值信息出错", ecomSpecValues.getSpecValueId(), e);
		}
		return goodsSpecValues;
	}

	/**
	 * 新增商品规格值信息
	 * @param specImageFile
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsSpec/addGoodsSpecValues")
	public BaseResult<Object> addGoodsSpecValues(@RequestParam("specImageFile") MultipartFile specImageFile, HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomSpecValues specValues = getTbEcomSpecValues(req);
			result = goodsManageService.addGoodsSpecValues(specValues, specImageFile);
		} catch (BizHandlerException e) {
			logger.error("## 商品规格值图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增商品规格值信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews04.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews04.getMsg());
		}
		return result;
	}

	/**
	 * 编辑商品规格值信息
	 * @param specImageFile
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsSpec/editGoodsSpecValues")
	public BaseResult<Object> editGoodsSpecValues(@RequestParam("specImageFile") MultipartFile specImageFile, HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomSpecValues specValues = getTbEcomSpecValues(req);
			result = goodsManageService.editGoodsSpecValues(specValues, specImageFile);
		} catch (BizHandlerException e) {
			logger.error("## 商品规格值图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 编辑商品规格值信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews05.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews05.getMsg());
		}
		return result;
	}

	/**
	 * 删除商品规格值信息
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsSpec/deleteGoodsSpecValues")
	public BaseResult<Object> deleteGoodsSpecValues(HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomSpecValues specValues = getTbEcomSpecValues(req);
			result = goodsManageService.deleteGoodsSpecValues(specValues);
		} catch (BizHandlerException e) {
			logger.error("## 删除商品规格值图片异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除商品规格值信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews06.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews06.getMsg());
		}
		return result;
	}

	/**
	 * 商品规格信息值封装类方法
	 * @param req
	 * @return
	 */
	private TbEcomSpecValues getTbEcomSpecValues(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);

		String specId = req.getParameter("spec_id");
		String specValueId = req.getParameter("spec_value_id");
		String specValue = req.getParameter("spec_value");
		String specImage = req.getParameter("spec_image");
		String specOrder = req.getParameter("spec_order");
		String specType = req.getParameter("spec_type");
		String remarks = req.getParameter("remarks");

		TbEcomSpecValues specValues = null;
		if (!StringUtil.isNullOrEmpty(specValueId)) {
			specValues = ecomSpecValuesService.getById(specValueId);
			specValues.setLockVersion(specValues.getLockVersion() + 1);
		} else {
			specValues = new TbEcomSpecValues();
			specValues.setSpecValueId(IdUtil.getNextId());
			specValues.setCreateUser(user.getId());
			specValues.setCreateTime(System.currentTimeMillis());
			specValues.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			specValues.setLockVersion(0);
		}
		specValues.setSpecId(specId);
		specValues.setUpdateUser(user.getId());
		specValues.setUpdateTime(System.currentTimeMillis());
		specValues.setSpecOrder(Integer.valueOf(specOrder));
		specValues.setSpecType(specType);
		if (GoodsSpecTypeEnum.GoodsSpecTypeEnum_1.getCode().equals(specType)) {
			specValues.setSpecImage(specImage);
		}
		if (GoodsSpecTypeEnum.GoodsSpecTypeEnum_0.getCode().equals(specType)) {
			specValues.setSpecValue(specValue);
		}
		specValues.setRemarks(remarks);
		return specValues;
	}

}
