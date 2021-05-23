package com.bai.zhou.week3.netty.filter.impl;

import com.bai.zhou.week3.netty.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpRequestFilterImpl implements HttpRequestFilter {
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        log.info("method:{},uri:{}", fullRequest.method(), fullRequest.uri());
        fullRequest.headers().set("mao", "soul");
        if (fullRequest.uri().contains("/test")) {
            throw new RuntimeException("不接收测试请求");
        }
    }
}
