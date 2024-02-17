package com.kanitin.api_gateway.response;

import lombok.Builder;

@Builder
public record CommonResponse(String status, String msg) {
}
