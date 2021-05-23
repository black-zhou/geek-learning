package com.bai.zhou.week3.netty.outbound;

import com.bai.zhou.week3.netty.filter.HttpRequestFilter;
import com.bai.zhou.week3.netty.filter.HttpResponseFilter;
import com.bai.zhou.week3.netty.filter.impl.HttpResponseFilterImpl;
import com.bai.zhou.week3.netty.router.HttpEndpointRouter;
import com.bai.zhou.week3.netty.router.RandomHttpEndpointRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
public class HttpOutboundHandler {
    private final List<String> backendUrls;
    private final ExecutorService proxyService;
    HttpResponseFilter filter = new HttpResponseFilterImpl();
    HttpEndpointRouter router = new RandomHttpEndpointRouter();

    public HttpOutboundHandler(List<String> backends) {
        this.backendUrls = backends.stream().map(this::formatUrl).collect(Collectors.toList());

        int cores = Runtime.getRuntime().availableProcessors();
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();//.DiscardPolicy();
        proxyService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpRequestFilter filter) {
        String backendUrl = router.route(this.backendUrls);
        final String url = backendUrl + fullRequest.uri();
        filter.filter(fullRequest, ctx);
        proxyService.submit(() -> sentGet(fullRequest, ctx, url));
    }

    public String sentGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
        HttpGet request = new HttpGet(url);
        request.setHeader("mao", inbound.headers().get("mao"));
        request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new RuntimeException("http send get exception return null");
            }
            filter.filter(response);
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            log.error("exception:", e);
            throw new RuntimeException("http send get exception", e);
        }
    }

    private String formatUrl(String backend) {
        return backend.endsWith("/") ? backend.substring(0, backend.length() - 1) : backend;
    }
}
