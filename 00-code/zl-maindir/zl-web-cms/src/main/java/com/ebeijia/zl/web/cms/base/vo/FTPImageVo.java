package com.ebeijia.zl.web.cms.base.vo;

import com.ebeijia.zl.common.utils.tools.DateUtil;

import java.io.Serializable;

/**
 * ftp图片上传封装实体类
 */
public class FTPImageVo implements Serializable {

    private String imgId;       //图片名称
    private String date;        //图片存储日期
    private String imgType;     //图片类型（以项目来区分）
    private String separator;   //分隔符
    private String uploadPath;  //上传路径
    private String newPath;     //创建图片存放文件夹
    private String service;     //图片服务器地址

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getDate() {
        return DateUtil.getDate();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }
}
