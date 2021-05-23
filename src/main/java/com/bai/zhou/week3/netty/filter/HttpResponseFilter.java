package com.bai.zhou.week3.netty.filter;

import io.netty.handler.codec.http.FullHttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;

public interface HttpResponseFilter {
    void filter(CloseableHttpResponse response);
}
