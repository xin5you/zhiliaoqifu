package com.ebeijia.zl.payment.fx;


import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {
    static final String USER_AGENT = "Mozilla/5.0";
    static String POST_URL = "https://m.dianpayer.com/gateway.do";

    private static PoolingHttpClientConnectionManager cm;


    private HttpUtils() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContextBuilder builder = SSLContexts.custom();
        builder.loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                return true;
            }
        });
        SSLContext sslContext = builder.build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslContext, new X509HostnameVerifier() {
            @Override
            public void verify(String host, SSLSocket ssl)
                    throws IOException {
            }


            @Override
            public void verify(String host, X509Certificate cert)
                    throws SSLException {
            }

            @Override
            public void verify(String host, String[] cns,
                               String[] subjectAlts) throws SSLException {
            }

            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create().register("https", sslsf)
                .build();

        cm = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);


    }


    /**
     * http 请求
     *
     * @param url     http/https 开头
     * @param request 请求参数
     * @param outSb   输出参数
     * @return 200 返回正常
     */
    public static String sendRequest(String url, Map<String, String> request, StringBuilder outSb) {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        BufferedReader reader = null;
        int ret = 0;
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>(request.size());
            for (Map.Entry<String, String> v : request.entrySet()) {
                urlParameters.add(new BasicNameValuePair(v.getKey(), v.getValue()));
            }

            HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
            httpPost.setEntity(postParams);
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

            reader = new BufferedReader
                    (new InputStreamReader(httpResponse.getEntity().getContent()));
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                outSb.append(inputLine);
            }
            reader.close();
            ret = httpResponse.getStatusLine().getStatusCode();
        } catch (IOException e) {
            //通讯异常
            ret = -1;
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException ignore) {
                }
            }
            try {
                httpClient.close();
            } catch (IOException ignore) {
            }
        }
        if (ret != -1) {
            return outSb.toString();
        } else {
            return null;
        }
    }


}
