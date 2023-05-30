package com.bsuir.chatroomserver.provider;

import com.bsuir.chatroomserver.exception.JwtExceptions;
import com.bsuir.chatroomserver.repository.user.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    public String generateRegistrationToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateSignInToken(User user) {

        Date expiryDate = this.getExpirationDate();

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());
        claims.put("emailConfirmed", user.isEmailConfirmed());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return (String) claims.get("email");
    }

    public boolean isEmailConfirmedFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return (boolean) claims.get("emailConfirmed");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new JwtExceptions.InvalidJwtTokenException("Invalid JWT token signature");
        } catch (MalformedJwtException ex) {
            throw new JwtExceptions.InvalidJwtTokenException("Invalid JWT token format");
        } catch (ExpiredJwtException ex) {
            throw new JwtExceptions.ExpiredJwtTokenException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtExceptions.UnsupportedJwtTokenException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtExceptions.JwtTokenValidationException("JWT token is empty or null");
        }
    }

    private Date getExpirationDate(){
        Date now = new Date();
        return new Date(now.getTime() + jwtExpirationMs);
    }
}
