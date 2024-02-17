package com.example.identity.model.response;

import lombok.Builder;

@Builder
public record CommonResponse(String status, String msg) {
}
