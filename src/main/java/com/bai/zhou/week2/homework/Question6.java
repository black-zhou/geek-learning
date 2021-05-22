package com.bai.zhou.week2.homework;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Question6 {
    public static String sentGet(String url) {
        HttpGet request = new HttpGet(url);
        request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("http send get exception", e);
        }
        return null;
    }

    public static String sendPost(String url, Map<String, Object> params) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        List<NameValuePair> urlParameters = new ArrayList<>();
        params.keySet().forEach(key -> {
            urlParameters.add(new BasicNameValuePair(key, params.get(key).toString()));
        });
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception ex) {
            throw new RuntimeException("http send post exception", ex);
        }
    }

    public static String sendJsonPost(String url, Map<String, Object> params) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        httpPost.addHeader(HttpHeaders.ACCEPT,"application/json");
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE,"application/json");
        String json = JSONObject.toJSONString(params);
        try {
            httpPost.setEntity(new StringEntity(json));
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception ex) {
            throw new RuntimeException("send json post exception", ex);
        }
    }

    public static void main(String[] args) {
        String s = sentGet("http://127.0.0.1:8808/test");
        System.out.println(s);
    }

}
