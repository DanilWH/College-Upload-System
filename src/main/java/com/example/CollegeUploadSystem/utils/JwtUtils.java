package com.example.CollegeUploadSystem.utils;

import com.example.CollegeUploadSystem.dto.output.AuthExceptionOutput;
import com.example.CollegeUploadSystem.dto.output.AuthOutput;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtils {
//    private final Key KEY = Keys.hmacShaKeyFor("fjfasdfaiefhuabvbabbbbealuabauealaeuabfalfuibvlauevlaiufehlauheflubvbvalufueufhhabsdsjsiencdjndhcfuebfbfhdliewiroqiwyrqouiebovbvuwibeiwbeiuvbiwuerhrhrhhruiwfhuiwfehusvhisdufhfhwewfifwiuvbiwubviwufehiusdhvdskakzzaqprlcfhjeieiihbhghehhehfhvhhghghehruiwerubbva".getBytes());
    private final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${jwt.access.token.expiration.ms}")
    private Long accessTokenExpirationMs;
    @Value("${jwt.refresh.token.expiration.ms}")
    private Long refreshTokenExpirationMs;

    private final UserService userService;

    @Autowired
    public JwtUtils(UserService userService) {
        this.userService = userService;
    }

    public String generateAccessJws(User jwsOwner) {
        return this.generateJws(jwsOwner, this.accessTokenExpirationMs);
    }

    public String generateRefreshJws(User jwsOwner) {
        return this.generateJws(jwsOwner, this.refreshTokenExpirationMs);
    }

    public AuthOutput packLoginResponse(User jwsOwner, String accessJws, String refreshJws) {
        Long accessExpiration = this.parseJws(accessJws).getBody().getExpiration().getTime();
        Long refreshExpiration = this.parseJws(refreshJws).getBody().getExpiration().getTime();

        return new AuthOutput()
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

        // TODO: consider to replace AuthExceptionOutput to ApiErrorOutput or just throw a ResponseStatusException.
        new ObjectMapper().writeValue(response.getOutputStream(), new AuthExceptionOutput(exception.getMessage()));
    }

    private String generateJws(User jwsOwner, Long expirationMs) {
        long epochMilli = Instant.now().toEpochMilli();

        return Jwts.builder()
                .setSubject(String.valueOf(jwsOwner.getId()))
                .setIssuedAt(new Date(epochMilli))
                .setExpiration(new Date(epochMilli + expirationMs))
                .signWith(this.KEY)
                .compact();
    }

    public User validateClaims(Claims claims) {
        // extract the necessary claims.
        long userId = Long.parseLong(claims.getSubject());
        long issuedAtSeconds = claims.getIssuedAt().toInstant().getEpochSecond();

        // find the user in the database by the id that has been extracted from the "subject" claim.
        User user = this.userService.findById(userId);

        if (issuedAtSeconds <= user.getLastLogoutTime().toEpochSecond()) {
            // token's invalid
            throw new RuntimeException("The token is invalid since the user's last logout time is later than the time the token was issued at, which means the user has logged out.");
        }
        else if (issuedAtSeconds <= user.getPasswordChangeTime().toEpochSecond()) {
            // token's invalid
            throw new RuntimeException("The token is invalid since the user's last password change time is later than the time the token was issued at, which means the user has changed the password.");
        }

        // return the user instance if the token's claims passed the validation.
        return user;
    }
}
