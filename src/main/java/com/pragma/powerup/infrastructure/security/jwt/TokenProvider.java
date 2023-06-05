package com.pragma.powerup.infrastructure.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class TokenProvider {
    private final static Logger LOGGER = LoggerFactory.getLogger(JwtEntryPoint.class);

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid token");
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Unsupported token");
        } catch (ExpiredJwtException e) {
            LOGGER.error("Expired token");
        } catch (IllegalArgumentException e) {
            LOGGER.error("Empty token");
        } catch (SignatureException e) {
            LOGGER.error("Invalid signature");
        }
        return false;
    }
}
