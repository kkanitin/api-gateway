package com.kanitin.api_gateway.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
@Log4j2
public class LoggingGlobalFilter implements GlobalFilter {

    private static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";
    private static final String CORRELATION_ID_LOG_VAR_NAME = "correlationId";

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
        log.info("REQUEST ID: " + request.getId());
        log.info("METHOD: " + request.getMethod().name());
        log.info("PATH: " + request.getPath());

        return chain.filter(exchange);
    }
}
