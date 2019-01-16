package com.ebeijia.zl.web.oms.common.service.impl;

import com.ebeijia.zl.FtpProps;
import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.account.req.AccountQueryReqVo;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import com.ebeijia.zl.facade.account.vo.AccountLogVO;
import com.ebeijia.zl.facade.account.vo.AccountVO;
import com.ebeijia.zl.web.oms.common.model.FTPImageVo;
import com.ebeijia.zl.web.oms.common.service.CommonService;
import com.ebeijia.zl.web.oms.common.util.FTPUtil;
import com.ebeijia.zl.web.oms.common.util.FileUtil;
import com.ebeijia.zl.web.oms.common.util.TransChnlEnum;
import com.github.pagehelper.PageInfo;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("commonService")
public class CommonServiceImpl implements CommonService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FtpProps ftpProps;

    @Autowired
    private AccountQueryFacade accountQueryFacade;

    @Value("${IMG_PATH}")
    private String IMG_PATH;

    @Override
    public Map<String, Object> getAccountInfPage(HttpServletRequest req) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Boolean.TRUE);
        String providerId = StringUtil.nullToString(req.getParameter("providerId"));
        String companyId = StringUtil.nullToString(req.getParameter("companyId"));
        String channelId = StringUtil.nullToString(req.getParameter("channelId"));
        int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
        AccountQueryReqVo reqVo = new AccountQueryReqVo();
        if (!StringUtil.isNullOrEmpty(providerId)) {
            reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
            reqVo.setUserChnlId(providerId);
            reqVo.setUserType(UserType.TYPE300.getCode());
        } else if (!StringUtil.isNullOrEmpty(companyId)) {
            reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
            reqVo.setUserChnlId(companyId);
            reqVo.setUserType(UserType.TYPE200.getCode());
        } else if (!StringUtil.isNullOrEmpty(channelId)) {
            reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
            reqVo.setUserChnlId(channelId);
            reqVo.setUserType(UserType.TYPE400.getCode());
        } else {
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "查询账户不正确");
            return resultMap;
        }
        try {
            PageInfo<AccountVO> pageList = accountQueryFacade.getAccountInfPage(startNum, pageSize, reqVo);
            /*if (pageList != null) {
                for (AccountVO vo : pageList.getList()) {
                    vo.setBId(SpecAccountTypeEnum.findByBId(vo.getBId()).getName());
                }
            }*/
            resultMap.put("pageInfo", pageList);
        } catch (Exception e) {
            logger.error("## 查询账户余额列表异常");
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "查询账户余额列表异常");
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getAccountLogInfPage(HttpServletRequest req) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Boolean.TRUE);
        String providerId = StringUtil.nullToString(req.getParameter("providerId"));
        String companyId = StringUtil.nullToString(req.getParameter("companyId"));
        String channelId = StringUtil.nullToString(req.getParameter("channelId"));
        String bId = StringUtil.nullToString(req.getParameter("bId"));
        int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
        AccountQueryReqVo reqVo = new AccountQueryReqVo();
        if (!StringUtil.isNullOrEmpty(providerId)) {
            reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
            reqVo.setUserChnlId(providerId);
            reqVo.setUserType(UserType.TYPE300.getCode());
        } else if (!StringUtil.isNullOrEmpty(companyId)) {
            reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
            reqVo.setUserChnlId(companyId);
            reqVo.setUserType(UserType.TYPE200.getCode());
        } else if (!StringUtil.isNullOrEmpty(channelId)) {
            reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
            reqVo.setUserChnlId(channelId);
            reqVo.setUserType(UserType.TYPE400.getCode());
        } else {
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "查询账户不正确");
            return resultMap;
        }
        reqVo.setBId(bId);
        try {
            PageInfo<AccountLogVO> pageList = accountQueryFacade.getAccountLogPage(startNum, pageSize, reqVo);
            if (pageList != null) {
                for (AccountLogVO log : pageList.getList()) {
                    Date date = new SimpleDateFormat("yyyyMMdd").parse(log.getTxnDate());
                    String txnDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    Date time = new SimpleDateFormat("hhmmss").parse(log.getTxnTime());
                    String txnTime = new SimpleDateFormat("hh:mm:ss").format(date);
                    log.setTxnDate(txnDate);
                    log.setTxnTime(txnTime);
                    log.setAccType(AccountCardAttrEnum.findByValue(log.getAccType()).getDesc());
                    log.setTransId(TransCode.findByCode(log.getTransId()).getValue());
                    log.setTransChnl(TransChnlEnum.findByCode(log.getTransChnl()).getName());
                    log.setPriBId(SpecAccountTypeEnum.findByBId(log.getPriBId()).getName());
                }
            }
            resultMap.put("pageInfo", pageList);
        } catch (Exception e) {
            logger.error("## 查询账户余额明细列表异常");
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "查询账单明细列表异常");
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> uploadImage(FTPImageVo imgVo, MultipartFile file) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Boolean.TRUE);
        if (file == null) {
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", ExceptionEnum.ImageNews.ImageNews03.getMsg());
            return resultMap;
        }
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
            if (!flag) {
                resultMap.put("status", Boolean.FALSE);
                resultMap.put("msg", ExceptionEnum.ImageNews.ImageNews02.getMsg());
            }
            ftpUtil.ftpCloseConnect(ftpClient); // 关闭ftp连接
        } catch (Exception e) {
            logger.error("## 图片上传异常", e);
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", ExceptionEnum.ImageNews.ImageNews02.getMsg());
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> uploadImageName(FTPImageVo imgVo, MultipartFile file) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Boolean.TRUE);
        if (file == null) {
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", ExceptionEnum.ImageNews.ImageNews03.getMsg());
            return resultMap;
        }
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
                resultMap.put("msg", imgVo.getService() + newPath + newFilename);
            }
        } catch (Exception e) {
            logger.error("## 图片上传异常", e);
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", ExceptionEnum.ImageNews.ImageNews02.getMsg());
        } finally {
            ftpUtil.ftpCloseConnect(ftpClient); // 关闭ftp连接
        }

        return resultMap;
    }

    @Override
    public void deleteImage(FTPImageVo imgVo, String fileName) {
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
    public Map<String, Object> isFileExsits(FTPImageVo imgVo, String fileName) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
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
        resultMap.put("status", flag);
        return resultMap;
    }

    @Override
    public Map<String, Object> uploadImage(MultipartFile file, HttpServletRequest request, String imgType, String orderId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Boolean.TRUE);
        try {
            String originalFilename = file.getOriginalFilename();//获取file图片名称
            String endFileName = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!endFileName.equalsIgnoreCase(".jpg") && !endFileName.equalsIgnoreCase(".jpeg") && !endFileName.equalsIgnoreCase(".bmp") && !endFileName.equalsIgnoreCase(".gif") && !endFileName.equalsIgnoreCase(".png")) {
                resultMap.put("status", Boolean.FALSE);
                resultMap.put("msg", "文件类型不正确");
                return resultMap;
            }

            String imgPath= IMG_PATH + imgType + "/";
            String newFileName = orderId + endFileName;
            String realPath = imgPath + newFileName;
            File dest = new File(imgPath);
            if(!dest.exists()){
                dest.mkdir();
            }
            FileOutputStream out = new FileOutputStream(realPath);
            out.write(file.getBytes());
            out.flush();
            out.close();
            resultMap.put("msg", realPath);
        } catch (Exception e) {
            logger.error("## 文件上传异常", e);
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "文件上传异常");
            return resultMap;
        }
        return resultMap;
    }

    @Override
    public String getImageStrFromPath(String imgPath) {
        InputStream in = null;
        byte[] data = null;

        // 读取图片字节数组 
        try {
            in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            logger.error("## 图片读取失败{}", imgPath);
            return null;
        }
        // 对字节数组Base64编码  
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串  
        return encoder.encode(data);
    }

}
