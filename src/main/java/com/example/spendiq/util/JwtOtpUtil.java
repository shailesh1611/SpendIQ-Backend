package com.example.spendiq.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtOtpUtil {

    private final PasswordEncoder passwordEncoder;


    @Value("${jwt.secretKey}")
    String secretKey;

    @Autowired
    public JwtOtpUtil(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public String generateToken(String otp, String email) {
        Map<String,Object> claims = new HashMap<>();
        String encodedOtp = passwordEncoder.encode(otp);
        claims.put("encryptedOtp",encodedOtp);
        return createToken(email,claims);
    }

    private String createToken(String email, Map<String, Object> claims) {
         return Jwts.builder()
                    .claims(claims)
                    .header()
                    .add("typ","jwt")
                    .and()
                    .subject(email)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // 5 Min
                    .signWith(getKey())
                    .compact();
    }

    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
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

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Boolean validateOtp(String token, String otp) {
        try {
            Claims claim = Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
            String encryptedOtp = (String) claim.get("encryptedOtp");
            return passwordEncoder.matches(otp, encryptedOtp);
        }
        catch(JwtException e) {
            log.error("Invalid OTP : {}",e.getMessage());
            throw new JwtException("Invalid OTP");
        }
    }


}
