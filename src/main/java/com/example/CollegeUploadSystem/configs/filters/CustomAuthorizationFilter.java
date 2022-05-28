package com.example.CollegeUploadSystem.configs.filters;

import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.services.UserService;
import com.example.CollegeUploadSystem.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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
        try {
            // extract the JWT from the header.
            String jws = this.jwtUtils.parseAuthorizationBearer(request);

            // get the claims stored in the JWT.
            Claims jwtClaims = this.jwtUtils.parseJws(jws).getBody();

            // validate the JWT claims and get the user object.
            User user = this.jwtUtils.validateClaims(jwtClaims);

            // put the user into the security context.
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (RuntimeException e) {
            this.jwtUtils.sendUnauthorizedResponse(response, e);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathRequestMatcher("/api/auth/login").matches(request)
                || new AntPathRequestMatcher("/api/groups", HttpMethod.GET.name(), true).matches(request)
                || new AntPathRequestMatcher("/api/groups/*/tasks", HttpMethod.GET.name(), true).matches(request) // permit to get all the tasks of a certain group.
                || new AntPathRequestMatcher("/api/groups/*/users", HttpMethod.GET.name(), true).matches(request) // permit to get all the students of a certain group.
                || new AntPathRequestMatcher("/api/tasks/*/file", HttpMethod.GET.name(), true).matches(request) // permit to download the description file of a certain task.
                || new AntPathRequestMatcher("/api/groups/*/student-results", HttpMethod.GET.name(), true).matches(request) // permit to get all the student results of a certain group.
                || new AntPathRequestMatcher("/api/student-results/*/file", HttpMethod.GET.name(), true).matches(request); // permit to download the file of a certain student result.
    }
}
