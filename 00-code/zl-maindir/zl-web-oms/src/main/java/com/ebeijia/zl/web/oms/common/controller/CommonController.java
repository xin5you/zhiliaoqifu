package com.ebeijia.zl.web.oms.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.web.oms.common.util.OrderConstants;
import com.ebeijia.zl.web.oms.common.util.XlsReadFile;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;

@Controller
@RequestMapping(value = "common")
public class CommonController {

	Logger logger = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;

	@Value("${EXCEL_OPEN_ACCOUNT}")
	private String EXCEL_OPEN_ACCOUNT;

	@Value("${EXCEL_RECHARGE_ACCOUNT}")
	private String EXCEL_RECHARGE_ACCOUNT;

	/**
	 * 模板下载
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/excelDownload")
	public void excelDownload(HttpServletRequest req, HttpServletResponse resp) {
		String batchType = req.getParameter("batchType");
		if (StringUtil.isNullOrEmpty(batchType)) {
			logger.error("## 模板下载异常，参数batchType为空");
			return;
		}
		FileInputStream inputStream = null;
		ServletOutputStream outputStream = null;
		String  path = null;
		try {
			if (batchType.equals("openAccount")) {
				path = req.getServletContext().getRealPath(EXCEL_OPEN_ACCOUNT);
			}
			if (batchType.equals("recharge")) {
				path = req.getServletContext().getRealPath(EXCEL_RECHARGE_ACCOUNT);
			}
			File file = new File(path);
			//获取文件全名，例：batchOpenAccount.xlsx
			String fileName = file.getName();
			//获取文件名称，例：batchOpenAccount
			String newFileName = fileName.substring(0, fileName.indexOf("."));
			//获取文件名后缀，例：.xlsx
			String endFileName = fileName.substring(fileName.lastIndexOf("."));
			if (newFileName.equals("batchOpenAccount")) {
				newFileName = "批量开户模板";
			}
			if (newFileName.equals("batchRecharge")) {
				newFileName = "批量充值模板";
			}
			fileName = newFileName + endFileName;
			inputStream = new FileInputStream(file);
			// 设置response参数，可以打开下载页面
			resp.reset();
			resp.setContentType("application/vnd.ms-excel;charset=utf-8");
			String agent = req.getHeader("USER-AGENT").toLowerCase();
			//游览器兼容  不中文乱码
			if(agent.indexOf("firefox")>0) {
				resp.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("utf-8"),"ISO8859-1"));
			}else {
				resp.setHeader("Content-disposition","attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			}
			outputStream = resp.getOutputStream();
			IOUtils.copy(inputStream, outputStream);
		}catch (Exception e){
			logger.error("## 模板下载异常", e);
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				logger.error("## 下载模板异常", e);
			}
		}
	}

	/**
	 * 模板上传
	 * @param req
	 * @param response
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/excelUpload")
	@ResponseBody
	public ModelMap excelUpload(HttpServletRequest req, HttpServletResponse response, @RequestParam(value = "file", required = false)MultipartFile file) {
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
		if (StringUtil.isNullOrEmpty(batchType)) {
			logger.error("## 模板上传失败，参数batchType为空");
			map.addAttribute("status", Boolean.FALSE);
			map.addAttribute("msg", "网络异常，请稍后再试");
			return map;
		}
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
			map.addAttribute("status", Boolean.FALSE);
			map.addAttribute("msg", "excel数据读取异常，请稍后再试");
			return map;
		}
		if (batchType.equals("openAccount")) {
			jedisClusterUtils.del(OrderConstants.openAccountSession);
			jedisClusterUtils.setex(OrderConstants.openAccountSession, JSON.toJSONString(orderList),
					1800);
		}
		if (batchType.equals("recharge")) {
			jedisClusterUtils.del(OrderConstants.rechargeSession);
			jedisClusterUtils.setex(OrderConstants.rechargeSession, JSON.toJSONString(orderList),
					1800);
		}
		return map;
	}

	/*@RequestMapping(value = "/excelUpload")
	public void excelUpload(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String batchType = req.getParameter("batchType");
			String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

			String path = null;
			path = req.getServletContext().getRealPath("/WEB-INF/excel/batchOpenAccount.xlsx");
			*//*if (batchType.equals("openAccount")) {
				path = jedisClusterUtils.hget("TB_BASE_DICT_KV", "OMS_BATCH_OPEN_ACCOUNT_EXCEL_PATH");
				if (StringUtil.isNullOrEmpty(path)) {
					path = "http://localhost:9998/oms/excel/batchOpenAccount.xlsx";
				}
			}
			if (batchType.equals("recharge")) {
				path = jedisClusterUtils.hget("TB_BASE_DICT_KV", "OMS_BATCH_RECHARGE_EXCEL_PATH");
				if (StringUtil.isNullOrEmpty(path)) {
					path = "http://localhost:9998/oms/excel/batchRecharge.xlsx";
				}
			}*//*
			logger.info("模板下载地址------>>{}", path);
			URL url = new URL(path);
			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();

			urlc.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
			urlc.setRequestProperty("Accept-Language", "zh-CN");
			urlc.setRequestProperty("Charset", "UTF-8");

			InputStream inStream = urlc.getInputStream();

			// 设置浏览器代理信息

			String agent = req.getHeader("USER-AGENT");

			File file = new File(path);
			// // 取得文件名。
			String filename = file.getName();
			String newFileName = filename.substring(0, filename.indexOf("."));
			if (newFileName.equals("batchOpenAccount")) {
				newFileName = "批量开户模板";
			}
			if (newFileName.equals("batchRecharge")) {
				newFileName = "批量充值模板";
			}
			resp.setHeader("content-disposition",
					"attachment;filename=" + new String((newFileName + ".xlsx").getBytes(), "iso-8859-1"));

			// 循环取出流中的数据

			byte[] b = new byte[1024];

			int len;

			while ((len = inStream.read(b)) > 0) {

				resp.getOutputStream().write(b, 0, len);

			}

			inStream.close();

			resp.getOutputStream().close();

			urlc.disconnect();

		} catch (Exception e) {
			logger.error("下载模板出错----------->>" + e.getMessage());
		}
	}*/
}
