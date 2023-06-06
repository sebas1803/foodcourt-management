package com.pragma.powerup.infrastructure.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class TokenProvider {
    private final static Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String getEmailFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            LOGGER.error("Error parsing token: {}", e.getMessage());
            throw new JwtException("Invalid token");
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Unsupported token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("Expired token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Empty token: {}", e.getMessage());
        } catch (SignatureException e) {
            LOGGER.error("Invalid signature: {}", e.getMessage());
        }
        return false;
    }
}
