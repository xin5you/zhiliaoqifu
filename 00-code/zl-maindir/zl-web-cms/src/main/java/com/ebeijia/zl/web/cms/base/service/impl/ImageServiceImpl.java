package com.ebeijia.zl.web.cms.base.service.impl;

import com.ebeijia.zl.FtpProps;
import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.web.cms.base.exception.BizHandlerException;
import com.ebeijia.zl.web.cms.base.service.ImageService;
import com.ebeijia.zl.web.cms.base.util.FTPUtil;
import com.ebeijia.zl.web.cms.base.util.FileUtil;
import com.ebeijia.zl.web.cms.base.vo.FTPImageVo;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private FtpProps ftpProps;

    @Override
    public void uploadImange(FTPImageVo imgVo, MultipartFile file) throws Exception {
        if (file != null) {
            StringBuffer path = new StringBuffer();
            path.append(imgVo.getUploadPath())
                    .append(imgVo.getNewPath())
//                .append(imgVo.getDate())
                    .append(imgVo.getImgType())
                    .append(imgVo.getSeparator());
            try {
                // 打开ftp连接
                FTPUtil ftpUtil = new FTPUtil();
                FTPClient ftpClient = ftpUtil.getFTPClient(ftpProps);
                // ftpClient.enterLocalActiveMode(); //主动模式
                ftpClient.enterLocalPassiveMode(); // 被动模式
                String newFilename = FileUtil.getNewFileName(file.getOriginalFilename());
                newFilename = newFilename.replace(newFilename.substring(0, newFilename.indexOf('.')), imgVo.getImgId());
                boolean flag = ftpUtil.uploadFile(ftpClient, path.toString(), newFilename, file.getInputStream());
                if (flag)
                    throw new BizHandlerException(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
                ftpUtil.ftpCloseConnect(ftpClient); // 关闭ftp连接
            } catch (Exception e) {
                throw new BizHandlerException(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
            }
        }
    }

    @Override
    public String uploadImangeName(FTPImageVo imgVo, MultipartFile file) throws Exception {
        if (file != null) {
            StringBuffer newPath = new StringBuffer();
            newPath.append(imgVo.getNewPath())
//                .append(imgVo.getDate())
                    .append(imgVo.getImgType())
                    .append(imgVo.getSeparator());
            StringBuffer realPath = new StringBuffer();
            realPath.append(imgVo.getUploadPath())
                    .append(newPath);
            // 打开ftp连接
            FTPUtil ftpUtil = new FTPUtil();
            FTPClient ftpClient = ftpUtil.getFTPClient(ftpProps);
            ftpClient.enterLocalPassiveMode(); // 被动模式
            try {
                String newFilename = FileUtil.getNewFileName(file.getOriginalFilename());
                newFilename = newFilename.replace(newFilename.substring(0, newFilename.indexOf('.')), imgVo.getImgId());
                boolean flag = ftpUtil.uploadFile(ftpClient, realPath.toString(), newFilename, file.getInputStream());
                if (flag) {
                    return imgVo.getService() + newPath + newFilename;
                }
            } catch (Exception e) {
                throw new BizHandlerException(ExceptionEnum.ImageNews.ImageNews02.getCode(), ExceptionEnum.ImageNews.ImageNews02.getMsg());
            } finally {
                ftpUtil.ftpCloseConnect(ftpClient); // 关闭ftp连接
            }
        }
        return null;
    }

    @Override
    public void deleteImange(FTPImageVo imgVo, String fileName) {
        StringBuffer newPath = new StringBuffer();
        String imgName = fileName.substring(fileName.lastIndexOf("/") + 1);
        newPath.append(imgVo.getUploadPath())
                .append(imgVo.getNewPath())
//                .append(imgVo.getDate())
                .append(imgVo.getImgType())
                .append(imgVo.getSeparator())
                .append(imgName);
        FTPUtil ftpUtil = new FTPUtil();
        FTPClient ftpClient = ftpUtil.getFTPClient(ftpProps);
        // ftpClient.enterLocalActiveMode(); //主动模式
        ftpClient.enterLocalPassiveMode(); // 被动模式
        ftpUtil.deleteFile(ftpClient, newPath.toString());
        ftpUtil.ftpCloseConnect(ftpClient);
    }

    @Override
    public boolean isFileExsits(FTPImageVo imgVo, String fileName) {
        StringBuffer newPath = new StringBuffer();
        String imgName = fileName.substring(fileName.lastIndexOf("/") + 1);
        newPath.append(imgVo.getUploadPath())
                .append(imgVo.getNewPath())
//                .append(imgVo.getDate())
                .append(imgVo.getImgType())
                .append(imgVo.getSeparator())
                .append(imgName);
        FTPUtil ftpUtil = new FTPUtil();
        FTPClient ftpClient = ftpUtil.getFTPClient(ftpProps);
        // ftpClient.enterLocalActiveMode(); //主动模式
        ftpClient.enterLocalPassiveMode(); // 被动模式
        boolean flag = ftpUtil.isFileExsits(ftpClient, newPath.toString());
        ftpUtil.ftpCloseConnect(ftpClient);
        return flag;
    }
}
