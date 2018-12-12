package com.ebeijia.zl.web.oms.common.controller;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;

@Controller
@RequestMapping(value = "common/excelDownload")
public class ExcelUploadController {

	Logger logger = LoggerFactory.getLogger(ExcelUploadController.class);
	
	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;

	@RequestMapping(value = "/excelUpload")
	public void excelUpload(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String batchType = req.getParameter("batchType");
			String path = null;
			/*if (batchType.equals("openAccount")) {
				path = RedisDictProperties.getInstance().getdictValueByCode("OMS_BATCH_OPEN_ACCOUNT_EXCEL_PATH");
			}
			if (batchType.equals("recharge")) {
				path = RedisDictProperties.getInstance().getdictValueByCode("OMS_BATCH_RECHARGE_EXCEL_PATH");
			}*/
			if (batchType.equals("speOpenAccount")) {
				path = jedisClusterUtils.hget("TB_BASE_DICT_KV", "OMS_SPE_BATCH_OPEN_ACCOUNT_EXCEL_PATH");
			}
			if (batchType.equals("speRecharge")) {
				path = jedisClusterUtils.hget("TB_BASE_DICT_KV", "OMS_SPE_BATCH_RECHARGE_EXCEL_PATH");
			}
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
			if (newFileName.equals("batchOpenAccount") || newFileName.equals("speBatchOpenAccount")) {
				newFileName = "批量开户模板";
			}
			if (newFileName.equals("batchRecharge") || newFileName.equals("speBatchRecharge")) {
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
	}
}