package com.ebeijia.zl.web.cms.goodsmanage.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
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
	private ITbEcomGoodsGalleryService ecomGoodsGalleryService;

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
		List<TbEcomSpecValues> goodsSpecValuesList = new ArrayList<TbEcomSpecValues>();

		PageHelper.startPage(startNum, pageSize);
		goodsSpecValuesList = ecomSpecValuesService.getGoodsSpecValuesList(entity);
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
		if (ecomGoodsProductService.updateBatchById(ecomGoodsProductList)) {
			logger.error("## 更新商品Spu{}下的所有Sku信息上下架失败", goodsId);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews10.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews10.getMsg());
		}

		return ResultsUtil.success();
	}

	@Override
	public PageInfo<TbEcomGoodsGallery> getGoodsGalleryListPage(int startNum, int pageSize, TbEcomGoodsGallery entity) {
		List<TbEcomGoodsGallery> goodsGalleryList = new ArrayList<TbEcomGoodsGallery>();
		PageHelper.startPage(startNum, pageSize);
		goodsGalleryList = ecomGoodsGalleryService.getGoodsGalleryList(entity);
		PageInfo<TbEcomGoodsGallery> page = new PageInfo<TbEcomGoodsGallery>(goodsGalleryList);
		return page;
	}

	@Override
	public BaseResult<Object> addGoodsGallery(TbEcomGoodsGallery entity, MultipartFile originalFile) {
		TbEcomGoodsGallery gallery = ecomGoodsGalleryService.getGoodsGalleryBySort(entity.getSort());
		if (gallery != null) {
			logger.error("## 新增商品相册信息失败,排序号{}已存在");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews14.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews14.getMsg());
		}

		if (entity.getIsDefault().equals(IsDefaultEnum.IsDefaultEnum_0.getCode())) {
			TbEcomGoodsGallery goodsGallery = ecomGoodsGalleryService.getGoodsGalleryByIsDefault(entity.getIsDefault());
			if (goodsGallery != null) {
				goodsGallery.setIsDefault(IsDefaultEnum.IsDefaultEnum_1.getCode());
				if (ecomGoodsGalleryService.updateById(goodsGallery)) {
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
		TbEcomGoodsGallery gallery = ecomGoodsGalleryService.getGoodsGalleryBySort(entity.getSort());
		if (gallery != null) {
			logger.error("## 编辑商品相册信息失败,排序号{}已存在");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews14.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews14.getMsg());
		}

		if (entity.getIsDefault().equals(IsDefaultEnum.IsDefaultEnum_0.getCode())) {
			TbEcomGoodsGallery goodsGallery = ecomGoodsGalleryService.getGoodsGalleryByIsDefault(entity.getIsDefault());
			if (goodsGallery != null) {
				goodsGallery.setIsDefault(IsDefaultEnum.IsDefaultEnum_1.getCode());
				if (ecomGoodsGalleryService.updateById(goodsGallery)) {
					logger.error("## 编辑商品相册信息失败");
					return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews12.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews12.getMsg());
				}
			}
		}

		if (StringUtil.isNullOrEmpty(entity.getThumbnail())) {
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
		List<TbEcomGoodsProduct> goodsProductList = new ArrayList<TbEcomGoodsProduct>();

		PageHelper.startPage(startNum, pageSize);
		goodsProductList = ecomGoodsProductService.getGoodsProductList(entity);
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

		if (IsDefaultEnum.IsDefaultEnum_0.getCode().equals(entity.getIsDefault())) {
			TbEcomGoods goods = ecomGoodsService.getById(entity.getGoodsId());
			goods.setDefaultSkuCode(entity.getIsDefault());
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
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> editGoodsProduct(TbEcomGoodsProduct entity, MultipartFile picUrlFile) {
		TbEcomGoodsProduct product = ecomGoodsProductService.getGoodsProductBySkuCode(entity.getSkuCode());
		if (product != null) {
			logger.error("## 编辑商品Sku信息失败,skuCode{}已存在");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews18.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews18.getMsg());
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

		if (IsDefaultEnum.IsDefaultEnum_0.getCode().equals(entity.getIsDefault())) {
			TbEcomGoods goods = ecomGoodsService.getById(entity.getGoodsId());
			goods.setDefaultSkuCode(entity.getIsDefault());
			goods.setUpdateTime(System.currentTimeMillis());
			goods.setUpdateUser(entity.getUpdateUser());
			goods.setLockVersion(goods.getLockVersion() + 1);
			if (!ecomGoodsService.updateById(goods)) {
				logger.error("## 编辑商品Sku信息失败,设置默认Sku出错");
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews15.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews15.getMsg());
			}
		}

		if (!ecomGoodsProductService.updateById(entity)) {
			logger.error("## 编辑商品Sku信息失败");
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

			if (!ecomGoodsProductService.updateById(product)) {
				logger.error("# 删除商品Sku信息失败,productId--->{}", productId);
				return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews17.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews17.getMsg());
			}

		} catch (Exception e) {
			logger.error("# 删除商品Sku信息异常,productId--->{}", productId, e);
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews17.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews17.getMsg());
		}
		return ResultsUtil.success();
	}
}
