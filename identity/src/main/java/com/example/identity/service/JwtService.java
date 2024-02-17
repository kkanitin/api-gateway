package com.example.identity.service;

import com.example.identity.exception.AuthenticationException;
import com.example.identity.model.request.JwtTokenRequest;
import com.example.identity.model.response.JwtPayload;
import com.nimbusds.jose.util.Pair;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Log4j2
@AllArgsConstructor
@NoArgsConstructor
@Service
public class JwtService {

    @Value("${application.jwt.public-key}")
    private String publicKeyFile;
    @Value("${application.jwt.private-key}")
    private String privateKeyFile;
    @Value("${application.jwt.app-name}")
    private String appName;

    //5 mins
    private static final int ACCESS_EXPIRATION_TIME = 300_000;
    //15 mins
    private static final int REFRESH_EXPIRATION_TIME = 900_000;
    private static final String NEW_LINE = "(\\r\\n|\\r|\\n|\\n\\r)";

    public PublicKey getPublicKey() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        try (InputStream inputStream = ResourceUtils.getURL(publicKeyFile).openStream()) {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            String publicKeyValue = new String(inputStream.readAllBytes());
            publicKeyValue = publicKeyValue.replaceAll(NEW_LINE, "")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyValue));
            return keyFactory.generatePublic(keySpec);
        }
    }

    public PrivateKey getPrivateKey() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        try (InputStream inputStream = ResourceUtils.getURL(privateKeyFile).openStream()) {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            String privateKeyValue = new String(inputStream.readAllBytes());
            privateKeyValue = privateKeyValue.replaceAll(NEW_LINE, "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyValue));
            return keyFactory.generatePrivate(keySpec);
        }
    }

    public Pair<String, String> generateAccessToken(JwtTokenRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        Date now = new Date();

        JwtPayload jwtPayload = JwtPayload.builder()
                .username(request.username())
                //mock
                .roles(List.of("user"))
                .build();

        return Pair.of(
                Jwts.builder()
                        .subject(request.username())
                        .issuer(appName)
                        .claim("claim", jwtPayload)
                        .issuedAt(now)
                        .notBefore(now)
                        .expiration(new Date(now.getTime() + ACCESS_EXPIRATION_TIME))
                        .signWith(getPrivateKey())
                        .compact(),
                Jwts.builder()
                        .subject(request.username())
                        .issuer(appName)
                        .claim("claim", jwtPayload)
                        .issuedAt(now)
                        .notBefore(now)
                        .expiration(new Date(now.getTime() + REFRESH_EXPIRATION_TIME))
                        .signWith(getPrivateKey())
                        .compact());
    }

    public String renewAccessToken(String refreshToken) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        Date now = new Date();
        if (!verifyJwtToken(refreshToken)) {
            throw new AuthenticationException("refresh token is invalid.");
        }
        Claims payload = Jwts.parser().verifyWith(getPublicKey()).build().parseSignedClaims(refreshToken).getPayload();
        return Jwts.builder()
                .subject(payload.getSubject())
                .claim("claim", payload.get("claim"))
                .issuedAt(now)
                .notBefore(now)
                .expiration(new Date(now.getTime() + ACCESS_EXPIRATION_TIME))
                .signWith(getPrivateKey())
                .compact();
    }

    public boolean verifyJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(getPublicKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("jwt expire : {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("error while verify jwt token : {}", ex.getMessage(), ex);
            return false;
        }
    }
}
