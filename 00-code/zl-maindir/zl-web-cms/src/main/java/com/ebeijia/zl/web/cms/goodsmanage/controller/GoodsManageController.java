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
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
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
	private RetailChnlInfFacade retailChnlInfFacade;

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

	/**
	 * 商品信息Spu列表（分页）
	 * @param req
	 * @param ecomGoods
	 * @return
	 */
	@RequestMapping(value = "/goodsInf/getGoodsSpuList")
	public ModelAndView getGoodsSpuList(HttpServletRequest req, TbEcomGoods ecomGoods) {
		ModelAndView mv = new ModelAndView("goodsManage/listGoodsSpu");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<TbEcomGoods> pageInfo = new PageInfo<>();
		List<RetailChnlInf> retailChnlInfList = new ArrayList<>();
		List<TbEcomGoodsDetail> goodsDetailList = new ArrayList<>();
		try {
			pageInfo = goodsManageService.getGoodsInfListPage(startNum, pageSize, ecomGoods);
			retailChnlInfList = retailChnlInfFacade.getRetailChnlInfList(new RetailChnlInf());
			goodsDetailList = ecomGoodsDetailService.getGoodsDetailList(new TbEcomGoodsDetail());
		} catch (Exception e) {
			logger.error("## 查询商品Spu信息异常{}", e);
		}
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("ecomGoods", ecomGoods);
		mv.addObject("ecomCodeList", retailChnlInfList);
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
	 * 查询商品Spu下的所有Sku信息
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/getGoodsProductByGoodsId")
	public List<TbEcomGoodsProduct> getGoodsSpecValues(HttpServletRequest req) {
		List<TbEcomGoodsProduct> goodsProductList = new ArrayList<>();
		String goodsId = req.getParameter("goodsId");
		try {
			goodsProductList = ecomGoodsProductService.getProductlistByGoodsId(goodsId);
		} catch (Exception e) {
			logger.error("## 查询商品Spu{}下的所有Sku信息", goodsId, e);
		}
		return goodsProductList;
	}

	/**
	 * 新增商品信息
	 * @param goodsImgFile
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/addGoodsSpu")
	public BaseResult<Object> addGoodsSpu(@RequestParam("goodsImgFile") MultipartFile goodsImgFile, HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomGoods goods = getTbEcomGoodsInf(req);
			result = goodsManageService.addGoodsSpu(goods, goodsImgFile);
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
	@PostMapping(value = "/goodsInf/editGoodsSpu")
	public BaseResult<Object> editGoodsSpu(@RequestParam("goodsImgFile") MultipartFile goodsImgFile, HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomGoods goods = getTbEcomGoodsInf(req);
			result = goodsManageService.editGoodsSpu(goods, goodsImgFile);
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
	@PostMapping(value = "/goodsInf/deleteGoodsSpu")
	public BaseResult<Object> deleteGoodsSpu(HttpServletRequest req) {
		/*BaseResult<Object> result = new BaseResult<>();*/
		try {
			/*TbEcomGoods goods = getTbEcomGoodsInf(req);*/
			HttpSession session = req.getSession();
			User user = (User) session.getAttribute(Constants.SESSION_USER);

			String goodsId = req.getParameter("goods_id");
			TbEcomGoods goods = ecomGoodsService.getById(goodsId);
			goods.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
			goods.setUpdateUser(user.getId());
			goods.setUpdateTime(System.currentTimeMillis());
			goods.setLockVersion(goods.getLockVersion() + 1);

			if (!ecomGoodsService.updateById(goods)) {
				logger.error("# 删除商品{}Spu信息失败", goodsId);
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews09.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews09.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("## 删除商品信息图片异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除商品信息出错", e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews09.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews09.getMsg());
		}
		return ResultsUtil.success();
	}

	/**
	 * 更新商品上下架状态
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/goodsInf/updateGoodsSpuEnable")
	public BaseResult<Object> updateGoodsSpuEnable(HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			result = goodsManageService.updateGoodsSpuEnable(req);
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
		String defaultSkuCode = req.getParameter("default_sku_code");
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
		goods.setDefaultSkuCode(defaultSkuCode);
		/*goods.setMarketEnable(marketEnable);*/
		goods.setMarketEnable(MarketEnableEnum.MarketEnableEnum_1.getCode());
		goods.setBrief(brief);
		goods.setGoodsDetail(goodsDetail);
		goods.setHaveGroups(haveGroups);
		goods.setHaveParams(havaParams);
		goods.setHaveSpec(haveSpec);
		goods.setIsDisabled(isDisabled);
		goods.setPonumber(Integer.valueOf(ponumber));
		goods.setGoodsSord(Integer.valueOf(goodsSord));
		goods.setGoodsWeight(goodsWeight);
		goods.setGrade(Integer.valueOf(grade));
		goods.setIsHot(isHot);
		goods.setGoodsImg(goodsImg);
		goods.setRemarks(remarks);
		return goods;
	}

}
