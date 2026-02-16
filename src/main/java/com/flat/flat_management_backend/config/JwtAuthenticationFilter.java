package com.flat.flat_management_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flat.flat_management_backend.exception.SessionExpiredException;
import com.flat.flat_management_backend.model.User;
import com.flat.flat_management_backend.repository.UserRepository;
import com.flat.flat_management_backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws IOException {
        try {
            String authHeader = request.getHeader("Authorization");


            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);
            Long userId = jwtService.extractUserId(token);

            User user = userRepository.findById(userId).orElse(null);

            if (user == null || !token.equals(user.getActiveJwtToken())) {
                throw new SessionExpiredException("Logged in from another device.");
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            user.getFlatNo(),
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                    );

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (Exception ex) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            Map<String, Object> body = Map.of(
                    "timestamp", Instant.now().toString(),
                    "status", 401,
                    "error", "Unauthorized",
                    "message", "Invalid or expired token",
                    "path", request.getRequestURI()
            );

            objectMapper.writeValue(response.getOutputStream(), body);
        }
    }
}
