package com.ebeijia.zl.web.cms.base.service;

import com.ebeijia.zl.web.cms.base.vo.FTPImageVo;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    /**
     * 文件上传
     * @param imgVo
     * @param file
     * @throws Exception
     */
    void uploadImange(FTPImageVo imgVo, MultipartFile file) throws Exception;

    /**
     * 文件上传返回文件名
     * @param imgVo
     * @param file
     * @return
     * @throws Exception
     */
    String uploadImangeName(FTPImageVo imgVo, MultipartFile file) throws Exception;

    /**
     *  删除图片
     * @param imgVo
     * @param fileName
     */
    void deleteImange(FTPImageVo imgVo, String fileName);

    /**
     * 查看文件是否已存在
     * @param imgVo
     * @param fileName
     * @return
     */
    boolean isFileExsits(FTPImageVo imgVo, String fileName);
}
