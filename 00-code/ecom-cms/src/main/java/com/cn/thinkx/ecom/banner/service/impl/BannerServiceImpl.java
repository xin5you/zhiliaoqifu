package com.cn.thinkx.ecom.banner.service.impl;

import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cn.thinkx.ecom.FtpProps;
import com.cn.thinkx.ecom.banner.domain.Banner;
import com.cn.thinkx.ecom.banner.mapper.BannerMapper;
import com.cn.thinkx.ecom.banner.service.BannerService;
import com.cn.thinkx.ecom.base.util.FTPUtil;
import com.cn.thinkx.ecom.base.util.FileUtil;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum.bannerNews;
import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.eshopbanner.domain.EshopBanner;
import com.cn.thinkx.ecom.eshopbanner.mapper.EshopBannerMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("bannerService")
public class BannerServiceImpl extends BaseServiceImpl<Banner> implements BannerService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${ECOM_FILE_UPLAOD_PATH}")
	private String ECOM_FILE_UPLAOD_PATH;

	@Value("${ECOM_FILE_UPLAOD_SEPARATOR}")
	private String ECOM_FILE_UPLAOD_SEPARATOR;

	@Value("${ECOM_IMG_SERVER}")
	private String ECOM_IMG_SERVER;

	@Value("${ECOM_FILE_NEW_PATH}")
	private String ECOM_FILE_NEW_PATH;

	@Autowired
	private FtpProps ftpProps;

	@Autowired
	private BannerMapper bannerMapper;

	@Autowired
	private EshopBannerMapper eshopBannerMapper;

	@Override
	public PageInfo<Banner> getBannerPage(int startNum, int pageSize, Banner entity) {
		PageHelper.startPage(startNum, pageSize);
		List<Banner> list = bannerMapper.getList(entity);
		PageInfo<Banner> page = new PageInfo<Banner>(list);
		return page;
	}

	@Override
	public List<Banner> getList() {
		return this.bannerMapper.getList();
	}

	@Override
	public void insertBanner(Banner banner, MultipartFile imageFile) throws Exception {
//		logger.info("新增Banner信息中待上传的Banner图片[{}]", imageFile);
		if (bannerMapper.insert(banner) > 0) {
			String imageUrl = uploadImangeName(banner.getId(), Constants.ImageType.IE01.getValue(), imageFile);
			logger.info("新增Banner信息中已上传Banner图片的路径[{}]", imageUrl);
			if (StringUtil.isNullOrEmpty(imageUrl)) {
				throw new BizHandlerException(bannerNews.BA10.getCode(), bannerNews.BA10.getMsg());
			} else {
				banner.setImageUrl(imageUrl);
				// 对数据库的图片路径进行修改
				bannerMapper.updateByPrimaryKey(banner);
			}
		} else {
			throw new BizHandlerException(bannerNews.BA01.getCode(), bannerNews.BA01.getMsg());
		}
	}

	@Override
	public void updateBanner(Banner banner, MultipartFile imageFile) throws Exception {
//		logger.info("编辑Banner图片中待上传的Banner图片[{}]", imageFile);
		if (!imageFile.isEmpty()) {
			String imageUrl = uploadImangeName(banner.getId(), Constants.ImageType.IE01.getValue(), imageFile);
			logger.info("编辑Banner图片中已上传Banner图片的路径[{}]", imageUrl);
			if (StringUtil.isNullOrEmpty(imageUrl))
				throw new BizHandlerException(bannerNews.BA10.getCode(), bannerNews.BA10.getMsg());
			else
				banner.setImageUrl(imageUrl);
		}
		// 修改banner数据库信息s
		if (bannerMapper.updateByPrimaryKey(banner) < 0)
			throw new BizHandlerException(bannerNews.BA02.getCode(), bannerNews.BA02.getMsg());
	}

	@Override
	public void deleteBanner(Banner banner) throws BizHandlerException {
		Banner ba = bannerMapper.selectByPrimaryKey(banner.getId());
		EshopBanner eb = new EshopBanner();
		eb.setBannerId(banner.getId());
		// 通过banner主键查询得到商城主页信息
		EshopBanner ebr = eshopBannerMapper.getEshopBanner(eb);
		if (ebr != null)
			eshopBannerMapper.deleteByPrimaryKey(ebr.getId());// 删除商城主页与主页关联的数据
		if (bannerMapper.deleteByPrimaryKey(banner.getId()) > 0)// 删除主页信息
			deleteImange(ba.getId(), Constants.ImageType.IE01.getValue(), ba.getImageUrl());// 将图片从FTP上删除
		else
			throw new BizHandlerException(bannerNews.BA03.getCode(), bannerNews.BA03.getMsg());
	}

	@Override
	public void uploadImange(String imgId, String imageType, MultipartFile file) throws Exception {
		if (file != null) {
			String separator = ECOM_FILE_UPLAOD_SEPARATOR;
			String realPath = ECOM_FILE_UPLAOD_PATH + ECOM_FILE_NEW_PATH;
			String changePath = imageType + separator;
			String path = realPath + changePath;
			try {
				// 打开ftp连接
				FTPUtil ftpUtil = new FTPUtil();
				FTPClient ftpClient = ftpUtil.getFTPClient(ftpProps);
				// ftpClient.enterLocalActiveMode(); //主动模式
				ftpClient.enterLocalPassiveMode(); // 被动模式
				String newFilename = FileUtil.getNewFileName(file.getOriginalFilename());
				newFilename = newFilename.replace(newFilename.substring(0, newFilename.indexOf('.')), imgId);
				boolean flag = ftpUtil.uploadFile(ftpClient, path, newFilename, file.getInputStream());
				if (flag)
					throw new BizHandlerException(bannerNews.BA11.getCode(), bannerNews.BA11.getMsg());
				ftpUtil.ftpCloseConnect(ftpClient); // 关闭ftp连接
			} catch (Exception e) {
				throw new BizHandlerException(bannerNews.BA11.getCode(), bannerNews.BA11.getMsg());
			}
		}
	}

	@Override
	public String uploadImangeName(String imgId, String imageType, MultipartFile file) throws Exception {
		if (file != null) {
			String server = ECOM_IMG_SERVER;
			String separator = ECOM_FILE_UPLAOD_SEPARATOR;
			String fixactionPath = ECOM_FILE_UPLAOD_PATH;
			String newPath = ECOM_FILE_NEW_PATH + imageType + separator;
			String realPath = fixactionPath + newPath;
			// 打开ftp连接
			FTPUtil ftpUtil = new FTPUtil();
			FTPClient ftpClient = ftpUtil.getFTPClient(ftpProps);
			ftpClient.enterLocalPassiveMode(); // 被动模式
			try {
				String newFilename = FileUtil.getNewFileName(file.getOriginalFilename());
				newFilename = newFilename.replace(newFilename.substring(0, newFilename.indexOf('.')), imgId);
				boolean flag = ftpUtil.uploadFile(ftpClient, realPath, newFilename, file.getInputStream());
				if (flag) {
					return server + newPath + newFilename;
				}
			} catch (Exception e) {
				throw new BizHandlerException(bannerNews.BA11.getCode(), bannerNews.BA11.getMsg());
			} finally {
				ftpUtil.ftpCloseConnect(ftpClient); // 关闭ftp连接
			}
		}
		return null;
	}

	@Override
	public void deleteImange(String imgId, String imageType, String fileName) {
		String separator = ECOM_FILE_UPLAOD_SEPARATOR;
		String imgPath = ECOM_FILE_UPLAOD_PATH + ECOM_FILE_NEW_PATH;
		String changePath = imageType + separator;
		String imgName = fileName.substring(fileName.lastIndexOf("/") + 1);
		String newPath = imgPath + changePath + imgName;
		FTPUtil ftpUtil = new FTPUtil();
		FTPClient ftpClient = ftpUtil.getFTPClient(ftpProps);
		// ftpClient.enterLocalActiveMode(); //主动模式
		ftpClient.enterLocalPassiveMode(); // 被动模式
		ftpUtil.deleteFile(ftpClient, newPath);
		ftpUtil.ftpCloseConnect(ftpClient);
	}

}
