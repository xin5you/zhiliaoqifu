package com.ebeijia.zl.web.cms.goodsmanage.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlInfFacade;
import com.ebeijia.zl.shop.dao.goods.domain.*;
import com.ebeijia.zl.shop.dao.goods.service.*;
import com.ebeijia.zl.web.cms.base.exception.BizHandlerException;
import com.ebeijia.zl.web.cms.goodsmanage.service.GoodsManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

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

	@Autowired
	private ITbEcomGoodsProductService ecomGoodsProductService;

	@Autowired
	private ITbEcomGoodsDetailService ecomGoodsDetailService;

	@Autowired
	private ITbEcomGoodsService ecomGoodsService;

	@Autowired
	private ITbEcomGoodsGalleryService ecomGoodsGalleryService;

	@Autowired
	private ITbEcomGoodsSpecService ecomGoodsSpecService;

	@Autowired
	private ProviderInfFacade providerInfFacade;

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
			String specId = req.getParameter("specId");
			TbEcomSpecification ecomSpecification = ecomSpecificationService.getById(specId);
			ecomSpecification.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
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
			String specValueId = req.getParameter("specValueId");
			TbEcomSpecValues specValues = ecomSpecValuesService.getById(specValueId);
			specValues.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
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
	 * 根据规格ID查询规格值信息
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsSpec/getSpecValuesBySpecId")
	public List<TbEcomSpecValues> getSpecValuesBySpecId(HttpServletRequest req) {
		String specId = req.getParameter("specId");
		if (StringUtil.isNullOrEmpty(specId)) {
			logger.error("## 根据规格ID查询规格值信息，规格ID为空");
			return null;
		}
		TbEcomSpecValues goodsSpecValues = new TbEcomSpecValues();
		goodsSpecValues.setSpecId(specId);
		List<TbEcomSpecValues> goodsSpecValueList = ecomSpecValuesService.getGoodsSpecValuesList(goodsSpecValues);
		return goodsSpecValueList;
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
		String specValueName = req.getParameter("spec_value_name");
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
		specValues.setSpecValueName(specValueName);
		if (GoodsSpecTypeEnum.GoodsSpecTypeEnum_1.getCode().equals(specType)) {
			specValues.setSpecImage(specImage);
			specValues.setSpecValue("");
		}
		if (GoodsSpecTypeEnum.GoodsSpecTypeEnum_0.getCode().equals(specType)) {
			specValues.setSpecValue(specValue);
			specValues.setSpecImage("");
		}
		specValues.setRemarks(remarks);
		return specValues;
	}

	/**
	 * 商品信息Spu列表（分页）
	 * @param req
	 * @param ecomGoods
	 * @return
	 */
	@RequestMapping(value = "/goodsInf/getGoodsInfList")
	public ModelAndView getGoodsInfList(HttpServletRequest req, TbEcomGoods ecomGoods) {
		ModelAndView mv = new ModelAndView("goodsManage/listGoodsInf");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<TbEcomGoods> pageInfo = new PageInfo<>();
		List<ProviderInf> providerInfList = new ArrayList<>();
		List<TbEcomGoodsDetail> goodsDetailList = new ArrayList<>();
		try {
			pageInfo = goodsManageService.getGoodsInfListPage(startNum, pageSize, ecomGoods);
			ProviderInf providerInf = new ProviderInf();
			providerInf.setIsOpen(IsOpenAccountEnum.ISOPEN_TRUE.getCode());
			providerInfList = providerInfFacade.getProviderInfList(providerInf);
			goodsDetailList = ecomGoodsDetailService.getGoodsDetailList(new TbEcomGoodsDetail());
		} catch (Exception e) {
			logger.error("## 查询商品Spu信息异常{}", e);
		}
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("ecomGoods", ecomGoods);
		mv.addObject("ecomCodeList", providerInfList);
		mv.addObject("goodsIsHotList", GoodsIsHotEnum.values());
		mv.addObject("isDisabledList", IsDisabledEnum.values());
		mv.addObject("haveGroupsList", HaveGroupsEnum.values());
		mv.addObject("marketEnableList", MarketEnableEnum.values());
		mv.addObject("goodsTypeList", GoodsTypeEnum.values());
		mv.addObject("specAccountTypeList", SpecAccountTypeEnum.values());
		mv.addObject("goodsUnitList", GoodsUnitEnum.values());
		mv.addObject("goodsDetailList", goodsDetailList);

		return mv;
	}

	/**
	 * 查询商品Spu信息
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/getGoodsInf")
	public TbEcomGoods getGoodsInf(HttpServletRequest req) {
		String goodsId = req.getParameter("goodsId");
		TbEcomGoods goods = new TbEcomGoods();
		try {
			goods = ecomGoodsService.getById(goodsId);
		} catch (Exception e) {
			logger.error("## 查询商品Spu{}信息异常", goodsId, e);
		}
		return goods;
	}

	/**
	 * 新增商品信息
	 * @param goodsImgFile
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/addGoodsInf")
	public BaseResult<Object> addGoodsInf(@RequestParam("goodsImgFile") MultipartFile goodsImgFile, HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomGoods goods = getTbEcomGoodsInf(req);
			result = goodsManageService.addGoodsInf(goods, goodsImgFile);
		} catch (BizHandlerException e) {
			logger.error("## 商品信息图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增商品信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews07.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews07.getMsg());
		}
		return result;
	}

	/**
	 * 编辑商品信息
	 * @param goodsImgFile
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/editGoodsInf")
	public BaseResult<Object> editGoodsInf(@RequestParam("goodsImgFile") MultipartFile goodsImgFile, HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomGoods goods = getTbEcomGoodsInf(req);
			result = goodsManageService.editGoodsInf(goods, goodsImgFile);
		} catch (BizHandlerException e) {
			logger.error("## 商品信息图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 编辑商品信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews08.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews08.getMsg());
		}
		return result;
	}

	/**
	 * 删除商品信息
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/deleteGoodsInf")
	public BaseResult<Object> deleteGoodsInf(HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		String goodsId = req.getParameter("goods_id");
		try {
			result = goodsManageService.deleteGoodsInf(req);
		} catch (Exception e) {
			logger.error("# 删除商品{}Spu信息异常", goodsId, e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews09.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews09.getMsg());
		}
		return result;
	}

	/**
	 * 更新商品上下架状态
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/updateGoodsInfEnable")
	public BaseResult<Object> updateGoodsInfEnable(HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			result = goodsManageService.updateGoodsInfEnable(req);
		} catch (Exception e) {
			logger.error("## 更新商品Spu上下架出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews10.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews10.getMsg());
		}
		return result;
	}

	/**
	 * 封装商品信息类
	 * @param req
	 * @return
	 */
	private TbEcomGoods getTbEcomGoodsInf(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);

		String goodsId = req.getParameter("goods_id");
		String goodsName = req.getParameter("goods_name");
		String goodsImg = req.getParameter("goods_img");
		String spuCode = req.getParameter("spu_code");
		String ecomCode = req.getParameter("ecom_code");
		String goodsType = req.getParameter("goods_type");
		String bId = req.getParameter("b_id");
		String unit = req.getParameter("unit");
		String weight = req.getParameter("weight");
		/*String defaultSkuCode = req.getParameter("default_sku_code");*/
		/*String marketEnable = req.getParameter("market_enable");*/
		String brief = req.getParameter("brief");
		String goodsDetail = req.getParameter("goods_detail");
		String haveGroups = req.getParameter("have_groups");
		String isDisabled = req.getParameter("is_disabled");
		String isHot = req.getParameter("is_hot");
		String remarks = req.getParameter("remarks");
		String havaParams = req.getParameter("hava_params");
		String haveSpec = req.getParameter("have_spec");
		String ponumber = req.getParameter("ponumber");
		String goodsSord = req.getParameter("goods_sord");
		String goodsWeight = req.getParameter("goods_weight");
		String grade = req.getParameter("grade");

		TbEcomGoods goods = null;
		if (!StringUtil.isNullOrEmpty(goodsId)) {
			goods = ecomGoodsService.getById(goodsId);
			goods.setLockVersion(goods.getLockVersion() + 1);
		} else {
			goods = new TbEcomGoods();
			goods.setGoodsId(IdUtil.getNextId());
			goods.setCreateUser(user.getId());
			goods.setCreateTime(System.currentTimeMillis());
			goods.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			goods.setLockVersion(0);
		}
		goods.setUpdateUser(user.getId());
		goods.setUpdateTime(System.currentTimeMillis());
		goods.setGoodsName(goodsName);
		goods.setSpuCode(spuCode);
		goods.setEcomCode(ecomCode);
		goods.setGoodsType(goodsType);
		goods.setBId(bId);
		goods.setUnit(unit);
		goods.setWeight(new BigDecimal(weight));
		/*goods.setDefaultSkuCode(defaultSkuCode);*/
		/*goods.setMarketEnable(marketEnable);*/
		goods.setMarketEnable(MarketEnableEnum.MarketEnableEnum_1.getCode());
		goods.setBrief(brief);
		goods.setGoodsDetail(goodsDetail);
		goods.setHaveGroups(haveGroups);
		if (!StringUtil.isNullOrEmpty(havaParams)) {
			goods.setHaveParams(havaParams);
		}
		if (!StringUtil.isNullOrEmpty(haveSpec)) {
			goods.setHaveSpec(haveSpec);
		}
		if (!StringUtil.isNullOrEmpty(ponumber)) {
			goods.setPonumber(Integer.valueOf(ponumber));
		}
		if (!StringUtil.isNullOrEmpty(goodsSord)) {
			goods.setGoodsSord(Integer.valueOf(goodsSord));
		}
		if (!StringUtil.isNullOrEmpty(goodsWeight)) {
			goods.setGoodsWeight(goodsWeight);
		}
		if (!StringUtil.isNullOrEmpty(grade)) {
			goods.setGrade(Integer.valueOf(grade));
		}
		goods.setIsDisabled(isDisabled);
		goods.setIsHot(isHot);
		goods.setGoodsImg(goodsImg);
		goods.setRemarks(remarks);
		return goods;
	}

	/**
	 * 查询商品相册信息列表（分页）
	 * @param req
	 * @param ecomGoodsGallery
	 * @return
	 */
	@RequestMapping(value = "/goodsInf/getGoodsGalleryList")
	public ModelAndView getGoodsGalleryList(HttpServletRequest req, TbEcomGoodsGallery ecomGoodsGallery) {
		ModelAndView mv = new ModelAndView("goodsManage/listGoodsGallery");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<TbEcomGoodsGallery> pageInfo = new PageInfo<>();
		String goodsId = req.getParameter("goodsId");
		try {
			ecomGoodsGallery.setGoodsId(goodsId);
			pageInfo = goodsManageService.getGoodsGalleryListPage(startNum, pageSize, ecomGoodsGallery);
		} catch (Exception e) {
			logger.error("## 查询商品{}相册信息异常", goodsId, e);
		}
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("ecomGoodsGallery", ecomGoodsGallery);
		mv.addObject("isDefaultList", IsDefaultEnum.values());
		mv.addObject("goodsId", goodsId);
		return mv;
	}

	/**
	 * 根据主键查询商品相册信息
	 * @param req
	 * @param ecomGoodsGallery
	 * @return
	 */
	@PostMapping(value = "/goodsInf/getGoodsGallery")
	public TbEcomGoodsGallery getGoodsGallery(HttpServletRequest req, TbEcomGoodsGallery ecomGoodsGallery) {
		TbEcomGoodsGallery goodsGallery = new TbEcomGoodsGallery();
		try {
			goodsGallery = ecomGoodsGalleryService.getById(ecomGoodsGallery.getImgId());
		} catch (Exception e) {
			logger.error("## 查询主键为[{}]的商品相册信息出错", ecomGoodsGallery.getImgId(), e);
		}
		return goodsGallery;
	}

	/**
	 * 新增商品相册信息
	 * @param thumbnailFile
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/addGoodsGallery")
	public BaseResult<Object> addGoodsGallery(@RequestParam("originalFile") MultipartFile originalFile, HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomGoodsGallery goodsGallery = getTbEcomGoodsGallery(req);
			result = goodsManageService.addGoodsGallery(goodsGallery, originalFile);
		} catch (BizHandlerException e) {
			logger.error("## 商品相册图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增商品相册信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews11.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews11.getMsg());
		}
		return result;
	}

	/**
	 * 编辑商品相册信息
	 * @param thumbnailFile
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/editGoodsGallery")
	public BaseResult<Object> editGoodsGallery(@RequestParam("originalFile") MultipartFile originalFile, HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomGoodsGallery goodsGallery = getTbEcomGoodsGallery(req);
			result = goodsManageService.editGoodsGallery(goodsGallery, originalFile);
		} catch (BizHandlerException e) {
			logger.error("## 商品相册图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 编辑商品相册信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews12.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews12.getMsg());
		}
		return result;
	}

	/**
	 * 删除商品相册信息
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/deleteGoodsGallery")
	public BaseResult<Object> deleteGoodsGallery(HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			result = goodsManageService.deleteGoodsGallery(req);
		} catch (BizHandlerException e) {
			logger.error("## 删除商品相册图片异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除商品相册信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews13.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews13.getMsg());
		}
		return result;
	}

	/**
	 * 商品相册信息封装类方法
	 * @param req
	 * @return
	 */
	private TbEcomGoodsGallery getTbEcomGoodsGallery(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);

		String imgId = req.getParameter("img_id");
		String goodsId = req.getParameter("goodsId");
		String original = req.getParameter("original");
		String isDefaultList = req.getParameter("isDefaultList");
		String sort = req.getParameter("sort");
		String remarks = req.getParameter("remarks");

		TbEcomGoodsGallery goodsGallery = null;
		if (!StringUtil.isNullOrEmpty(imgId)) {
			goodsGallery = ecomGoodsGalleryService.getById(imgId);
			goodsGallery.setLockVersion(goodsGallery.getLockVersion() + 1);
		} else {
			goodsGallery = new TbEcomGoodsGallery();
			goodsGallery.setImgId(IdUtil.getNextId());
			goodsGallery.setCreateUser(user.getId());
			goodsGallery.setCreateTime(System.currentTimeMillis());
			goodsGallery.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			goodsGallery.setLockVersion(0);
		}
		goodsGallery.setUpdateUser(user.getId());
		goodsGallery.setUpdateTime(System.currentTimeMillis());
		goodsGallery.setGoodsId(goodsId);
		goodsGallery.setOriginal(original);
		goodsGallery.setIsDefault(isDefaultList);
		goodsGallery.setSort(Integer.valueOf(sort));
		goodsGallery.setRemarks(remarks);
		return goodsGallery;
	}

	/**
	 * 查询商品Sku列表（分页）
	 * @param req
	 * @param ecomGoodsProduct
	 * @return
	 */
	@RequestMapping(value = "/goodsInf/getGoodsProductList")
	public ModelAndView getGoodsProductList(HttpServletRequest req, TbEcomGoodsProduct ecomGoodsProduct) {
		ModelAndView mv = new ModelAndView("goodsManage/listGoodsProduct");

		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);

		String goodsId = req.getParameter("goodsId");

		PageInfo<TbEcomGoodsProduct> pageInfo = new PageInfo<>();
		TbEcomGoods goodsInf = new TbEcomGoods();
		List<TbEcomSpecification> goodsSpecList = new ArrayList<>();
		List<TbEcomSpecValues> goodsSpecValuesList = new ArrayList<>();

		try {
			ecomGoodsProduct.setGoodsId(goodsId);
			pageInfo = goodsManageService.getGoodsProductListPage(startNum, pageSize, ecomGoodsProduct);
			goodsInf = ecomGoodsService.getGoodsInfByGoodsId(goodsId);
			goodsSpecList = ecomSpecificationService.getGoodsSpecList(new TbEcomSpecification());
			goodsSpecValuesList = ecomSpecValuesService.getGoodsSpecValuesList(new TbEcomSpecValues());
		} catch (Exception e) {
			logger.error("## 查询商品{}Sku信息异常", goodsId, e);
		}
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("ecomGoodsProduct", ecomGoodsProduct);
		mv.addObject("goodsInf", goodsInf);
		mv.addObject("productEnableList", MarketEnableEnum.values());
		mv.addObject("isDefaultList", IsDefaultEnum.values());
		mv.addObject("goodsSpecList", goodsSpecList);
		mv.addObject("goodsSpecValuesList", goodsSpecValuesList);
		return mv;
	}

	/**
	 * 根据主键查询商品Sku信息
	 * @param req
	 * @param ecomGoodsProduct
	 * @return
	 */
	@PostMapping(value = "/goodsInf/getGoodsProduct")
	public TbEcomGoodsProduct getGoodsProduct(HttpServletRequest req, TbEcomGoodsProduct ecomGoodsProduct) {
		TbEcomGoodsProduct goodsProduct = new TbEcomGoodsProduct();
		try {
			goodsProduct = ecomGoodsProductService.getById(ecomGoodsProduct.getProductId());
			if (goodsProduct != null && !StringUtil.isEmpty(goodsProduct)) {
				TbEcomGoodsSpec ecomGoodsSpec = new TbEcomGoodsSpec();
				ecomGoodsSpec.setGoodsId(goodsProduct.getGoodsId());
				ecomGoodsSpec.setProductId(goodsProduct.getProductId());
				TbEcomGoodsSpec goodsSpec = ecomGoodsSpecService.getGoodsSpecByGoodsIdAndProductId(ecomGoodsSpec);
				if (goodsSpec != null) {
					goodsProduct.setSpecId(goodsSpec.getSpecId());
					goodsProduct.setSpecValueId(goodsSpec.getSpecValueId());
				}
				goodsProduct.setGoodsPrice(NumberUtils.RMBCentToYuan(goodsProduct.getGoodsPrice()));
				goodsProduct.setGoodsCost(NumberUtils.RMBCentToYuan(goodsProduct.getGoodsCost()));
				goodsProduct.setMktPrice(NumberUtils.RMBCentToYuan(goodsProduct.getMktPrice()));
				TbEcomGoods goods = ecomGoodsService.getById(goodsProduct.getGoodsId());
				if (!StringUtil.isNullOrEmpty(goods.getDefaultSkuCode()) && goods.getDefaultSkuCode().equals(goodsProduct.getSkuCode())) {
					goodsProduct.setIsDefault(IsDefaultEnum.IsDefaultEnum_0.getCode());
				} else {
					goodsProduct.setIsDefault(IsDefaultEnum.IsDefaultEnum_1.getCode());
				}
            }
		} catch (Exception e) {
			logger.error("## 查询主键为[{}]的商品Sku信息出错", goodsProduct.getProductId(), e);
		}
		return goodsProduct;
	}

	/**
	 * 新增商品Sku信息
	 * @param picUrlFile
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/addGoodsProduct")
	public BaseResult<Object> addGoodsProduct(@RequestParam("picUrlFile") MultipartFile picUrlFile, HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomGoodsProduct goodsProduct = TbEcomGoodsProduct(req);
			result = goodsManageService.addGoodsProduct(goodsProduct, picUrlFile);
		} catch (BizHandlerException e) {
			logger.error("## 商品Sku图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增商品Sku信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews15.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews15.getMsg());
		}
		return result;
	}

	/**
	 * 编辑商品Sku信息
	 * @param picUrlFile
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/editGoodsProduct")
	public BaseResult<Object> editGoodsProduct(@RequestParam("picUrlFile") MultipartFile picUrlFile, HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomGoodsProduct goodsProduct = TbEcomGoodsProduct(req);
			result = goodsManageService.editGoodsProduct(goodsProduct, picUrlFile);
		} catch (BizHandlerException e) {
			logger.error("## 商品Sku图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 编辑商品Sku信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews16.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews16.getMsg());
		}
		return result;
	}

	/**
	 * 删除商品Sku信息
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/deleteGoodsProduct")
	public BaseResult<Object> deleteGoodsProduct(HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			result = goodsManageService.deleteGoodsProduct(req);
		} catch (BizHandlerException e) {
			logger.error("## 删除商品Sku图片异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除商品Sku信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews17.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews17.getMsg());
		}
		return result;
	}

	/**
	 * 更新商品Sku上下架状态
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/updateGoodsProductEnable")
	public BaseResult<Object> updateGoodsProductEnable(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);

		String productId = req.getParameter("productId");
		String productEnable = req.getParameter("productEnable");

		try {
			TbEcomGoodsProduct goodsProduct = ecomGoodsProductService.getById(productId);
			goodsProduct.setProductEnable(Integer.valueOf(productEnable));
			goodsProduct.setUpdateUser(user.getId());
			goodsProduct.setUpdateTime(System.currentTimeMillis());
			goodsProduct.setLockVersion(goodsProduct.getLockVersion() + 1);

			if (!ecomGoodsProductService.updateById(goodsProduct)) {
				logger.error("## 更新商品Sku上下架出错,productId--->{}", productId);
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews10.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews10.getMsg());
			}
		} catch (Exception e) {
			logger.error("## 更新商品Sku上下架出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews10.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews10.getMsg());
		}
		return ResultsUtil.success();
	}

	/**
	 * 商品Sku信息封装类方法
	 * @param req
	 * @return
	 */
	private TbEcomGoodsProduct TbEcomGoodsProduct(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);

		String productId = req.getParameter("product_id");
		String goodsId = req.getParameter("goods_id");
		String spuCode = req.getParameter("spu_code");
		String ecomCode = req.getParameter("ecom_code");
		String accountSign = req.getParameter("account_sign");
		String isStore = req.getParameter("is_store");
		String skuCode = req.getParameter("sku_code");
		String enableStore = req.getParameter("enable_store");
		String goodsPrice = req.getParameter("goods_price");
		String goodsCost = req.getParameter("goods_cost");
		String mktPrice = req.getParameter("mkt_price");
		String pageTitle = req.getParameter("page_title");
		String metaDescription = req.getParameter("meta_description");
		String picUrl = req.getParameter("pic_url");
		String remarks = req.getParameter("remarks");
		String defaultSkuCode = req.getParameter("default_sku_code");
		String specId = req.getParameter("specId");
		String specValueId = req.getParameter("specValueId");

		TbEcomGoodsProduct goodsProduct = null;
		if (!StringUtil.isNullOrEmpty(productId)) {
			goodsProduct = ecomGoodsProductService.getById(productId);
			goodsProduct.setLockVersion(goodsProduct.getLockVersion() + 1);
		} else {
			goodsProduct = new TbEcomGoodsProduct();
			goodsProduct.setProductId(IdUtil.getNextId());
			goodsProduct.setCreateUser(user.getId());
			goodsProduct.setCreateTime(System.currentTimeMillis());
			goodsProduct.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			goodsProduct.setLockVersion(0);
		}
		goodsProduct.setUpdateUser(user.getId());
		goodsProduct.setUpdateTime(System.currentTimeMillis());
		goodsProduct.setGoodsId(goodsId);
		goodsProduct.setSpuCode(spuCode);
		goodsProduct.setEcomCode(ecomCode);
		goodsProduct.setAccountSign(accountSign);
		goodsProduct.setIsStore(Integer.valueOf(isStore));
		goodsProduct.setSkuCode(skuCode);
		goodsProduct.setEnableStore(Integer.valueOf(enableStore));
		goodsProduct.setGoodsPrice(NumberUtils.RMBYuanToCent(goodsPrice));
		goodsProduct.setGoodsCost(NumberUtils.RMBYuanToCent(goodsCost));
		goodsProduct.setMktPrice(NumberUtils.RMBYuanToCent(mktPrice));
		goodsProduct.setPageTitle(pageTitle);
		goodsProduct.setMetaDescription(metaDescription);
		goodsProduct.setPicUrl(picUrl);
		goodsProduct.setRemarks(remarks);
		goodsProduct.setIsDefault(defaultSkuCode);
		goodsProduct.setProductEnable(Integer.valueOf(MarketEnableEnum.MarketEnableEnum_1.getCode()));
		goodsProduct.setSpecId(specId);
		goodsProduct.setSpecValueId(specValueId);
		return goodsProduct;
	}

}
