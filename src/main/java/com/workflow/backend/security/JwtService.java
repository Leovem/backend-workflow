package com.workflow.backend.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.workflow.backend.model.User; // 👈 Importa tu modelo de usuario

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String SECRET_STRING = "esta_es_una_clave_secreta_muy_larga_de_32_caracteres";
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());

    // 🚩 CAMBIO: Ahora recibe el objeto User completo
    public String generateToken(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        
        // 🚀 Agregamos información útil para el Frontend
        extraClaims.put("nombre", user.getNombre());
        extraClaims.put("roles", user.getRoles()); // Enviamos la lista ["ADMIN", "USER"]
        extraClaims.put("id", user.getId());

        return Jwts.builder()
                .setClaims(extraClaims) // 👈 Inyectamos los claims adicionales
                .setSubject(user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}