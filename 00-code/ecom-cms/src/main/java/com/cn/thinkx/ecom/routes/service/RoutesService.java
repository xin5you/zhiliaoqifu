package com.cn.thinkx.ecom.routes.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.service.BaseService;
import com.cn.thinkx.ecom.routes.domain.Routes;
import com.github.pagehelper.PageInfo;

public interface RoutesService extends BaseService<Routes> {

	/**
	 * 电商入口列表（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<Routes> getRoutesPage(int startNum, int pageSize, Routes entity);

	/**
	 * 查询所有电商入口信息
	 * 
	 * @return
	 */
	List<Routes> getList();

	/**
	 * 根据电商名称查询电商入口信息
	 * 
	 * @param ecomName
	 * @return
	 */
	Routes selectByEcomName(String ecomName);

	/**
	 * 根据电商代码查询电商入口信息
	 * 
	 * @param ecomCode
	 * @return
	 */
	List<Routes> selectByEcomCode(String ecomCode);

	/**
	 * 新增电商入口信息
	 * 
	 * @param routes
	 * @param loginFile
	 * @return
	 */
	void insertRoutes(Routes routes, MultipartFile loginFile) throws Exception;

	/**
	 * 修改Routes信息
	 * 
	 * @param routes
	 * @param loginFile
	 */
	void updateRoutes(Routes routes, MultipartFile loginFile) throws Exception;

	/**
	 * 删除Routes与关联信息
	 * 
	 * @param routes
	 */
	void deleteRoutes(Routes routes) throws Exception;

	/**
	 * 文件上传
	 * 
	 * @param channelNo
	 * @param imgId
	 * @param imageType
	 * @param file
	 */
	void uploadImange(String channelNo, String imgId, String imageType, MultipartFile file) throws BizHandlerException;

	/**
	 * 文件上传返回文件名
	 * 
	 * @param channelNo
	 * @param imgId
	 * @param imageType
	 * @param file
	 * @return
	 */
	String uploadImangeName(String channelNo, String imgId, String imageType, MultipartFile file) throws BizHandlerException;

	/**
	 * 删除图片
	 * 
	 * @param channelNo
	 * @param imgId
	 * @param imageType
	 * @param fileName
	 */
	void deleteImange(String channelNo, String imgId, String imageType, String fileName) throws BizHandlerException;

}
