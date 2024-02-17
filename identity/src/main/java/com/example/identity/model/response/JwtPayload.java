package com.example.identity.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record JwtPayload(String username, List<String> roles) {
}
