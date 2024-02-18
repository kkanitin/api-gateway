package com.kanitin.api_gateway.filter;

import com.kanitin.api_gateway.config.RouterValidator;
import com.kanitin.api_gateway.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@AllArgsConstructor
public class CustomerGlobalFilter implements GlobalFilter {

    private static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";
    private static final String CORRELATION_ID_LOG_VAR_NAME = "correlationId";

    private final RouterValidator routerValidator;
    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders requestHeaders = request.getHeaders();
        List<String> correlationIdList = requestHeaders.get(CORRELATION_ID_HEADER_NAME);
        String correlationId = UUID.randomUUID().toString();
        if (correlationIdList != null && !correlationIdList.isEmpty()) {
            correlationId = correlationIdList.getFirst();
        }
        exchange.getRequest().mutate().header(CORRELATION_ID_HEADER_NAME, correlationId);

        log.info(CORRELATION_ID_LOG_VAR_NAME + ": " + correlationId);
        log.info("METHOD: " + request.getMethod().name());
        log.info("PATH: " + request.getPath());

        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request)) {
                return this.onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String token = this.getAuthHeader(request).replaceFirst("Bearer ", "");

            if (!jwtService.verifyJwtToken(token)) {
                return this.onError(exchange, HttpStatus.FORBIDDEN);
            }
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").getFirst();
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}
