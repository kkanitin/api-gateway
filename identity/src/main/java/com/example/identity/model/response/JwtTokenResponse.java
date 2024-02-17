package com.example.identity.model.response;

import lombok.Builder;

@Builder
public record JwtTokenResponse(String accessToken, String refreshToken) {
}
