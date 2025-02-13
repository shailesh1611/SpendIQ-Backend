package com.example.spendiq.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secretKey}")
    String secretKey;

    public String generateToken(String userName) {
        Map<String,Object> claims = new HashMap<>();
        return createToken(userName,claims);
    }

    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(String subject, Map<String,Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .header().add("typ", "JWT")
                .and()
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 Hour
                .signWith(getKey())
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
            return true;
        }
        catch (JwtException e) {
            throw new JwtException("Invalid Token : "+e.getMessage());
        }
    }

    public String extractUserName(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
