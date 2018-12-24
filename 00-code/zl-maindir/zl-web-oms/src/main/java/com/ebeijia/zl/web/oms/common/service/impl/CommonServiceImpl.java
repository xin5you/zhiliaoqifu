package com.ebeijia.zl.web.oms.common.service.impl;

import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.account.req.AccountQueryReqVo;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import com.ebeijia.zl.facade.account.vo.AccountLogVO;
import com.ebeijia.zl.web.oms.common.service.CommonService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service("commonService")
public class CommonServiceImpl implements CommonService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AccountQueryFacade accountQueryFacade;

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
            reqVo.setUserType(UserType.TYPE300.getCode());
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
        PageInfo<AccountLogVO> pageList = accountQueryFacade.getAccountLogPage(startNum, pageSize, reqVo);
            resultMap.put("pageInfo", pageList);
        } catch (Exception e) {
            logger.error("## 查询账户余额列表异常");
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "查询账户余额列表异常");
            return resultMap;
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> saveFile(MultipartFile file, HttpServletRequest request, String orderId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Boolean.TRUE);
        try {
            String fileName = file.getOriginalFilename();//获取file图片名称
            String endFileName = fileName.substring(fileName.lastIndexOf("."));
            if (!endFileName.equalsIgnoreCase(".jpg") && !endFileName.equalsIgnoreCase(".jpeg") && !endFileName.equalsIgnoreCase(".bmp") && !endFileName.equalsIgnoreCase(".gif") && !endFileName.equalsIgnoreCase(".png")){
                resultMap.put("status", Boolean.FALSE);
                resultMap.put("msg", "文件类型不正确");
                return resultMap;
            }
            // 文件保存路径
            String filePath = request.getSession().getServletContext().getRealPath("images/");//本地项目路径

            File targetFile = new File(filePath);
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(filePath + orderId + endFileName);
            out.write(file.getBytes());
            out.flush();
            out.close();
            resultMap.put("msg", filePath + orderId + endFileName);
        } catch (Exception e) {
            logger.error("## 文件上传异常");
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "文件上传异常");
            return resultMap;
        }
        return resultMap;
    }
}
