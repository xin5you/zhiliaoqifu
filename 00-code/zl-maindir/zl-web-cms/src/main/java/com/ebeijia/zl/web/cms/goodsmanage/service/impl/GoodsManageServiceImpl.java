package com.ebeijia.zl.web.cms.goodsmanage.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.GoodsSpecTypeEnum;
import com.ebeijia.zl.common.utils.enums.ImageTypeEnum;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomSpecValues;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomSpecification;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsService;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomSpecValuesService;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomSpecificationService;
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
		FTPImageVo imgVo = new FTPImageVo();
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
		if (!ecomSpecValuesService.updateById(entity)) {
			logger.error("## 编辑商品规格值信息失败");
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews05.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews05.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> deleteGoodsSpecValues(TbEcomSpecValues entity) {
		FTPImageVo imgVo = new FTPImageVo();
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
		}
		return ResultsUtil.success();
	}

	@Override
	public PageInfo<TbEcomGoods> getGoodsInfListPage(int startNum, int pageSize, TbEcomGoods entity) {
		List<TbEcomGoods> goodsInfList = new ArrayList<TbEcomGoods>();

		PageHelper.startPage(startNum, pageSize);
		goodsInfList = ecomGoodsService.getGoodsInfList(entity);
		PageInfo<TbEcomGoods> page = new PageInfo<TbEcomGoods>(goodsInfList);
		return page;
	}
}
