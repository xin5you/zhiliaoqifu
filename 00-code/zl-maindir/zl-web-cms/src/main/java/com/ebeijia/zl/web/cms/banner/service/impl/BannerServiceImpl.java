package com.ebeijia.zl.web.cms.banner.service.impl;

import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.BannerPositionEnum;
import com.ebeijia.zl.common.utils.enums.BannerSpecEnum;
import com.ebeijia.zl.common.utils.enums.ImageTypeEnum;
import com.ebeijia.zl.common.utils.enums.IsDisabledEnum;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomBanner;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomBannerService;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomMemberService;
import com.ebeijia.zl.web.cms.banner.service.BannerService;
import com.ebeijia.zl.web.cms.base.exception.BizHandlerException;
import com.ebeijia.zl.web.cms.base.service.ImageService;
import com.ebeijia.zl.web.cms.base.vo.FTPImageVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service("bannerService")
public class BannerServiceImpl implements BannerService {

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
	private ITbEcomBannerService ecomBannerService;

	public PageInfo<TbEcomBanner> getBannerListPage(int startNum, int pageSize, TbEcomBanner entity) {
		PageHelper.startPage(startNum, pageSize);
		List<TbEcomBanner> bannerList = ecomBannerService.getBannerList(entity);
		if (bannerList != null && bannerList.size() > 0) {
			for (TbEcomBanner b : bannerList) {
				b.setPosition(BannerPositionEnum.findByBId(b.getPosition()).getName());
				b.setSpec(BannerSpecEnum.findByBId(b.getSpec()).getName());
			}
		}
		PageInfo<TbEcomBanner> bannerPage = new PageInfo<TbEcomBanner>(bannerList);
		return bannerPage;
	}

	@Override
	public BaseResult<Object> addBanner(TbEcomBanner banner, MultipartFile imageUrlFile) {
		TbEcomBanner bannerSort = ecomBannerService.getBannerBySort(banner.getSort());
		if (bannerSort != null) {
			logger.error("## 新增banner信息失败，排序号{}已存在", banner.getSort());
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews14.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews14.getMsg());
		}
		//判断上传图片是否为空
		if (imageUrlFile == null || imageUrlFile.isEmpty()) {
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews03.getCode(), ExceptionEnum.ImageNews.ImageNews03.getMsg());
		}
		FTPImageVo imgVo = new FTPImageVo();
		imgVo.setImgId(banner.getId());
		imgVo.setService(IMG_SERVER);
		imgVo.setNewPath(FILE_NEW_PATH);
		imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
		imgVo.setUploadPath(FILE_UPLAOD_PATH);
		imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_09.getValue());
		String imageUrl = "";
		try {
			imageUrl = imageService.uploadImangeName(imgVo, imageUrlFile);
			if (StringUtil.isNullOrEmpty(imageUrl)) {
				logger.error("## banner图片上传返回路径为空");
				return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews01.getCode(), ExceptionEnum.ImageNews.ImageNews01.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("## banner图片上传异常", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## banner图片上传异常");
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
		}
		banner.setImageUrl(imageUrl);
		//添加banner信息
		if (!ecomBannerService.save(banner)) {
			logger.error("## 新增banner信息失败");
			return ResultsUtil.error(ExceptionEnum.BannerNews.BannerNews01.getCode(), ExceptionEnum.BannerNews.BannerNews01.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> editBanner(TbEcomBanner banner, MultipartFile imageUrlFile) {
		TbEcomBanner bannerSort = ecomBannerService.getBannerBySort(banner.getSort());
		if (bannerSort != null && !bannerSort.getId().equals(banner.getId())) {
			logger.error("## 编辑banner信息失败，排序号{}已存在", banner.getSort());
			return ResultsUtil.error(ExceptionEnum.GoodsSpecNews.GoodsSpecNews14.getCode(), ExceptionEnum.GoodsSpecNews.GoodsSpecNews14.getMsg());
		}

		//判断上传的图片是否为空
		if (StringUtil.isNullOrEmpty(banner.getImageUrl())) {
			return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews03.getCode(), ExceptionEnum.ImageNews.ImageNews03.getMsg());
		}
		if (imageUrlFile != null && !imageUrlFile.isEmpty()) {
			FTPImageVo imgVo = new FTPImageVo();
			imgVo.setImgId(banner.getId());
			imgVo.setService(IMG_SERVER);
			imgVo.setNewPath(FILE_NEW_PATH);
			imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
			imgVo.setUploadPath(FILE_UPLAOD_PATH);
			imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_01.getValue());
			try {
				if (imageUrlFile != null && !imageUrlFile.isEmpty()) {
					String imageUrl = imageService.uploadImangeName(imgVo, imageUrlFile);
					if (StringUtil.isNullOrEmpty(imageUrl)) {
						logger.error("## banner图片上传返回路径为空");
						return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews01.getCode(), ExceptionEnum.ImageNews.ImageNews01.getMsg());
					}
					banner.setImageUrl(imageUrl);
				}
			} catch (BizHandlerException e) {
				logger.error("## banner图片上传异常", e.getMessage());
				return ResultsUtil.error(e.getCode(), e.getMessage());
			} catch (Exception e) {
				logger.error("## banner图片上传异常");
				return ResultsUtil.error(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
			}
		}

		if (!ecomBannerService.updateById(banner)) {
			logger.error("## 编辑banner信息失败");
			return ResultsUtil.error(ExceptionEnum.BannerNews.BannerNews02.getCode(), ExceptionEnum.BannerNews.BannerNews02.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> deleteBanner(HttpServletRequest req) {
		String bannerId = req.getParameter("bannerId");
		TbEcomBanner banner = ecomBannerService.getById(bannerId);
		if (banner == null) {
			logger.error("## 删除banner信息失败,查询banner{}信息不存在", bannerId);
			return ResultsUtil.error(ExceptionEnum.BannerNews.BannerNews03.getCode(), ExceptionEnum.BannerNews.BannerNews03.getMsg());
		}
		banner.setDisable(IsDisabledEnum.IsDisabledEnum_1.getCode());
		if (!ecomBannerService.updateById(banner)) {
			logger.error("## 删除banner{}信息失败", bannerId);
			return ResultsUtil.error(ExceptionEnum.BannerNews.BannerNews03.getCode(), ExceptionEnum.BannerNews.BannerNews03.getMsg());
		}
		return ResultsUtil.success();
	}

}
