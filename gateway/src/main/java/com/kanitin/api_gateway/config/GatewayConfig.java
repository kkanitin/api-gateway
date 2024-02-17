package com.kanitin.api_gateway.config;

import com.kanitin.api_gateway.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
@RequiredArgsConstructor
public class GatewayConfig {

    private final AuthenticationFilter authenticationFilter;

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10, 20, 1);
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("MOCK-SERVICE-RATE-LIMIT", route -> route.path("/MOCK-SERVICE-RATE/rate/**")
                        .filters(filter -> {
//                            filter.filter(this.authenticationFilter);
                            filter.rewritePath("/MOCK-SERVICE-RATE/rate", "/rate");
                            filter.requestRateLimiter(rateLimiter -> rateLimiter.setRateLimiter(redisRateLimiter()));
                            return filter;
                        })
                        .uri("lb://MOCK-SERVICE/rate"))

//                .route("MOCK-SERVICE", r -> r.path("/auth/**")
//                        .filters(f -> f.filter(authenticationFilter))
//                        .uri("lb://auth-service"))
                .build();
    }

//    @Bean
//    public RouterFunction<ServerResponse> gatewayRouterFunctionsRewritePath() {
//        return builder.routes().route("rewritepath_route")
//                .GET("/red/**", http("https://example.org"))
//                .before(rewritePath("/red/(?<segment>.*)", "/${segment}"))
//                .build();
//    }
}
