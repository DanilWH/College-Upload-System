package com.example.CollegeUploadSystem.utils;

import com.example.CollegeUploadSystem.dto.response.AuthExceptionResponse;
import com.example.CollegeUploadSystem.dto.response.LoginResponse;
import com.example.CollegeUploadSystem.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.access.token.expiration.ms}")
    private Long accessTokenExpirationMs;
    @Value("${jwt.refresh.token.expiration.ms}")
    private Long refreshTokenExpirationMs;

    private final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateAccessJws(User jwsOwner) {
        return this.generateJws(jwsOwner, this.accessTokenExpirationMs);
    }

    public String generateRefreshJws(User jwsOwner) {
        return this.generateJws(jwsOwner, this.refreshTokenExpirationMs);
    }

    public LoginResponse packLoginResponse(User jwsOwner, String accessJws, String refreshJws) {
        Long accessExpiration = this.parseJws(accessJws).getBody().getExpiration().getTime();
        Long refreshExpiration = this.parseJws(refreshJws).getBody().getExpiration().getTime();

        return new LoginResponse()
                .setId(jwsOwner.getId())
                .setLogin(jwsOwner.getLogin())
                .setAccessJws(accessJws)
                .setRefreshJws(refreshJws)
                .setAccessJwsExpirationMs(accessExpiration)
                .setRefreshJwsExpirationMs(refreshExpiration);
    }

    public Jws<Claims> parseJws(String jws) {
        return Jwts.parserBuilder().setSigningKey(this.KEY).build().parseClaimsJws(jws);
    }

    public String parseAuthorizationBearer(HttpServletRequest request) {
        String value = request.getHeader("Authorization");

        if (value != null && value.startsWith("Bearer ")) {
            return value.substring("Bearer ".length());
        }

        throw new RuntimeException("The Authorization header isn't present or Bearer is missing");
    }

    public void sendUnauthorizedResponse(HttpServletResponse response, RuntimeException exception) throws IOException {
        response.setHeader("Error", exception.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        new ObjectMapper().writeValue(response.getOutputStream(), new AuthExceptionResponse(exception.getMessage()));
    }

    private String generateJws(User jwsOwner, Long expirationMs) {
        return Jwts.builder()
                .setSubject(jwsOwner.getLogin())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(this.KEY)
                .compact();
    }
}
