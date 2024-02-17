package com.kanitin.api_gateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static final List<String> WHITELIST_ENDPOINT = List.of(
            "/identity"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> WHITELIST_ENDPOINT
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
