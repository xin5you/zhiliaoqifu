package com.ebeijia.zl.web.oms.common.service;

import com.ebeijia.zl.web.oms.common.model.FTPImageVo;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface CommonService {

	Map<String, Object> getAccountInfPage(HttpServletRequest req);

	Map<String, Object> getAccountLogInfPage(HttpServletRequest req);

	Map<String, Object> uploadImage(MultipartFile file, HttpServletRequest request, String imgType, String orderId);

	String getImageStrFromPath(String imgPath);

	/**
	 * 文件上传
	 * @param imgVo
	 * @param file
	 * @throws Exception
	 */
	Map<String, Object> uploadImage(FTPImageVo imgVo, MultipartFile file) throws Exception;

	/**
	 * 文件上传返回文件名
	 * @param imgVo
	 * @param file
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> uploadImageName(FTPImageVo imgVo, MultipartFile file) throws Exception;

	/**
	 *  删除图片
	 * @param imgVo
	 * @param fileName
	 */
	void deleteImage(FTPImageVo imgVo, String fileName);

	/**
	 * 查看文件是否已存在
	 * @param imgVo
	 * @param fileName
	 * @return
	 */
	Map<String, Object> isFileExsits(FTPImageVo imgVo, String fileName);
}
