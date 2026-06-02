package com.diego.futbol.security;



import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.diego.futbol.entity.Users;
import com.diego.futbol.enums.Rol;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class JwtService {
    
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token-expiration}")
    private Long expiration;

    private SecretKey getSigninKey(){
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String generatedToken(Users users){
        Date now = new Date();
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);

        return Jwts.builder()
                .claims(Map.of("userId", users.getUserId(), "role", users.getRole().name()))
                .subject(users.getEmail())
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSigninKey())
                .compact();
    }

    public Boolean validToken(String token){
        try {
            Jwts.parser().verifyWith(getSigninKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            log.error("token invalido" + e.getMessage());
            return false;
        } catch (Exception e){
            log.error("Error al validar el token" + e.getMessage());
            return false;
        }
    }

    public <T> T extractClaims(String token, Function<Claims, T> resolver){
        final Claims claims = Jwts.parser().verifyWith(getSigninKey()).build().parseSignedClaims(token).getPayload();
        return resolver.apply(claims);
    }

    public String extractEmail(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public Long extractUserId(String token){
        return extractClaims(token, claims -> {
            Number id = claims.get("userId", Number.class);
            return id != null ? id.longValue() : null;
        });
    }

    public String extractRol(String token){
        return extractClaims(token, claims -> claims.get("role", String.class));
    }

    public String refreshToken(String token) throws Exception{
        Claims claims;
        try {
            claims = Jwts.parser().verifyWith(getSigninKey()).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            throw new Exception("Token ha expirado" + e.getMessage());
        } catch (JwtException e){
            throw new Exception("El token no es valido" + e.getMessage());
        } catch (Exception e){
            throw new Exception("Un error en servicio" + e.getMessage());
        }
        Users users = new Users();

        users.setUserId(claims.get("userId", Long.class));
        users.setEmail(claims.getSubject());
        users.setRole(Rol.valueOf(claims.get("rol", String.class)));

        return generatedToken(users);
    }
}
