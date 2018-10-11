package org.clearfuny.funnytest.util;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    public final static String RET_CODE = "code";

    public final static String RET_STRING = "content";

    public final static String RET_HEADERS = "headers";

    public final static String RET_DURATION = "duration";

    public static Map<String, Object> postJSON(String url, String data, Map<String, String> headers) throws IOException {

        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(data, "UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);

        if (headers != null) {
	        /* 设置headers */
            for (String key : headers.keySet()) {
                httpPost.setHeader(key, headers.get(key));
            }
        }
        CloseableHttpClient client = HttpClients.createDefault();
        return doExecute(client, httpPost);
    }

    public static Map<String, Object> post(String url, Map<String, String> data, Map<String, String> headers) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();

        if (headers != null) {
	        /* 设置headers */
            for (String key : headers.keySet()) {
                httpPost.setHeader(key, headers.get(key));
            }
        }

        for (String key : data.keySet()) {
            formparams.add(new BasicNameValuePair(key, data.get(key)));
        }
        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httpPost.setEntity(uefEntity);

        CloseableHttpClient client = HttpClients.createDefault();

        return doExecute(client,httpPost);
    }


    /**
     * get请求
     * @param url
     * @param headers
     * @return
     * @throws IOException
     */
    public static Map<String, Object> get(String url, Map<String, String> headers) throws IOException  {
        HttpGet httpget = new HttpGet(url);
        CloseableHttpClient client = HttpClients.createDefault();

        if (headers != null) {
	        /* 设置headers */
            for (String key : headers.keySet()) {
                httpget.setHeader(key, headers.get(key));
            }
        }

        return doExecute(client, httpget);
    }


    /**
     * 获取http返回的内容
     * @param url
     * @param headers
     * @return
     * @throws IOException
     */
    public static String getContent(String url, Map<String, String> headers) throws IOException {
        Map<String, Object> res = get(url, headers);
        return (String) res.get(RET_STRING);
    }

    public static Map<String, Object> delete(String url, Map<String, String> headers) throws IOException {

        HttpDelete httpDelete = new HttpDelete(url);
        CloseableHttpClient client = HttpClients.createDefault();

        if (headers != null) {
	        /* 设置headers */
            for (String key : headers.keySet()) {
                httpDelete.setHeader(key, headers.get(key));
            }
        }

        return doExecute(client, httpDelete);
    }

    public static Map<String, Object> put(String url, Map<String, String> data, Map<String, String> headers) throws IOException {
        HttpPut httpPut = new HttpPut(url);
        CloseableHttpClient client = HttpClients.createDefault();
        if (headers != null) {
	        /* 设置headers */
            for (String key : headers.keySet()) {
                httpPut.setHeader(key, headers.get(key));
            }
        }

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();

        for (String key : data.keySet()) {
            formparams.add(new BasicNameValuePair(key, data.get(key)));
        }
        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httpPut.setEntity(uefEntity);

        return doExecute(client, httpPut);
    }

    /*========================================[私有方法区]==========================================*/

    private static Map<String, Object> doExecute(CloseableHttpClient client, HttpUriRequest request) throws IOException {
        try {
            long startTime=System.currentTimeMillis();

            HttpResponse response = client.execute(request);

            long endTime=System.currentTimeMillis();
            long duration = endTime-startTime;
            return parse(response, duration, "UTF-8");
        }finally {
            if (client != null) client.close();
        }
    }

    private static Map<String, Object> parse(HttpResponse response, long duration,String charset) throws ParseException, IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        Map<String, Object> res = new HashMap<String, Object>();
        HttpEntity entity = response.getEntity();
        String resp = EntityUtils.toString(entity, charset);
        res.put(RET_CODE, statusCode);
        res.put(RET_STRING, resp);
        res.put(RET_HEADERS, response.getAllHeaders());
        res.put(RET_DURATION, duration);

        return res;
    }
}