package com.cn.thinkx.ecom.routes.service.impl;

import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cn.thinkx.ecom.FtpProps;
import com.cn.thinkx.ecom.base.util.FTPUtil;
import com.cn.thinkx.ecom.base.util.FileUtil;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum.bannerNews;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum.routesNews;
import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.eshoproutes.domain.EshopRoutes;
import com.cn.thinkx.ecom.eshoproutes.mapper.EshopRoutesMapper;
import com.cn.thinkx.ecom.routes.domain.Routes;
import com.cn.thinkx.ecom.routes.mapper.RoutesMapper;
import com.cn.thinkx.ecom.routes.service.RoutesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("routesService")
public class RoutesServiceImpl extends BaseServiceImpl<Routes> implements RoutesService {
	
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
	private RoutesMapper routesMapper;

	@Autowired
	private EshopRoutesMapper eshopRoutesMapper;

	@Override
	public PageInfo<Routes> getRoutesPage(int startNum, int pageSize, Routes entity) {
		PageHelper.startPage(startNum, pageSize);
		List<Routes> list = routesMapper.getList(entity);
		if (list != null && list.size() > 0) {
			for (Routes m : list)
				m.setEcomCode(Constants.ChannelEcomType.findByCode(m.getEcomCode()).getValue() + "   " + m.getEcomCode());
		}
		PageInfo<Routes> page = new PageInfo<Routes>(list);
		return page;
	}

	@Override
	public List<Routes> getList() {
		return this.routesMapper.getList();
	}

	@Override
	public Routes selectByEcomName(String ecomName) {
		return this.routesMapper.selectByEcomName(ecomName);
	}

	@Override
	public List<Routes> selectByEcomCode(String ecomCode) {
		return this.routesMapper.selectByEcomCode(ecomCode);
	}

	@Override
	public void insertRoutes(Routes routes, MultipartFile loginFile) throws Exception {
//		logger.info("新增入口信息中待上传的LOGO图片[{}]", loginFile);
		if (routesMapper.insert(routes) > 0) {
			// 上传Banner图片
			String imageUrl = uploadImangeName(routes.getEcomCode(), routes.getId(), Constants.ImageType.IE02.getValue(), loginFile);
			logger.info("新增入口信息中已上传LOGO图片的路径[{}]", imageUrl);
			if (StringUtil.isNullOrEmpty(imageUrl)) {
				throw new BizHandlerException(routesNews.RO09.getCode(), routesNews.RO09.getMsg());
			} else {
				routes.setEcomLogo(imageUrl);
				routesMapper.updateByPrimaryKey(routes);
			}
		} else {
			throw new BizHandlerException(routesNews.RO01.getCode(), routesNews.RO01.getMsg());
		}
	}

	@Override
	public void updateRoutes(Routes routes, MultipartFile loginFile) throws Exception {
//		logger.info("编辑入口信息中待上传的LOGO图片[{}]", loginFile);
		if (!loginFile.isEmpty()) {
			// 上传Banner图片
			String imageUrl = uploadImangeName(routes.getEcomCode(), routes.getId(),
					Constants.ImageType.IE02.getValue(), loginFile);
			logger.info("编辑入口信息中已上传LOGO图片的路径[{}]", imageUrl);
			if (StringUtil.isNullOrEmpty(imageUrl))
				throw new BizHandlerException(routesNews.RO09.getCode(), routesNews.RO09.getMsg());
			else
				routes.setEcomLogo(imageUrl);
		}
		if (routesMapper.updateByPrimaryKey(routes) < 0)
			throw new BizHandlerException(routesNews.RO02.getCode(), routesNews.RO02.getMsg());
	}

	@Override
	public void deleteRoutes(Routes routes) throws Exception {
		Routes rs = routesMapper.selectByPrimaryKey(routes.getId());
		EshopRoutes er = new EshopRoutes();
		er.setRoutesId(routes.getId());
		EshopRoutes ers = eshopRoutesMapper.getEshopRoutes(er);
		if (ers != null)
			eshopRoutesMapper.deleteByPrimaryKey(ers.getId());
		if (routesMapper.deleteByPrimaryKey(routes.getId()) > 0)
			deleteImange(rs.getEcomCode(), rs.getId(), Constants.ImageType.IE02.getValue(), rs.getEcomLogo());
		else
			throw new BizHandlerException(routesNews.RO03.getCode(), routesNews.RO03.getMsg());
	}

	@Override
	public void uploadImange(String channelNo, String imgId, String imageType, MultipartFile file) throws BizHandlerException {
		if (file != null) {
			String realPath = ECOM_FILE_UPLAOD_PATH + ECOM_FILE_NEW_PATH;
			String separator = ECOM_FILE_UPLAOD_SEPARATOR;
			String changePath = imageType + separator + channelNo + separator;
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
					throw new BizHandlerException(routesNews.RO10.getCode(), routesNews.RO10.getMsg());
				ftpUtil.ftpCloseConnect(ftpClient); // 关闭ftp连接
			} catch (Exception e) {
				throw new BizHandlerException(bannerNews.BA10.getCode(), bannerNews.BA10.getMsg());
			}
		}
	}

	@Override
	public String uploadImangeName(String channelNo, String imgId, String imageType, MultipartFile file) throws BizHandlerException {
		if (file != null) {
			String server = ECOM_IMG_SERVER;
			String fixactionPath = ECOM_FILE_UPLAOD_PATH;
			String separator = ECOM_FILE_UPLAOD_SEPARATOR;
			String changePath = ECOM_FILE_NEW_PATH + imageType + separator + channelNo + separator;
			String realPath = fixactionPath + changePath;
			// 打开ftp连接
			FTPUtil ftpUtil = new FTPUtil();
			FTPClient ftpClient = ftpUtil.getFTPClient(ftpProps);
			ftpClient.enterLocalPassiveMode(); // 被动模式
			try {
				String newFilename = FileUtil.getNewFileName(file.getOriginalFilename());
				newFilename = newFilename.replace(newFilename.substring(0, newFilename.indexOf('.')), imgId);
				boolean flag = ftpUtil.uploadFile(ftpClient, realPath, newFilename, file.getInputStream());
				if (flag)
					return server + changePath + newFilename;
			} catch (Exception e) {
				throw new BizHandlerException(routesNews.RO10.getCode(), routesNews.RO10.getMsg());
			} finally {
				ftpUtil.ftpCloseConnect(ftpClient); // 关闭ftp连接
			}
		}
		return null;
	}

	@Override
	public void deleteImange(String channelNo, String imgId, String imageType, String fileName) throws BizHandlerException {
		String separator = ECOM_FILE_UPLAOD_SEPARATOR;
		String imgPath = ECOM_FILE_UPLAOD_PATH + ECOM_FILE_NEW_PATH;
		String changePath = imageType + separator + channelNo + separator;
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
