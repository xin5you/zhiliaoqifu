package com.ebeijia.zl.web.cms.ueditor.controller;

import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.enums.ImageTypeEnum;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsDetailService;
import com.ebeijia.zl.web.cms.base.controller.AuthCodeController;
import com.ebeijia.zl.web.cms.base.exception.BizHandlerException;
import com.ebeijia.zl.web.cms.base.service.ImageService;
import com.ebeijia.zl.web.cms.base.vo.FTPImageVo;
import com.ebeijia.zl.web.cms.ueditor.ActionEnter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Calendar;
import java.util.HashMap;

@Controller
@RequestMapping(value = "ueditor")
public class UEditorController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${FILE_UPLAOD_PATH}")
    private String FILE_UPLAOD_PATH;

    @Value("${FILE_UPLAOD_SEPARATOR}")
    private String FILE_UPLAOD_SEPARATOR;

    @Value("${IMG_SERVER}")
    private String IMG_SERVER;

    @Value("${FILE_NEW_PATH}")
    private String FILE_NEW_PATH;

    @Value("${UEDITOR_ROOTPATH}")
    private String UEDITOR_ROOTPATH;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ITbEcomGoodsDetailService ecomGoodsDetailService;

    /**
     * 百度富文本编辑器
     * @param request
     * @param response
     */
    @RequestMapping(value="/config")
    public void config(HttpServletRequest request, HttpServletResponse response) {
//        response.setContentType("application/json");
        response.setHeader("Content-Type" , "text/html");
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        rootPath = rootPath + UEDITOR_ROOTPATH;
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (JSONException e) {
            e.printStackTrace();
            logger.error("## UEditor去读配置文件config.json异常", e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("## UEditor去读配置文件config.json异常", e);
        }
    }

    /**
     * 富文本图片上传
     * @param req
     * @param upfile
     * @return
     * @throws FileNotFoundException
     */
    @ResponseBody
    @RequestMapping(value = "/uploadImage")
    public String imgUpdate(HttpServletRequest req, MultipartFile upfile) throws FileNotFoundException {
        // 判断是否有文件提交
        if (upfile == null || upfile.isEmpty()) {
            return "error";
        }
        String detailId = req.getParameter("detailId");
        String newDetailId = detailId + System.currentTimeMillis();

        FTPImageVo imgVo = new FTPImageVo();
        imgVo.setImgId(newDetailId);
        imgVo.setService(IMG_SERVER);
        imgVo.setNewPath(FILE_NEW_PATH);
        imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
        imgVo.setUploadPath(FILE_UPLAOD_PATH);
        imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_07.getValue());
        String imageUrl = "";
        try {
            imageUrl = imageService.uploadImangeName(imgVo, upfile);
            if (StringUtil.isNullOrEmpty(imageUrl)) {
                logger.error("## 商品详情图片上传返回路径为空");
                return "error";
            }
        } catch (BizHandlerException e) {
            logger.error("## 商品详情图片上传异常", e.getMessage());
            return "error";
        } catch (Exception e) {
            logger.error("## 商品详情图片上传异常");
            return "error";
        }

        String fileName = upfile.getOriginalFilename(); // 获取文件名
        String suffixName = fileName.substring(fileName.lastIndexOf(".")); // 获取文件的后缀名
        fileName = newDetailId + suffixName;

    try {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,Object> configs = new HashMap<String,Object>();
        configs.put("state", "SUCCESS");
        configs.put("url", imageUrl);
        configs.put("title", fileName);
        configs.put("original", fileName);
        return mapper.writeValueAsString(configs);
    } catch (IllegalStateException e) {
        logger.error("## 添加商品详情{}富文本信息异常", detailId, e.toString());
    } catch (IOException e) {
        logger.error("## 添加商品详情{}富文本信息异常", detailId, e.toString());
    }
        return "error";
    }

}
