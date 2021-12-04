package com.example.CollegeUploadSystem.configs.filters;

import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.services.UserService;
import com.example.CollegeUploadSystem.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public CustomAuthorizationFilter(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // do filter if the user isn't trying either to register or to log in.
        if (!request.getRequestURI().equals("/api/auth/login")) {
            try {
                String jws = request.getHeader("Authorization").substring("Bearer ".length());
                String login = this.jwtUtils.parseJws(jws).getBody().getSubject();

                User user = (User) this.userService.loadUserByUsername(login);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (JwtException e) {
                this.jwtUtils.throwInvalidJwsException(response, e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
