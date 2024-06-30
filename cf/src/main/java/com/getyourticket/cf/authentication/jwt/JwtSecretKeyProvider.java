package com.getyourticket.cf.authentication.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Getter
@Component
public class JwtSecretKeyProvider {
    private String jwtSecretBase64;

    @PostConstruct
    public void init() {
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        byte[] keyBytes = secretKey.getEncoded();
        jwtSecretBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes);
        System.out.println("Generated JWT Secret Key: " + jwtSecretBase64);
    }

}