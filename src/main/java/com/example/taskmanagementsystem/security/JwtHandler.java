package com.example.taskmanagementsystem.security;

import com.example.taskmanagementsystem.entity.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtHandler {
    @Value(value = "${jwt.token.secret.key}")
    private String secretKey;
    @Value(value = "${jwt.token.time.expired}")
    private int timeExpired;

    public String jwtGenerationToken(User user) {
        Date nowTime = new Date();
        Date timeDateExpired = new Date(nowTime.getTime() + timeExpired);

        return Jwts.builder()
                .setIssuedAt(nowTime)
                .setExpiration(timeDateExpired)
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();
    }

    public String getUserLoginFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(this.secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        ;
        System.out.println(username);
        return username;
    }

    public boolean tokenValidator(String token) {
        try {
            Jwts.parser().setSigningKey(this.secretKey).build().parse(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Время токена истекло");
        } catch (MalformedJwtException | SignatureException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
