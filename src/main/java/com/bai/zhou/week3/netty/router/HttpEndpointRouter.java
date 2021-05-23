package com.bai.zhou.week3.netty.router;

import java.util.List;

public interface HttpEndpointRouter {
    String route(List<String> endpoints);
}
