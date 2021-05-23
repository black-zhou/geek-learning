package com.bai.zhou.week3.netty;

import com.bai.zhou.week3.netty.inbound.HttpInboundServer;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class NettyServerApplication {
    //作业1：com.bai.zhou.week3.netty.outbound.HttpOutboundHandler
    //作业3：com.bai.zhou.week3.netty.filter
    public static void main(String[] args) {
        String proxyPort = System.getProperty("proxyPort", "8888");
        String proxyServers = System.getProperty("proxyServers", "http://localhost:8801,http://localhost:8802");
        int port = Integer.parseInt(proxyPort);
        HttpInboundServer server = new HttpInboundServer(port, Arrays.asList(proxyServers.split(",")));
        try {
            server.run();
        } catch (Exception ex) {
            log.error("NettyServerApplication exception", ex);
        }
    }
}
