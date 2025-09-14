package com.bci.auth_service.config;

import com.bci.auth_service.commons.utils.JwtUtil;
import com.bci.auth_service.errors.ApiError;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        if (token != null) {
            try {
                Claims claims = jwtUtil.parseToken(token);
                String userId = claims.getSubject();
                if (userId != null) {
                    UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(
                        userId, null, Collections.emptyList());
                    authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                try {
                    ApiError apiError = new ApiError(401, "Token JWT inválido o expirado");
                    String timestamp = apiError.getTimestamp().atOffset(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    String json = String.format(
                        "{\"error\":[{\"codigo\":%d,\"detail\":\"%s\",\"timestamp\":\"%s\"}]}",
                        apiError.getCodigo(), apiError.getDetail(), timestamp);
                    response.getWriter().write(json);
                } catch (Exception ex) {
                    response.getWriter().write("{\"error\":\"Token JWT inválido o expirado\"}");
                }
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getServletPath();
        return path.equals("/api/auth/sign-up") || path.startsWith("/h2-console");
    }

}
