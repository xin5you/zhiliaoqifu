package com.ebeijia.zl.web.cms.goodsmanage.service.impl;

import java.util.ArrayList;
import java.util.List;

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
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import com.ebeijia.zl.shop.dao.goods.domain.*;
import com.ebeijia.zl.shop.dao.goods.service.*;
import com.ebeijia.zl.web.cms.base.exception.BizHandlerException;
import com.ebeijia.zl.web.cms.base.service.ImageService;
import com.ebeijia.zl.web.cms.base.vo.FTPImageVo;
import com.ebeijia.zl.web.cms.goodsmanage.service.GoodsManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service("goodsManageService")
public class GoodsManageServiceImpl implements GoodsManageService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${FILE_UPLAOD_PATH}")
	private String FILE_UPLAOD_PATH;

	@Value("${FILE_UPLAOD_SEPARATOR}")
	private String FILE_UPLAOD_SEPARATOR;

	@Value("${IMG_SERVER}")
	private String IMG_SERVER;

	@Value("${FILE_NEW_PATH}")
	private String FILE_NEW_PATH;

	@Autowired
	private ImageService imageService;

	@Autowired
	private ITbEcomSpecificationService ecomSpecificationService;

	@Autowired
	private ITbEcomSpecValuesService ecomSpecValuesService;

	@Autowired
	private ITbEcomGoodsService ecomGoodsService;

	@Autowired
	private ITbEcomGoodsProductService ecomGoodsProductService;

	@Autowired
	private ITbEcomGoodsSpecService ecomGoodsSpecService;

	@Autowired
	private ITbEcomGoodsGalleryService ecomGoodsGalleryService;

	@Autowired
	private ITbEcomGoodsDetailService ecomGoodsDetailService;

	@Autowired
	private ITbEcomGoodsBillingService ecomGoodsBillingService;

	@Autowired
	private ProviderInfFacade providerInfFacade;

	@Override
	public PageInfo<TbEcomSpecification> getGodosSpecListPage(int startNum, int pageSize, TbEcomSpecification entity) {
		List<TbEcomSpecification> goodsSpecList = new ArrayList<TbEcomSpecification>();

		PageHelper.startPage(startNum, pageSize);
		goodsSpecList = ecomSpecificationService.getGoodsSpecList(entity);
		PageInfo<TbEcomSpecification> page = new PageInfo<TbEcomSpecification>(goodsSpecList);
		return page;
	}

	@Override
	public BaseResult<Object> addGoodsSpec(TbEcomSpecification entity, MultipartFile specImgFile) {
		// 判断是否有文件提交
		if (specImgFile == null || specImgFile.isEmpty()) {
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews03.getCode(), ExceptionEnum.ImageNews.ImageNews03.getMsg());
		}
		FTPImageVo imgVo = new FTPImageVo();
		imgVo.setImgId(entity.getSpecId());
		imgVo.setService(IMG_SERVER);
		imgVo.setNewPath(FILE_NEW_PATH);
		imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
		imgVo.setUploadPath(FILE_UPLAOD_PATH);
		imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_01.getValue());
		String imageUrl = "";
		try {
			imageUrl = imageService.uploadImangeName(imgVo, specImgFile);
			if (StringUtil.isNullOrEmpty(imageUrl)) {
				logger.error("## 商品规格图片上传返回路径为空");
				return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews01.getCode(), ExceptionEnum.ImageNews.ImageNews01.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("## 商品规格图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 商品规格图片上传异常");
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
		}
		entity.setSpecImg(imageUrl);
		if (!ecomSpecificationService.save(entity)) {
			logger.error("## 新增商品规格信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews01.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews01.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> editGoodsSpec(TbEcomSpecification entity, MultipartFile specImgFile) {
		if (StringUtil.isNullOrEmpty(entity.getSpecImg())) {
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews03.getCode(), ExceptionEnum.ImageNews.ImageNews03.getMsg());
		}
		if (specImgFile != null && !specImgFile.isEmpty()) {
			FTPImageVo imgVo = new FTPImageVo();
			imgVo.setImgId(entity.getSpecId());
			imgVo.setService(IMG_SERVER);
			imgVo.setNewPath(FILE_NEW_PATH);
			imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
			imgVo.setUploadPath(FILE_UPLAOD_PATH);
			imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_01.getValue());
			try {
				if (specImgFile != null && !specImgFile.isEmpty()) {
					String imageUrl = imageService.uploadImangeName(imgVo, specImgFile);
					if (StringUtil.isNullOrEmpty(imageUrl)) {
						logger.error("## 商品规格图片上传返回路径为空");
						return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews01.getCode(), ExceptionEnum.ImageNews.ImageNews01.getMsg());
					}
					entity.setSpecImg(imageUrl);
				}
			} catch (BizHandlerException e) {
				logger.error("## 商品规格图片上传异常", e.getMessage());
				return ResultsUtil.error(e.getCode(), e.getMessage());
			} catch (Exception e) {
				logger.error("## 商品规格图片上传异常");
				return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
			}
		}

		if (!ecomSpecificationService.updateById(entity)) {
			logger.error("## 编辑商品规格信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews02.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews02.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> deleteGoodsSpec(TbEcomSpecification entity) {
		/*FTPImageVo imgVo = new FTPImageVo();
		imgVo.setImgId(entity.getSpecId());
		imgVo.setService(IMG_SERVER);
		imgVo.setNewPath(FILE_NEW_PATH);
		imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
		imgVo.setUploadPath(FILE_UPLAOD_PATH);
		imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_01.getValue());
		try {
			imageService.deleteImange(imgVo, entity.getSpecId());
		} catch (BizHandlerException e) {
			logger.error("## 删除商品规格图片异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除商品规格图片异常");
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews04.getCode(), ExceptionEnum.ImageNews.ImageNews04.getMsg());
		}
		if (!ecomSpecificationService.removeById(entity.getSpecId())) {
			logger.error("## 删除商品规格信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews03.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews03.getMsg());
		}*/
		if (!ecomSpecificationService.updateById(entity)) {
			logger.error("## 删除商品规格信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews03.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews03.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public PageInfo<TbEcomSpecValues> getGodosSpecValuesListPage(int startNum, int pageSize, TbEcomSpecValues entity) {
		PageHelper.startPage(startNum, pageSize);
		List<TbEcomSpecValues> goodsSpecValuesList = ecomSpecValuesService.getGoodsSpecValuesList(entity);
		if (goodsSpecValuesList != null && goodsSpecValuesList.size() >= 1) {
			for (TbEcomSpecValues g : goodsSpecValuesList) {
				g.setSpecType(GoodsSpecTypeEnum.findByBId(g.getSpecType()).getValue());
			}
		}
		PageInfo<TbEcomSpecValues> page = new PageInfo<TbEcomSpecValues>(goodsSpecValuesList);
		return page;
	}

	@Override
	public BaseResult<Object> addGoodsSpecValues(TbEcomSpecValues entity, MultipartFile specImageFile) {
		if (GoodsSpecTypeEnum.GoodsSpecTypeEnum_1.getCode().equals(entity.getSpecType())) {
			if (specImageFile == null || specImageFile.isEmpty()) {
				return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews03.getCode(), ExceptionEnum.ImageNews.ImageNews03.getMsg());
			}
			FTPImageVo imgVo = new FTPImageVo();
			imgVo.setImgId(entity.getSpecValueId());
			imgVo.setService(IMG_SERVER);
			imgVo.setNewPath(FILE_NEW_PATH);
			imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
			imgVo.setUploadPath(FILE_UPLAOD_PATH);
			imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_02.getValue());
			try {
				String imageUrl = imageService.uploadImangeName(imgVo, specImageFile);
				if (StringUtil.isNullOrEmpty(imageUrl)) {
					logger.error("## 商品规格值图片上传返回路径为空");
					return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews01.getCode(), ExceptionEnum.ImageNews.ImageNews01.getMsg());
				}
				entity.setSpecImage(imageUrl);
			} catch (BizHandlerException e) {
				logger.error("## 商品规格值图片上传异常", e.getMessage());
				return ResultsUtil.error(e.getCode(), e.getMessage());
			} catch (Exception e) {
				logger.error("## 商品规格值图片上传异常");
				return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
			}
		}

		if (!ecomSpecValuesService.save(entity)) {
			logger.error("## 新增商品规格值信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews04.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews04.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> editGoodsSpecValues(TbEcomSpecValues entity, MultipartFile specImageFile) {
		if (GoodsSpecTypeEnum.GoodsSpecTypeEnum_1.getCode().equals(entity.getSpecType())) {
			if (specImageFile != null && !specImageFile.isEmpty()) {
				FTPImageVo imgVo = new FTPImageVo();
				imgVo.setImgId(entity.getSpecValueId());
				imgVo.setService(IMG_SERVER);
				imgVo.setNewPath(FILE_NEW_PATH);
				imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
				imgVo.setUploadPath(FILE_UPLAOD_PATH);
				imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_02.getValue());
				try {
					if (specImageFile != null && !specImageFile.isEmpty()) {
						String imageUrl = imageService.uploadImangeName(imgVo, specImageFile);
						if (StringUtil.isNullOrEmpty(imageUrl)) {
							logger.error("## 商品规格值图片上传返回路径为空");
							return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews01.getCode(), ExceptionEnum.ImageNews.ImageNews01.getMsg());
						}
						entity.setSpecImage(imageUrl);
					}
				} catch (BizHandlerException e) {
					logger.error("## 商品规格值图片上传异常", e.getMessage());
					return ResultsUtil.error(e.getCode(), e.getMessage());
				} catch (Exception e) {
					logger.error("## 商品规格值图片上传异常");
					return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
				}
			}
		}
		if (!ecomSpecValuesService.updateById(entity)) {
			logger.error("## 编辑商品规格值信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews05.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews05.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> deleteGoodsSpecValues(TbEcomSpecValues entity) {
		/*FTPImageVo imgVo = new FTPImageVo();
		imgVo.setImgId(entity.getSpecValueId());
		imgVo.setService(IMG_SERVER);
		imgVo.setNewPath(FILE_NEW_PATH);
		imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
		imgVo.setUploadPath(FILE_UPLAOD_PATH);
		imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_02.getValue());
		try {
			if (imageService.isFileExsits(imgVo, entity.getSpecValueId())) {
				imageService.deleteImange(imgVo, entity.getSpecValueId());
			}
		} catch (BizHandlerException e) {
			logger.error("## 删除商品规格值图片异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除商品规格值图片异常");
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews04.getCode(), ExceptionEnum.ImageNews.ImageNews04.getMsg());
		}
		if (!ecomSpecificationService.removeById(entity.getSpecId())) {
			logger.error("## 删除商品规格值信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews06.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews06.getMsg());
		}*/

		if (!ecomSpecValuesService.updateById(entity)) {
			logger.error("## 删除商品规格值信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews06.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews06.getMsg());
		}
		return ResultsUtil.success();
	}

	/**
	 * 查询商品Spu下的所有Sku信息
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	@Override
	public PageInfo<TbEcomGoods> getGoodsInfListPage(int startNum, int pageSize, TbEcomGoods entity) {
		PageHelper.startPage(startNum, pageSize);
		List<TbEcomGoods> goodsInfList = ecomGoodsService.getGoodsInfList(entity);
		try {
			if (goodsInfList != null && goodsInfList.size() >= 1) {
				for (TbEcomGoods goods : goodsInfList) {
					ProviderInf provider = providerInfFacade.getProviderInfByLawCode(goods.getEcomCode());
					goods.setEcomCode(provider.getProviderName());
					goods.setGoodsType(GoodsTypeEnum.findByBId(goods.getGoodsType()).getName());
					goods.setMarketEnable(MarketEnableEnum.findByBId(goods.getMarketEnable()).getName());
					goods.setHaveGroups(HaveGroupsEnum.findByBId(goods.getHaveGroups()).getName());
					goods.setIsDisabled(IsDefaultEnum.findByBId(goods.getIsDisabled()).getName());
					goods.setIsHot(GoodsIsHotEnum.findByBId(goods.getIsHot()).getName());
				}
			}
			} catch (Exception e) {
				logger.error("## 查询商品信息列表异常", e);
			}
		PageInfo<TbEcomGoods> page = new PageInfo<TbEcomGoods>(goodsInfList);
		return page;
	}

	@Override
	public BaseResult<Object> addGoodsInf(TbEcomGoods entity, MultipartFile goodsImgFile) {
		if (goodsImgFile == null || goodsImgFile.isEmpty()) {
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews03.getCode(), ExceptionEnum.ImageNews.ImageNews03.getMsg());
		}
		FTPImageVo imgVo = new FTPImageVo();
		imgVo.setImgId(entity.getGoodsId());
		imgVo.setService(IMG_SERVER);
		imgVo.setNewPath(FILE_NEW_PATH);
		imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
		imgVo.setUploadPath(FILE_UPLAOD_PATH);
		imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_04.getValue());
		try {
			String imageUrl = imageService.uploadImangeName(imgVo, goodsImgFile);
			if (StringUtil.isNullOrEmpty(imageUrl)) {
				logger.error("## 商品信息图片上传返回路径为空");
				return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews01.getCode(), ExceptionEnum.ImageNews.ImageNews01.getMsg());
			}
			entity.setGoodsImg(imageUrl);
		} catch (BizHandlerException e) {
			logger.error("## 商品信息图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 商品信息图片上传异常");
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
		}

		TbEcomGoodsBilling goodsBilling = new TbEcomGoodsBilling();
		goodsBilling.setGoodsId(entity.getGoodsId());
		goodsBilling.setBId(entity.getBId());

		if (!ecomGoodsService.save(entity)) {
			logger.error("## 新增商品信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews07.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews07.getMsg());
		}

		if (!ecomGoodsBillingService.save(goodsBilling)) {
			logger.error("## 新增商品信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews07.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews07.getMsg());
		}

		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> editGoodsInf(TbEcomGoods entity, MultipartFile goodsImgFile) {
		if (goodsImgFile != null && !goodsImgFile.isEmpty()) {
			FTPImageVo imgVo = new FTPImageVo();
			imgVo.setImgId(entity.getGoodsId());
			imgVo.setService(IMG_SERVER);
			imgVo.setNewPath(FILE_NEW_PATH);
			imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
			imgVo.setUploadPath(FILE_UPLAOD_PATH);
			imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_04.getValue());
			try {
				if (goodsImgFile != null && !goodsImgFile.isEmpty()) {
					String imageUrl = imageService.uploadImangeName(imgVo, goodsImgFile);
					if (StringUtil.isNullOrEmpty(imageUrl)) {
						logger.error("## 商品信息图片上传返回路径为空");
						return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews01.getCode(), ExceptionEnum.ImageNews.ImageNews01.getMsg());
					}
					entity.setGoodsImg(imageUrl);
				}
			} catch (BizHandlerException e) {
				logger.error("## 商品信息图片上传异常", e.getMessage());
				return ResultsUtil.error(e.getCode(), e.getMessage());
			} catch (Exception e) {
				logger.error("## 商品信息图片上传异常");
				return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
			}
		}

		TbEcomGoodsBilling goodsBilling = ecomGoodsBillingService.getGoodsBillingByGoodsId(entity.getGoodsId());
		goodsBilling.setBId(entity.getBId());

		if (!ecomGoodsService.updateById(entity)) {
			logger.error("## 编辑商品信息信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews08.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews08.getMsg());
		}

		if (!ecomGoodsBillingService.updateById(goodsBilling)) {
			logger.error("## 新增商品信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews08.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews08.getMsg());
		}

		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> deleteGoodsInf(HttpServletRequest req) {
		String goodsId = req.getParameter("goodsId");
		try {
			HttpSession session = req.getSession();
			User user = (User) session.getAttribute(Constants.SESSION_USER);

			TbEcomGoods goods = ecomGoodsService.getById(goodsId);
			goods.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
			goods.setUpdateUser(user.getId());
			goods.setUpdateTime(System.currentTimeMillis());
			goods.setLockVersion(goods.getLockVersion() + 1);

			if (!ecomGoodsService.updateById(goods)) {
				logger.error("# 删除商品{}Spu信息失败", goodsId);
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews09.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews09.getMsg());
			}

			List<TbEcomGoodsProduct> goodsProductList = ecomGoodsProductService.getGoodsProductListByGoodsId(goodsId);
			if (goodsProductList != null && goodsProductList.size() >= 1) {
				for (TbEcomGoodsProduct product : goodsProductList) {
					product.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
					product.setUpdateUser(user.getId());
					product.setUpdateTime(System.currentTimeMillis());
					product.setLockVersion(goods.getLockVersion() + 1);
				}
				if (!ecomGoodsProductService.updateBatchById(goodsProductList)) {
					logger.error("# 删除商品{}Spu下的所有Sku信息失败", goodsId);
					return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews09.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews09.getMsg());
				}
			}

		} catch (Exception e) {
			logger.error("# 删除商品{}Spu信息异常", goodsId, e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews09.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews09.getMsg());
		}
		/*FTPImageVo imgVo = new FTPImageVo();
		imgVo.setImgId(entity.getGoodsId());
		imgVo.setService(IMG_SERVER);
		imgVo.setNewPath(FILE_NEW_PATH);
		imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
		imgVo.setUploadPath(FILE_UPLAOD_PATH);
		imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_02.getValue());
		try {
			if (imageService.isFileExsits(imgVo, entity.getGoodsId())) {
				imageService.deleteImange(imgVo, entity.getGoodsId());
			}
		} catch (BizHandlerException e) {
			logger.error("## 删除商品信息图片异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除商品信息图片异常");
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews04.getCode(), ExceptionEnum.ImageNews.ImageNews04.getMsg());
		}
		if (!ecomGoodsService.removeById(entity.getGoodsId())) {
			logger.error("## 删除商品信息信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews09.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews09.getMsg());
		}*/
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> updateGoodsInfEnable(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);

		String goodsId = req.getParameter("goodsId");
		String marketEnable = req.getParameter("marketEnable");

		//查询商品Spu信息
		TbEcomGoods goods = ecomGoodsService.getById(goodsId);
		goods.setMarketEnable(marketEnable);
		goods.setUpdateTime(System.currentTimeMillis());
		goods.setUpdateUser(user.getId());
		goods.setLockVersion(goods.getLockVersion() + 1);

		//查询商品Spu信息下的所有Sku
		List<TbEcomGoodsProduct> ecomGoodsProductList = ecomGoodsProductService.getGoodsProductListByGoodsId(goodsId);
		for (TbEcomGoodsProduct product : ecomGoodsProductList) {
			product.setProductEnable(Integer.valueOf(marketEnable));
			product.setUpdateTime(System.currentTimeMillis());
			product.setUpdateUser(user.getId());
			product.setLockVersion(product.getLockVersion() + 1);
		}

		//更新商品Spu上下架状态
		if (!ecomGoodsService.updateById(goods)) {
			logger.error("## 更新商品Spu{}信息上下架失败", goodsId);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews10.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews10.getMsg());
		}

		//更新商品Spu下的所有Sku上下架状态
		if (ecomGoodsProductList != null && ecomGoodsProductList.size() >= 1) {
			if (ecomGoodsProductService.updateBatchById(ecomGoodsProductList)) {
				logger.error("## 更新商品Spu{}下的所有Sku信息上下架失败", goodsId);
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews10.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews10.getMsg());
			}
		}

		return ResultsUtil.success();
	}

	@Override
	public PageInfo<TbEcomGoodsGallery> getGoodsGalleryListPage(int startNum, int pageSize, TbEcomGoodsGallery entity) {
		PageHelper.startPage(startNum, pageSize);
		List<TbEcomGoodsGallery> goodsGalleryList = ecomGoodsGalleryService.getGoodsGalleryList(entity);
		if (goodsGalleryList != null && goodsGalleryList.size() >= 1) {
			for (TbEcomGoodsGallery g : goodsGalleryList) {
				g.setIsDefault(IsDefaultEnum.findByBId(g.getIsDefault()).getName());
			}
		}
		PageInfo<TbEcomGoodsGallery> page = new PageInfo<TbEcomGoodsGallery>(goodsGalleryList);
		return page;
	}

	@Override
	public BaseResult<Object> addGoodsGallery(TbEcomGoodsGallery entity, MultipartFile originalFile) {
		TbEcomGoodsGallery gallery = ecomGoodsGalleryService.getGoodsGalleryBySort(entity);
		if (gallery != null) {
			logger.error("## 新增商品相册信息失败,排序号{}已存在");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews14.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews14.getMsg());
		}

		if (entity.getIsDefault().equals(IsDefaultEnum.IsDefaultEnum_0.getCode())) {
			TbEcomGoodsGallery goodsGallery = ecomGoodsGalleryService.getGoodsGalleryByIsDefault(entity.getIsDefault());
			if (goodsGallery != null) {
				goodsGallery.setIsDefault(IsDefaultEnum.IsDefaultEnum_1.getCode());
				if (!ecomGoodsGalleryService.updateById(goodsGallery)) {
					logger.error("## 新增商品相册信息失败");
					return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews11.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews11.getMsg());
				}
			}
		}

		// 判断是否有文件提交
		if (originalFile == null || originalFile.isEmpty()) {
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews03.getCode(), ExceptionEnum.ImageNews.ImageNews03.getMsg());
		}
		FTPImageVo imgVo = new FTPImageVo();
		imgVo.setImgId(entity.getImgId());
		imgVo.setService(IMG_SERVER);
		imgVo.setNewPath(FILE_NEW_PATH);
		imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
		imgVo.setUploadPath(FILE_UPLAOD_PATH);
		imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_05.getValue());
		String imageUrl = "";
		try {
			imageUrl = imageService.uploadImangeName(imgVo, originalFile);
			if (StringUtil.isNullOrEmpty(imageUrl)) {
				logger.error("## 商品相册图片上传返回路径为空");
				return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews01.getCode(), ExceptionEnum.ImageNews.ImageNews01.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("## 商品相册图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 商品相册图片上传异常");
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
		}
		entity.setOriginal(imageUrl);

		if (!ecomGoodsGalleryService.save(entity)) {
			logger.error("## 新增商品相册信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews11.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews11.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> editGoodsGallery(TbEcomGoodsGallery entity, MultipartFile originalFile) {
		TbEcomGoodsGallery gallery = ecomGoodsGalleryService.getGoodsGalleryBySort(entity);
		if (gallery != null && !gallery.getImgId().equals(entity.getImgId())) {
			logger.error("## 编辑商品相册信息失败,排序号{}已存在");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews14.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews14.getMsg());
		}

		if (entity.getIsDefault().equals(IsDefaultEnum.IsDefaultEnum_0.getCode())) {
			TbEcomGoodsGallery goodsGallery = ecomGoodsGalleryService.getGoodsGalleryByIsDefault(entity.getIsDefault());
			if (goodsGallery != null) {
				goodsGallery.setIsDefault(IsDefaultEnum.IsDefaultEnum_1.getCode());
				if (!ecomGoodsGalleryService.updateById(goodsGallery)) {
					logger.error("## 编辑商品相册信息失败");
					return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews12.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews12.getMsg());
				}
			}
		}

		if (StringUtil.isNullOrEmpty(entity.getOriginal())) {
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews03.getCode(), ExceptionEnum.ImageNews.ImageNews03.getMsg());
		}
		if (originalFile != null && !originalFile.isEmpty()) {
			FTPImageVo imgVo = new FTPImageVo();
			imgVo.setImgId(entity.getImgId());
			imgVo.setService(IMG_SERVER);
			imgVo.setNewPath(FILE_NEW_PATH);
			imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
			imgVo.setUploadPath(FILE_UPLAOD_PATH);
			imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_05.getValue());
			try {
				if (originalFile != null && !originalFile.isEmpty()) {
					String imageUrl = imageService.uploadImangeName(imgVo, originalFile);
					if (StringUtil.isNullOrEmpty(imageUrl)) {
						logger.error("## 商品相册信息图片上传返回路径为空");
						return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews01.getCode(), ExceptionEnum.ImageNews.ImageNews01.getMsg());
					}
					entity.setOriginal(imageUrl);
				}
			} catch (BizHandlerException e) {
				logger.error("## 商品相册信息图片上传异常", e.getMessage());
				return ResultsUtil.error(e.getCode(), e.getMessage());
			} catch (Exception e) {
				logger.error("## 商品相册信息图片上传异常");
				return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
			}
		}

		if (!ecomGoodsGalleryService.updateById(entity)) {
			logger.error("## 编辑商品相册信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews12.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews12.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> deleteGoodsGallery(HttpServletRequest req) {
		String imgId = req.getParameter("imgId");
		try {
			HttpSession session = req.getSession();
			User user = (User) session.getAttribute(Constants.SESSION_USER);

			TbEcomGoodsGallery goodsGallery = ecomGoodsGalleryService.getById(imgId);
			goodsGallery.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
			goodsGallery.setUpdateUser(user.getId());
			goodsGallery.setUpdateTime(System.currentTimeMillis());
			goodsGallery.setLockVersion(goodsGallery.getLockVersion() + 1);

			if (!ecomGoodsGalleryService.updateById(goodsGallery)) {
				logger.error("# 删除商品相册{}信息失败", imgId);
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews13.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews13.getMsg());
			}

		} catch (Exception e) {
			logger.error("# 删除商品相册{}信息异常", imgId, e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews13.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews13.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public PageInfo<TbEcomGoodsProduct> getGoodsProductListPage(int startNum, int pageSize, TbEcomGoodsProduct entity) {
		PageHelper.startPage(startNum, pageSize);
		List<TbEcomGoodsProduct> goodsProductList = ecomGoodsProductService.getGoodsProductList(entity);
		try {
			if (goodsProductList != null && goodsProductList.size() >= 1) {
				for (TbEcomGoodsProduct g : goodsProductList) {
					ProviderInf provider = providerInfFacade.getProviderInfByLawCode(g.getEcomCode());
					g.setEcomCode(provider.getProviderName());
					g.setProductEnableName(MarketEnableEnum.findByBId(Integer.toString(g.getProductEnable())).getName());
					g.setGoodsPrice(NumberUtils.RMBCentToYuan(g.getGoodsPrice()));
					g.setGoodsCost(NumberUtils.RMBCentToYuan(g.getGoodsCost()));
					g.setMktPrice(NumberUtils.RMBCentToYuan(g.getMktPrice()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		PageInfo<TbEcomGoodsProduct> page = new PageInfo<TbEcomGoodsProduct>(goodsProductList);
		return page;
	}

	@Override
	public BaseResult<Object> addGoodsProduct(TbEcomGoodsProduct entity, MultipartFile picUrlFile) {
		TbEcomGoodsProduct product = ecomGoodsProductService.getGoodsProductBySkuCode(entity.getSkuCode());
		if (product != null) {
			logger.error("## 新增商品Sku信息失败,skuCode{}已存在");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews18.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews18.getMsg());
		}

		// 判断是否有文件提交
		if (picUrlFile == null || picUrlFile.isEmpty()) {
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews03.getCode(), ExceptionEnum.ImageNews.ImageNews03.getMsg());
		}
		FTPImageVo imgVo = new FTPImageVo();
		imgVo.setImgId(entity.getProductId());
		imgVo.setService(IMG_SERVER);
		imgVo.setNewPath(FILE_NEW_PATH);
		imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
		imgVo.setUploadPath(FILE_UPLAOD_PATH);
		imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_06.getValue());
		String imageUrl = "";
		try {
			imageUrl = imageService.uploadImangeName(imgVo, picUrlFile);
			if (StringUtil.isNullOrEmpty(imageUrl)) {
				logger.error("## 商品Sku图片上传返回路径为空");
				return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews01.getCode(), ExceptionEnum.ImageNews.ImageNews01.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("## 商品Sku图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 商品Sku图片上传异常");
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
		}
		entity.setPicUrl(imageUrl);

		TbEcomGoods goods = ecomGoodsService.getById(entity.getGoodsId());
		if (!StringUtil.isNullOrEmpty(goods.getDefaultSkuCode())) {
			if (goods.getDefaultSkuCode().equals(entity.getSkuCode()) && IsDefaultEnum.IsDefaultEnum_1.getCode().equals(entity.getIsDefault())) {
				logger.error("## 新增商品Sku信息失败，必须要有一个是默认的Sku");
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews19.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews19.getMsg());
			}
		}
		if (StringUtil.isNullOrEmpty(goods.getDefaultSkuCode())) {
			if (IsDefaultEnum.IsDefaultEnum_1.getCode().equals(entity.getIsDefault())) {
				logger.error("## 新增商品Sku信息失败，必须要有一个是默认的Sku");
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews19.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews19.getMsg());
			}
		}

		if (IsDefaultEnum.IsDefaultEnum_0.getCode().equals(entity.getIsDefault())) {
			goods.setDefaultSkuCode(entity.getSkuCode());
			goods.setUpdateTime(System.currentTimeMillis());
			goods.setUpdateUser(entity.getUpdateUser());
			goods.setLockVersion(goods.getLockVersion() + 1);
			if (!ecomGoodsService.updateById(goods)) {
				logger.error("## 新增商品Sku信息失败,设置默认Sku出错");
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews15.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews15.getMsg());
			}
		}

		if (!ecomGoodsProductService.save(entity)) {
			logger.error("## 新增商品Sku信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews15.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews15.getMsg());
		}

		TbEcomGoodsSpec goodsSpec = new TbEcomGoodsSpec();
		goodsSpec.setId(IdUtil.getNextId());
		goodsSpec.setSpecId(entity.getSpecId());
		goodsSpec.setSpecValueId(entity.getSpecValueId());
		goodsSpec.setGoodsId(entity.getGoodsId());
		goodsSpec.setProductId(entity.getProductId());
		goodsSpec.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		goodsSpec.setCreateUser(entity.getUpdateUser());
		goodsSpec.setCreateTime(System.currentTimeMillis());
		goodsSpec.setUpdateTime(System.currentTimeMillis());
		goodsSpec.setUpdateUser(entity.getUpdateUser());
		goodsSpec.setLockVersion(0);
		if (!ecomGoodsSpecService.save(goodsSpec)) {
			logger.error("## 新增商品Sku信息失败,商品规格关联表新增失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews15.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews15.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> editGoodsProduct(TbEcomGoodsProduct entity, MultipartFile picUrlFile) {
		TbEcomGoodsProduct product = ecomGoodsProductService.getGoodsProductBySkuCode(entity.getSkuCode());
		if (product != null && !product.getProductId().equals(entity.getProductId())) {
			logger.error("## 编辑商品Sku信息失败,skuCode{}已存在");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews18.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews18.getMsg());
		}

		TbEcomGoodsSpec ecomGoodsSpec = new TbEcomGoodsSpec();
		ecomGoodsSpec.setGoodsId(entity.getGoodsId());
		ecomGoodsSpec.setProductId(entity.getProductId());
		TbEcomGoodsSpec goodsSpec = ecomGoodsSpecService.getGoodsSpecByGoodsIdAndProductId(ecomGoodsSpec);
		if (goodsSpec == null) {
			logger.error("## 编辑商品Sku信息失败,查询商品规格关联表信息为空");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews16.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews16.getMsg());
		}

		if (StringUtil.isNullOrEmpty(entity.getPicUrl())) {
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews03.getCode(), ExceptionEnum.ImageNews.ImageNews03.getMsg());
		}
		if (picUrlFile != null && !picUrlFile.isEmpty()) {
			FTPImageVo imgVo = new FTPImageVo();
			imgVo.setImgId(entity.getProductId());
			imgVo.setService(IMG_SERVER);
			imgVo.setNewPath(FILE_NEW_PATH);
			imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
			imgVo.setUploadPath(FILE_UPLAOD_PATH);
			imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_06.getValue());
			try {
				if (picUrlFile != null && !picUrlFile.isEmpty()) {
					String imageUrl = imageService.uploadImangeName(imgVo, picUrlFile);
					if (StringUtil.isNullOrEmpty(imageUrl)) {
						logger.error("## 商品Sku信息图片上传返回路径为空");
						return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews01.getCode(), ExceptionEnum.ImageNews.ImageNews01.getMsg());
					}
					entity.setPicUrl(imageUrl);
				}
			} catch (BizHandlerException e) {
				logger.error("## 商品Sku信息图片上传异常", e.getMessage());
				return ResultsUtil.error(e.getCode(), e.getMessage());
			} catch (Exception e) {
				logger.error("## 商品Sku信息图片上传异常");
				return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
			}
		}

		TbEcomGoods goods = ecomGoodsService.getById(entity.getGoodsId());
		if (!StringUtil.isNullOrEmpty(goods.getDefaultSkuCode())) {
			if (goods.getDefaultSkuCode().equals(entity.getSkuCode()) && IsDefaultEnum.IsDefaultEnum_1.getCode().equals(entity.getIsDefault())) {
				logger.error("## 编辑商品Sku信息失败，必须要有一个是默认的Sku");
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews19.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews19.getMsg());
			}
		}
		if (StringUtil.isNullOrEmpty(goods.getDefaultSkuCode())) {
			if (IsDefaultEnum.IsDefaultEnum_1.getCode().equals(entity.getIsDefault())) {
				logger.error("## 编辑商品Sku信息失败，必须要有一个是默认的Sku");
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews19.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews19.getMsg());
			}
		}

		if (IsDefaultEnum.IsDefaultEnum_0.getCode().equals(entity.getIsDefault())) {
			goods.setDefaultSkuCode(entity.getSkuCode());
			goods.setUpdateTime(System.currentTimeMillis());
			goods.setUpdateUser(entity.getUpdateUser());
			goods.setLockVersion(goods.getLockVersion() + 1);
			if (!ecomGoodsService.updateById(goods)) {
				logger.error("## 编辑商品Sku信息失败,设置默认Sku出错");
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews16.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews16.getMsg());
			}
		}

		if (!ecomGoodsProductService.updateById(entity)) {
			logger.error("## 编辑商品Sku信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews16.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews16.getMsg());
		}

		goodsSpec.setSpecValueId(entity.getSpecValueId());
		goodsSpec.setSpecId(entity.getSpecId());
		goodsSpec.setUpdateUser(entity.getUpdateUser());
		goodsSpec.setUpdateTime(System.currentTimeMillis());
		goodsSpec.setLockVersion(goodsSpec.getLockVersion() + 1);
		if (!ecomGoodsSpecService.updateById(goodsSpec)) {
			logger.error("## 编辑商品Sku信息失败,更新商品关联表信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews16.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews16.getMsg());
		}

		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> deleteGoodsProduct(HttpServletRequest req) {
		String productId = req.getParameter("productId");
		try {
			HttpSession session = req.getSession();
			User user = (User) session.getAttribute(Constants.SESSION_USER);

			TbEcomGoodsProduct product = ecomGoodsProductService.getById(productId);
			product.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
			product.setUpdateUser(user.getId());
			product.setUpdateTime(System.currentTimeMillis());
			product.setLockVersion(product.getLockVersion() + 1);

			TbEcomGoods goods = ecomGoodsService.getById(product.getGoodsId());
			if (goods.getDefaultSkuCode().equals(product.getSkuCode())) {
				logger.error("# 删除商品Sku信息失败,productId--->{},该Sku是默认的Sku标识", productId);
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews20.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews20.getMsg());
			}

			TbEcomGoodsSpec ecomGoodsSpec = new TbEcomGoodsSpec();
			ecomGoodsSpec.setGoodsId(product.getGoodsId());
			ecomGoodsSpec.setProductId(product.getProductId());
			TbEcomGoodsSpec goodsSpec = ecomGoodsSpecService.getGoodsSpecByGoodsIdAndProductId(ecomGoodsSpec);
			if (goodsSpec == null) {
				logger.error("## 删除商品Sku信息失败,查询商品规格关联表信息为空");
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews17.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews17.getMsg());
			}

			if (!ecomGoodsProductService.updateById(product)) {
				logger.error("# 删除商品Sku信息失败,productId--->{}", productId);
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews17.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews17.getMsg());
			}

			goodsSpec.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
			goodsSpec.setUpdateUser(product.getUpdateUser());
			goodsSpec.setUpdateTime(System.currentTimeMillis());
			goodsSpec.setLockVersion(goodsSpec.getLockVersion() + 1);
			if (!ecomGoodsSpecService.updateById(goodsSpec)) {
				logger.error("## 删除商品Sku信息失败,删除商品关联表信息失败");
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews17.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews17.getMsg());
			}

		} catch (Exception e) {
			logger.error("# 删除商品Sku信息异常,productId--->{}", productId, e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews17.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews17.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public PageInfo<TbEcomGoodsDetail> getGoodsDetailListPage(int startNum, int pageSize, TbEcomGoodsDetail entity) {
		PageHelper.startPage(startNum, pageSize);
		List<TbEcomGoodsDetail> goodsDetailList = ecomGoodsDetailService.getGoodsDetailList(entity);
		PageInfo<TbEcomGoodsDetail> page = new PageInfo<TbEcomGoodsDetail>(goodsDetailList);
		return page;
	}

	@Override
	public BaseResult<Object> addGoodsDetail(TbEcomGoodsDetail goodsDetail) {
		if (!ecomGoodsDetailService.save(goodsDetail)) {
			logger.error("## 新增商品详情信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews21.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews21.getMsg());
		}
		TbEcomGoods goods = ecomGoodsService.getById(goodsDetail.getGoodsId());
		if (goods == null) {
			logger.error("## 新增商品详情信息失败,查询商品{}信息为空", goodsDetail.getGoodsId());
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews21.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews21.getMsg());
		}
		goods.setGoodsDetail(goodsDetail.getDetailId());
		goods.setUpdateUser(goodsDetail.getUpdateUser());
		goods.setUpdateTime(System.currentTimeMillis());
		goods.setLockVersion(goodsDetail.getLockVersion() + 1);
		if (!ecomGoodsService.updateById(goods)) {
			logger.error("## 新增商品详情信息失败,更新商品{}信息失败", goods.getGoodsId());
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews21.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews21.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> deleteGoodsDetail(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);

		String detailId = req.getParameter("detailId");
		TbEcomGoodsDetail goodsDetail = ecomGoodsDetailService.getById(detailId);
		goodsDetail.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
		goodsDetail.setUpdateTime(System.currentTimeMillis());
		goodsDetail.setUpdateUser(user.getId());
		goodsDetail.setLockVersion(goodsDetail.getLockVersion() + 1);

		if (!ecomGoodsDetailService.updateById(goodsDetail)) {
			logger.error("## 删除商品详情信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews23.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews23.getMsg());
		}

		TbEcomGoods goods = ecomGoodsService.getById(goodsDetail.getGoodsId());
		if (goods == null) {
			logger.error("## 删除商品详情信息失败,查询商品{}信息为空", goodsDetail.getGoodsId());
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews23.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews21.getMsg());
		}
		goods.setGoodsDetail("");
		goods.setUpdateUser(goodsDetail.getUpdateUser());
		goods.setUpdateTime(System.currentTimeMillis());
		goods.setLockVersion(goodsDetail.getLockVersion() + 1);
		if (!ecomGoodsService.updateById(goods)) {
			logger.error("## 删除商品详情信息失败,更新商品{}信息失败", goods.getGoodsId());
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews23.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews21.getMsg());
		}
		return ResultsUtil.success();
	}
}
