package com.bci.auth_service.config;

import com.bci.auth_service.errors.ApiError;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(),
            "No se encontró el token JWT en la cabecera Authorization o es inválido");
        String timestamp = apiError.getTimestamp().atOffset(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String json = String.format(
            "{\"error\":[{\"codigo\":%d,\"detail\":\"%s\",\"timestamp\":\"%s\"}]}",
            apiError.getCodigo(), apiError.getDetail(), timestamp);
        response.getWriter().write(json);
    }

}

