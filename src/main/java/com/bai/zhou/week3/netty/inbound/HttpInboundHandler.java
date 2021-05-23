package com.bai.zhou.week3.netty.inbound;

import com.bai.zhou.week3.netty.filter.HttpRequestFilter;
import com.bai.zhou.week3.netty.filter.impl.HttpRequestFilterImpl;
import com.bai.zhou.week3.netty.outbound.HttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {
    private final HttpOutboundHandler handler;
    private final HttpRequestFilter filter = new HttpRequestFilterImpl();

    public HttpInboundHandler(List<String> proxyServer) {
        this.handler = new HttpOutboundHandler(proxyServer);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            handler.handle(fullRequest, ctx, filter);
        } catch (Exception e) {
            log.error("channel read exception", e);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
