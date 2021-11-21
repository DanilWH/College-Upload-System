package com.example.CollegeUploadSystem.configs.filters;

import com.example.CollegeUploadSystem.dto.LoginRequest;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // get the body and parse it.
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            // authenticate the user with its login and password.
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getLogin(),
                    loginRequest.getPassword()
            );

            return this.authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User loggedInUser = (User) authResult.getPrincipal();

        String accessJws = this.jwtUtils.generateAccessJws(loggedInUser);
        String refreshJws = this.jwtUtils.generateRefreshJws(loggedInUser);

        // send the generated JWSs as a JSON response.
        new ObjectMapper().writeValue(response.getOutputStream(), this.jwtUtils.packLoginResponse(loggedInUser, accessJws, refreshJws));
    }

    /* TODO implement refresh token. */
}
