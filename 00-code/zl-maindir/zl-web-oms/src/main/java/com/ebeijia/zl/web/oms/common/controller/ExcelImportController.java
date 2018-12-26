package com.ebeijia.zl.web.oms.common.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.ebeijia.zl.web.oms.utils.OrderConstants;
import com.ebeijia.zl.web.oms.utils.XlsReadFile;

@Controller
@RequestMapping(value = "common/excelImport")
public class ExcelImportController {

	Logger logger = LoggerFactory.getLogger(ExcelImportController.class);
	
	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;

	@RequestMapping(value = "/excelImp")
	@ResponseBody
	public ModelMap importEcxel(HttpServletRequest req, HttpServletResponse response, @RequestParam(value = "file", required = false)MultipartFile file) {
		LinkedList<BatchOrderList> orderList = new LinkedList<BatchOrderList>();
		Map<String, BatchOrderList> orderMap = new LinkedHashMap<String, BatchOrderList>();
		ModelMap map = null;
		if (file == null) {
			map = new ModelMap();
			map.addAttribute("status", Boolean.FALSE);
			map.addAttribute("msg", "请选择上传文件！！！");
			return map;
		}
		String batchType = req.getParameter("batchType");
		try {
			CommonsMultipartFile cf = (CommonsMultipartFile) file;
			DiskFileItem fileItem = (DiskFileItem) cf.getFileItem();
			InputStream inputStream = fileItem.getInputStream();

			XlsReadFile xls = new XlsReadFile();
			map = xls.readOrderExcel(inputStream, file.getOriginalFilename(), orderMap, batchType);

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
			jedisClusterUtils.setex(OrderConstants.openAccountSession, JSON.toJSONString(orderList),
					1800);
		}
		if (batchType.equals("recharge")) {
			jedisClusterUtils.setex(OrderConstants.rechargeSession, JSON.toJSONString(orderList),
					1800);
		}
		return map;
	}
}
