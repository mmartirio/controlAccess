package com.example.controlAccess.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component

public class JwtUtil {

    // Chave secreta para assinatura do token (deve ser segura e gerada adequadamente)
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tempo de expiração do token (1 hora)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // Método para gerar o token JWT
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    // Método para extrair os claims (informações) do token
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Método para verificar se o token está expirado
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Método para validar o token
    public boolean validateToken(String token, String username) {
        String tokenUsername = extractClaims(token).getSubject();
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }
}
