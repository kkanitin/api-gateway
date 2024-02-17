package com.kanitin.api_gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Log4j2
@AllArgsConstructor
@NoArgsConstructor
@Service
public class JwtService {

    @Value("${application.jwt.app-name}")
    private String appName;
    @Value("${application.jwt.public-key}")
    private String publicKeyFile;

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

    public Claims getClaims(String token) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        return Jwts.parser().verifyWith(getPublicKey()).build().parseSignedClaims(token).getPayload();
    }

    public boolean verifyJwtToken(String authToken) {
        try {
            if (ObjectUtils.isEmpty(authToken)) {
                return false;
            }
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
