package com.be.config.jwt;


import com.be.entity.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;


@Service
public class JwtTokenProvider {
    @Value("${jwt.jwt-key}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(Authentication authentication, List<Roles> authorities) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpiration);

        //tạo token
        String token = Jwts.builder()
                .setSubject(username) //dựa trên username
                .setIssuedAt(currentDate)//ngày kích hoạt
                .setExpiration(expireDate)//ngày hết hạn
                .claim("authorities", authorities)//role
                .signWith(key, SignatureAlgorithm.HS512)//mã hóa với key
                .compact();

        System.out.println("New token :");
        System.out.println(token);
        return token;
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception exception) {
            throw new AuthenticationCredentialsNotFoundException("not found");
        }

    }

}
