package com.example.identity.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record JwtTokenRequest(@NotBlank String username, @NotBlank String password) {
}
