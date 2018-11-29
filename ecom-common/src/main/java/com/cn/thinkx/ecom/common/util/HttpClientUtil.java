package com.cn.thinkx.ecom.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpClientUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * HttpClient GET方式请求，返回json
	 * 
	 * @param url
	 * @return
	 */
	public static String sendGet(String url) {
		CloseableHttpClient client = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000) // 设置连接超时时间
				.setConnectionRequestTimeout(10000) // 设置请求超时时间
				.setSocketTimeout(10000).setRedirectsEnabled(true)// 默认允许自动重定向
				.build();
		
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader(new BasicHeader("Accept", "application/json;charset=UTF-8"));
		httpGet.setConfig(requestConfig);
		
		String result = null;
		try {
			CloseableHttpResponse response = client.execute(httpGet);
			// 判断网络连接状态码是否正常(0--200都数正常)
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
			response = null;
		} catch (ClientProtocolException e) {
			logger.error("## HttpClient sendGet ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("## HttpClient sendGet IOException", e);
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				logger.error("## Close HttpClient Exception", e);
			}
		}
		return result;
	}

	/**
	 * HttpClient POST方式以JSON参数请求
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public static String sendPost(String url, String data) {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000) // 设置连接超时时间
				.setConnectionRequestTimeout(10000) // 设置请求超时时间
				.setSocketTimeout(6000).setRedirectsEnabled(true)// 默认允许自动重定向
				.build();
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);
		String result =null;
		try {
			StringEntity s = new StringEntity(data, Charset.forName("utf-8"));
			s.setContentType("application/json; charset=UTF-8");
			s.setContentEncoding("UTF-8");
			post.setEntity(s);
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				 result = EntityUtils.toString(res.getEntity());// 返回json格式：
			}
			res = null;
		} catch (Exception e) {
			logger.error("## Http send post Exception", e);
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				logger.error("## Close HttpClient Exception", e);
			}
		}
		return result;
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGetKeyAndValue(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			// Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// System.out.println(key + "--->" + map.get(key));
			// }
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是
	 *            http://192.168.1.140:8000/GetOrderStatus/parms1/parma2的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGetUrl(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPostKeyAndValue(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		
		/*TransferOrders order = new TransferOrders();
		order.setSerialNo("Z599028298");
		order.setReceiverName("韩振环");
		order.setReceiverCardNo("6222081001030751691");
		order.setReceiverType("PERSON");
		order.setBankName("中国工商银行");
		order.setBankCode("ICBC");
//		order.setPayeeBankLinesNo("");
		order.setBankProvince("重庆市");
		order.setBankCity("重庆市");
		order.setId(Long.parseLong("1806220000001682200"));
		order.setAmount(Long.parseLong("48"));
		order.setPoundage(Long.parseLong("50"));
		order.setSuccess(false);
//		order.setPayTime("");
		order.setErrMessage("发送结果:（A00001）收款账号、户名不符");
		order.setRefundTicket("N");
		List<TransferOrders> orders = new ArrayList<>();
		orders.add(order);
		Content content = new Content();
		content.setBatchNo("HQ2002860020");
		content.setMerchantNo("70220031");
		content.setDataSource("00");
		content.setTotalNum(1);
		content.setTotalAmount(Long.parseLong("48"));
		content.setSuccessNum(0);
		content.setSuccessAmount(Long.parseLong("0"));
		content.setFailNum(1);
		content.setFailAmount(Long.parseLong("48"));
		content.setPoundage(Long.parseLong("50"));
		content.setTransferOrders(orders);
		content.setStatus("07");*/
//		content.setErrorCode("");
//		content.setErrorMsg("");
		/*WithdrawOrder withdrawOrder = new WithdrawOrder();
		withdrawOrder.setSign("WXLmjyxiFgR7hVkKgEAfd9Lkn_Ex6j7FfK4J8xZtOM3suklEpXDjmw8mspFRTZG4HGp7YyUnTIIdbtfI2m7IYtYFNmuws1Ig3TzuSb92SwAHYUIgMoEp5QNqPvobPaB2hWu-iExvYIPQVKwlIkfPiZfiQUlxCfpHQg1r1m0Ib5M");
		withdrawOrder.setContent(content);
		withdrawOrder.setSign_type("RSA");
		withdrawOrder.setVk_version("0");
		JSONObject json = new JSONObject();
		json.put("content", content);
		json.put("sign", "WXLmjyxiFgR7hVkKgEAfd9Lkn_Ex6j7FfK4J8xZtOM3suklEpXDjmw8mspFRTZG4HGp7YyUnTIIdbtfI2m7IYtYFNmuws1Ig3TzuSb92SwAHYUIgMoEp5QNqPvobPaB2hWu-iExvYIPQVKwlIkfPiZfiQUlxCfpHQg1r1m0Ib5M");
		json.put("sign_type", "RSA");
		json.put("vk_version", "0");*/
		
		/*String url = "http://192.168.1.201:10701/withdraw/welfaremart/resellCommit.html";
		String data = "{\"productCode\":\"444444\",\"bankNo\":\"6222620110037558799\",\"resellNum\":\"1\",\"userId\":\"2018020213144200000941\",\"sign\":\"9B28D21B6786AA84B69277738FEBA249\"}";
		System.out.println(sendPost(url, data));*/
		/*
		 * String url =
		 * "http://115.29.189.195:8088/ecshop-server/system/accessSign";
		 * JSONObject json = new JSONObject(); json.put("sign",
		 * "537B7BD8E15C2F850B872809592E22A656846A0C"); json.put("timestamp",
		 * "1524577794208"); json.put("appId", "APP2018012300004");
		 * json.put("data",
		 * "C48A5C4CB1F9BDA3435CF0E7486B4484E698118A152D01872DCDB0E8AB31CA546F7815AC8778C348719CE61757350408A485FE0C94DFA96FC6B18E2A9C746D3B2D3A6614BD95839C4F1F31E340B0F211"
		 * ); json.put("randomKey", "123456"); String str = sendPost(url,
		 * json.toJSONString()); System.out.print(str);
		 */

//		String url = "http://gw.hzdingchi.com/longan-supply-getway/unicomAync/buy.do?userId=93&itemId=13479&uid=18251935885&serialno=aynctest275&dtCreate=20160307141626&sign=3525add5a4b3269a3665c512ab396481";
//		System.out.println(sendGet(url));
		
	}

}
