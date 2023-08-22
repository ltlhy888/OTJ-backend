package com.ltech.bidding.util;

import com.ltech.bidding.service.dto.auth.JwtTokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;

@Component
public class JwtTokenProvider {
    @Value("${jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${jwt.accessTokenExpirationMs}")
    private int jwtExpirationMs;

    @Value("${jwt.refreshTokenExpirationMs}")
    private int refreshTokenExpirationMs;

    private static final Logger logger = LogManager.getLogger(JwtTokenProvider.class);

    public JwtTokenDto generateAccessToken(String id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return  generateToken(expiryDate,id);
    }

    public JwtTokenDto generateRefreshToken(String id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMs);

        return generateToken(expiryDate,id);
    }

    public JwtTokenDto generateToken(Date expiryDate, String id) {
        return new JwtTokenDto(Jwts.builder()
                .setSubject(id)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact(), expiryDate);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String getUserIdFromToken(String token) {
        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret).build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            // Invalid JWT token
            logger.error(ex);
        } catch (ExpiredJwtException ex) {
            // Expired JWT token
            logger.error(ex);
        } catch (UnsupportedJwtException ex) {
            // Unsupported JWT token
            logger.error(ex);
        } catch (IllegalArgumentException ex) {
            // JWT claims string is empty
            logger.error(ex);
        }
        return false;
    }
}
