package com.example.identity.model.request;

import lombok.Builder;

@Builder
public record ValidateTokenRequest(String accessToken){
}
