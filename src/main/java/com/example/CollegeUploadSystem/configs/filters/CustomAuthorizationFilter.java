package com.example.CollegeUploadSystem.configs.filters;

import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.services.UserService;
import com.example.CollegeUploadSystem.utils.JwtUtils;
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
            String jws = this.jwtUtils.parseAuthorizationBearer(request);
            String login = this.jwtUtils.parseJws(jws).getBody().getSubject();

            User user = (User) this.userService.loadUserByUsername(login);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (RuntimeException e) {
            this.jwtUtils.sendUnauthorizedResponse(response, e);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // TODO: remove the commented code when tested.
//        String path = request.getRequestURI();
//        return path.equals("/api/auth/login")
//                || path.equals("/api/groups") && request.getMethod().equals("GET");

        return new AntPathRequestMatcher("/api/auth/login").matches(request)
                || new AntPathRequestMatcher("/api/groups", HttpMethod.GET.name(), true).matches(request)
                || new AntPathRequestMatcher("/api/groups/*/tasks", HttpMethod.GET.name(), true).matches(request) // permit to get all the tasks of a certain group.
                || new AntPathRequestMatcher("/api/groups/*/users", HttpMethod.GET.name(), true).matches(request) // permit to get all the students of a certain group.
                || new AntPathRequestMatcher("/api/tasks/*/file", HttpMethod.GET.name(), true).matches(request) // permit to download the description file of a certain task.
                || new AntPathRequestMatcher("/api/groups/*/student-results", HttpMethod.GET.name(), true).matches(request) // permit to get all the student results of a certain group.
                || new AntPathRequestMatcher("/api/student-results/*/file", HttpMethod.GET.name(), true).matches(request); // permit to download the file of a certain student result.
    }
}
