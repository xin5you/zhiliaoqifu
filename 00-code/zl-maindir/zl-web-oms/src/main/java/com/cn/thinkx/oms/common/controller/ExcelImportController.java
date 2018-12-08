package com.cn.thinkx.oms.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.cn.thinkx.oms.specialAccount.util.OrderConstants;
import com.cn.thinkx.oms.specialAccount.util.XlsReadFile;

@Controller
@RequestMapping(value = "common/excelImport")
public class ExcelImportController {

	Logger logger = LoggerFactory.getLogger(ExcelImportController.class);
	
	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;

	@RequestMapping(value = "/excelImp")
	@ResponseBody
	public ModelMap importEcxel(HttpServletRequest req, HttpServletResponse response) {
		LinkedList<SpeAccountBatchOrderList> orderList = new LinkedList<SpeAccountBatchOrderList>();
		Map<String, SpeAccountBatchOrderList> orderMap = new LinkedHashMap<String, SpeAccountBatchOrderList>();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		ModelMap map = null;
		if (multipartFile == null) {
			map = new ModelMap();
			map.addAttribute("status", Boolean.FALSE);
			map.addAttribute("msg", "请选择上传文件！！！");
			return map;
		}
		String batchType = req.getParameter("batchType");
		try {
			CommonsMultipartFile cf = (CommonsMultipartFile) multipartFile;
			if (cf != null && cf.getSize() > 0) {
				DiskFileItem fi = (DiskFileItem) cf.getFileItem();
				File file = fi.getStoreLocation();
				XlsReadFile xls = new XlsReadFile();
				InputStream inputStream = new FileInputStream(file);
				map = xls.readOrderExcel(inputStream, multipartFile.getOriginalFilename(), orderMap, batchType);
			}
			if ((boolean) map.get("status") == false) {
				return map;
			}
			for (Iterator<String> it = orderMap.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				orderList.addLast(orderMap.get(key));
			}
		} catch (Exception e) {
			logger.error(" ##  读取excel数据异常", e);
		}
		if (batchType.equals("openAccount")) {
			jedisClusterUtils.setex(OrderConstants.speBathOpenAccountSession, JSON.toJSONString(orderList),
					1800);
		}
		if (batchType.equals("recharge")) {
			jedisClusterUtils.setex(OrderConstants.speBathRechargeSession, JSON.toJSONString(orderList),
					1800);
		}
		return map;
	}
}
