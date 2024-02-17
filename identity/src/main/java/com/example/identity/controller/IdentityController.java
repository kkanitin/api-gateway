package com.example.identity.controller;

import com.example.identity.model.request.JwtTokenRequest;
import com.example.identity.model.request.ValidateTokenRequest;
import com.example.identity.model.response.JwtTokenResponse;
import com.example.identity.service.JwtService;
import com.nimbusds.jose.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@AllArgsConstructor
@Getter
@RestController
@RequestMapping("/identity")
public class IdentityController {

    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<JwtTokenResponse> generateJwtToken(@RequestBody @Validated JwtTokenRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        Pair<String, String> jwtToken = jwtService.generateAccessToken(request);

        return ResponseEntity.ok(JwtTokenResponse.builder()
                .accessToken(jwtToken.getLeft())
                .refreshToken(jwtToken.getRight())
                .build());
    }

    @GetMapping("/verify")
    public boolean verifyToken(@RequestBody ValidateTokenRequest request) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        return jwtService.verifyJwtToken(request.accessToken());
    }

}
