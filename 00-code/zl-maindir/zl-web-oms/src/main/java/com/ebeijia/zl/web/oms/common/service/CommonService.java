package com.ebeijia.zl.web.oms.common.service;

import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface CommonService {

	Map<String, Object> getAccountInfPage(HttpServletRequest req);

	Map<String, Object> saveFile(MultipartFile file, HttpServletRequest request, String orderId);
}
