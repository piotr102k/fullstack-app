package com.example.pasir_swiszcz_piotr.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final Key key = Keys.hmacShaKeyFor("bezpieczoństwoJestMojaPasja123398247927kejhfdiuadshf222@IUY@#I@U#Y".getBytes());

    public String generateToken(com.example.pasir_swiszcz_piotr.model.User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        long expirationMs=3600000;

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expirationMs))
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token){
        try{
            extractAllClaims(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
