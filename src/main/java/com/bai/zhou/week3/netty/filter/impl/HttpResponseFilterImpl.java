package com.bai.zhou.week3.netty.filter.impl;

import com.bai.zhou.week3.netty.filter.HttpResponseFilter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;

@Slf4j
public class HttpResponseFilterImpl implements HttpResponseFilter {
    @Override
    public void filter(CloseableHttpResponse response) {
        // 记录请求失败
        if (response.getStatusLine().getStatusCode() != 200) {
            log.error("请求失败,status:{}", response.getStatusLine().getStatusCode());
        }
        response.addHeader("kk", "java-1-nio");
    }
}
