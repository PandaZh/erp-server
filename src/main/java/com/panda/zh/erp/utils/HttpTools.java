package com.panda.zh.erp.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Panda.Z
 * @since http-client 4.3
 */
public class HttpTools {

    private static Logger logger = LoggerFactory.getLogger(HttpTools.class);

    private static final CloseableHttpClient HTTP_CLIENT;

    private static final String UTF_8 = "UTF-8";

    private static SSLSocketFactory ssf;

    /**
     * 忽视证书HostName
     */
    private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String s, SSLSession sslsession) {
            return true;
        }
    };

    /**
     * 忽视证书 Certification
     */
    private static TrustManager ignoreCertificationTrustManger = new X509TrustManager() {
        private X509Certificate[] certificates;

        @Override
        public void checkClientTrusted(X509Certificate[] certificates, String authType)
                throws CertificateException {
            if (this.certificates == null) {
                this.certificates = certificates;
            }

        }

        @Override
        public void checkServerTrusted(X509Certificate[] ax509certificate, String s)
                throws CertificateException {
            if (this.certificates == null) {
                this.certificates = ax509certificate;
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    };

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        HTTP_CLIENT = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

        HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);

        // Prepare SSL Context
        try
        {
            TrustManager[] tm = {ignoreCertificationTrustManger};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            ssf = sslContext.getSocketFactory();
        }
        catch (Exception e)
        {
        }
    }

    public static String doGet(String url, Map<String, String> params) {
        return doGet(url, params, UTF_8);
    }

    public static String doGet(String url, Map<String, String> params, String charset) {
        if (Strings.isNullOrEmpty(url)) {
            return null;
        }
        try {
            List<NameValuePair> pairs = buildPair(params);
            if (pairs.size() > 0) {
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);
            return execute(httpGet);
        } catch (Exception e) {
            logger.error("HttpClient request error : {}", e.getMessage(), e);
        }
        return null;
    }

    public static String doGetByCookie(String url, Map<String, String> params, Map<String, String> cookie) {
        if (Strings.isNullOrEmpty(url)) {
            return null;
        }
        try {
            List<NameValuePair> pairs = buildPair(params);
            if (pairs.size() > 0) {
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, UTF_8));
            }
            HttpGet httpGet = new HttpGet(url);
            if (null != cookie) {
                StringBuilder cookieBuf = new StringBuilder();
                for (Entry<String, String> entry : cookie.entrySet()) {
                    cookieBuf.append(entry.getKey())
                            .append("=")
                            .append(entry.getValue())
                            .append(";");
                }
                httpGet.setHeader("Cookie", cookieBuf.toString());
            }
            return execute(httpGet);
        } catch (Exception e) {
            logger.error("HttpClient request error : {}", e.getMessage(), e);
        }
        return null;
    }


    public static String doPost(String url, Map<String, String> params) {
        return doPost(url, params, UTF_8);
    }

    public static String doPost(String url, Map<String, String> params, String charset) {
        if (Strings.isNullOrEmpty(url)) {
            return null;
        }
        try {
            List<NameValuePair> pairs = buildPair(params);
            HttpPost httpPost = new HttpPost(url);
            if (pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
            }
            return execute(httpPost);
        } catch (Exception e) {
            logger.error("HttpClient request error : {}", e.getMessage(), e);
        }
        return null;
    }

    public static String doPostByJson(String url, String param) {
        if (Strings.isNullOrEmpty(url)) {
            return null;
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            if (!Strings.isNullOrEmpty(param)) {
                httpPost.setEntity(new StringEntity(param, ContentType.APPLICATION_JSON));
            }
            return execute(httpPost);
        } catch (Exception e) {
            logger.error("HttpClient request error : {}", e.getMessage(), e);
        }
        return null;
    }

    public static String doPostByJsonUrlencoded(String url, String param) {
        if (Strings.isNullOrEmpty(url)) {
            return null;
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            if (!Strings.isNullOrEmpty(param)) {
                httpPost.setEntity(new StringEntity(param, ContentType.APPLICATION_FORM_URLENCODED));
            }
            return execute(httpPost);
        } catch (Exception e) {
            logger.error("HttpClient request error : {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 通过HttpURLConnection POST请求 (包括http和https请求)
     * @param url
     * @param params
     * @return
     */
    public static String doPostByConnection(String url,  String params) {
        return doPostByConnection(url, params, UTF_8);
    }

    /**
     *  通过HttpURLConnection POST请求
     * @param url
     * @param params
     * @param encoding
     * @return
     */
    public static String doPostByConnection(String url,String params, String encoding) {
        StringBuffer resultSb = new StringBuffer();
        HttpURLConnection urlCon = null;
        BufferedReader in = null;
        try {

            URL dataUrl = new URL(url);
            urlCon = (HttpURLConnection) dataUrl.openConnection();
            if (url.indexOf("https://") >= 0) {
                ((HttpsURLConnection)urlCon).setSSLSocketFactory(ssf);
            }
//            String dataSb = null;
//            List<NameValuePair> pairs = buildPair(params);
//            if (pairs.size() > 0) {
//                dataSb =  EntityUtils.toString(new UrlEncodedFormEntity(pairs, encoding));
//            }
            urlCon.setRequestMethod("POST");
            urlCon.setRequestProperty("content-type", "text/json");
            urlCon.setRequestProperty("Proxy-Connection", "Keep-Alive");
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setConnectTimeout(60000);
            urlCon.setReadTimeout(15000);
            urlCon.getOutputStream().write(params.getBytes(encoding));
            urlCon.getOutputStream().flush();
            InputStream is = urlCon.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,encoding));
            String line = null;
            while ((line = reader.readLine()) != null) {
                resultSb.append(line + "\r\n");
            }
            return resultSb.toString();
        } catch(Exception e) {
            logger.error("HttpUrlConnection request error : {}", e.getMessage(), e);
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (urlCon != null) {
                    urlCon.disconnect();
                }
            } catch (Exception e) {
                logger.error("HttpUrlConnection request error : {}", e.getMessage(), e);
                return null;
            }
        }
    }

    private static List<NameValuePair> buildPair(Map<String, String> params) {
        List<NameValuePair> pairs = Lists.newArrayList();
        if (null != params && !params.isEmpty()) {
            for (Entry<String, String> entry : params.entrySet()) {
                if (!Strings.isNullOrEmpty(entry.getKey()) && !Strings.isNullOrEmpty(entry.getValue())) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
        }
        return pairs;
    }

    private static String execute(HttpUriRequest requestMethod) {
        CloseableHttpResponse response = null;
        try {
            RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
            response = HttpClientBuilder.create().setDefaultRequestConfig(config).build().execute(requestMethod);
//            response = HTTP_CLIENT.execute(requestMethod);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String result = null == entity ? null : EntityUtils.toString(entity, UTF_8);
                EntityUtils.consume(entity);
                return result;
            } else {
                requestMethod.abort();
                logger.error("HttpClient response error status code : {} , reason phrase : {} !", status, response.getStatusLine().getReasonPhrase());
                return null;
            }
        } catch (Exception e) {
            logger.error("HttpClient request error : {}", e.getMessage(), e);
        } finally {
            try {
                if (null != response)
                    response.close();
            } catch (IOException e) {
                logger.error("HttpClient response close error : {}", e.getMessage(), e);
            }
        }
        return null;
    }
}
