package com.example.Zomato.ZomatoApplication.services.impl;

import com.example.Zomato.ZomatoApplication.entites.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class JwtService {

    @Value("${JWT.secret.key}")
    private String SECRET_KEY;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserEntity userEntity) {
        return Jwts.builder()
                .subject(userEntity.getId().toString())
                .claim("email", userEntity.getEmail())
                .claim("roles", userEntity.getRole().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60*5*4))
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(UserEntity userEntity) {
        Date sixMonthsLater = Date.from(ZonedDateTime.now().plusMonths(6).toInstant());

        return Jwts.builder()
                .subject(userEntity.getId().toString())
                .issuedAt(new Date())
                .expiration(sixMonthsLater)
                .signWith(getKey())
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims;
    }

    public Long getUserId(Claims claims) {
        return Long.valueOf(claims.getSubject());
    }
}