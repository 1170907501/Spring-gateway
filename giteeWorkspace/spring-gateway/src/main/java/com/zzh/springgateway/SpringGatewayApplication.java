package com.zzh.springgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRouterLocater(RouteLocatorBuilder builder){
       return builder.routes()
                .route("path_router", r -> r.path("/post")
                        .uri("http://httpbin.org"))
                .build();

    }

    @Bean
    public RouteLocator myRouterLocater2(RouteLocatorBuilder builder){
       /* return builder.routes()
                .route("path_router", r -> r.path("/get")
                        .uri("http://httpbin.org"))
                .build();*/
        return builder.routes()
                .route("path_router", r -> r.path("/baidu")
                        .uri("https://image.baidu.com/"))
                .build();
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("path_route_baidu",
                r -> r.path("/guonei")
                        .uri("https://news.baidu.com/guonei"))
                .build();

        return routes.build();
    }
}
