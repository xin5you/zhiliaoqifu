package com.cn.thinkx.ecom.banner.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cn.thinkx.ecom.banner.domain.Banner;
import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.service.BaseService;
import com.github.pagehelper.PageInfo;

public interface BannerService extends BaseService<Banner> {

	/**
	 * Banner列表（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<Banner> getBannerPage(int startNum, int pageSize, Banner entity);

	/**
	 * 查询所有Banner信息
	 * 
	 * @return
	 */
	public List<Banner> getList();

	/**
	 * 新增Banner信息
	 * 
	 * @param banner
	 * @param imageFile
	 * @return
	 */
	void insertBanner(Banner banner, MultipartFile imageFile) throws Exception;

	/**
	 * 修改Banner信息
	 * 
	 * @param banner
	 * @param imageFile
	 */
	void updateBanner(Banner banner, MultipartFile imageFile) throws Exception;

	/**
	 * 删除Banner与关联信息
	 * 
	 * @param banner
	 */
	void deleteBanner(Banner banner) throws BizHandlerException;

	/**
	 * 文件上传
	 * 
	 * @param imgId
	 * @param imageType
	 * @param file
	 */
	void uploadImange(String imgId, String imageType, MultipartFile file) throws Exception;

	/**
	 * 文件上传返回文件名
	 * 
	 * @param imgId
	 * @param imageType
	 * @param file
	 * @return
	 */
	String uploadImangeName(String imgId, String imageType, MultipartFile file) throws Exception;

	/**
	 * 删除图片
	 * 
	 * @param imgId
	 * @param imageType
	 * @param fileName
	 */
	void deleteImange(String imgId, String imageType, String fileName);

}
