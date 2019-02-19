package com.ebeijia.zl.web.oms.common.service;

import com.ebeijia.zl.web.oms.common.model.FTPImageVo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface CommonService {

	/**
	 * 查询账户余额信息
	 * @param req
	 * @return
	 */
	Map<String, Object> getAccountInfPage(HttpServletRequest req);

	/**
	 * 查询账户余额明细
	 * @param req
	 * @return
	 */
	Map<String, Object> getAccountLogInfPage(HttpServletRequest req);

	/**
	 * 图片上传(oms服务器根目录)
	 * @param file
	 * @param request
	 * @param imgType
	 * @param orderId
	 * @return
	 */
	Map<String, Object> uploadImage(MultipartFile file, HttpServletRequest request, String imgType, String orderId);

	/**
	 * 读取图片地址并转成Base64格式(oms服务器根目录)
	 * @param imgPath
	 * @return
	 */
	String getImageStrFromPath(String imgPath);

	/**
	 * 图片上传（ftp）
	 * @param imgVo
	 * @param file
	 * @throws Exception
	 */
	Map<String, Object> uploadImage(FTPImageVo imgVo, MultipartFile file) throws Exception;

	/**
	 * 图片上传返回上传地址(oms服务器根目录)
	 * @param imgVo
	 * @param file
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> uploadImageName(FTPImageVo imgVo, MultipartFile file) throws Exception;

	/**
	 *  删除图片（ftp）
	 * @param imgVo
	 * @param fileName
	 */
	void deleteImage(FTPImageVo imgVo, String fileName);

	/**
	 * 查看文件是否已存在（ftp）
	 * @param imgVo
	 * @param fileName
	 * @return
	 */
	Map<String, Object> isFileExsits(FTPImageVo imgVo, String fileName);

}
