package com.ebeijia.zl.web.oms.common.service.impl;

import com.ebeijia.zl.web.oms.common.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service("commonService")
public class CommonServiceImpl implements CommonService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Map<String, Object> getAccountInfPage(HttpServletRequest req) {

        return null;
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
