package com.bci.auth_service.config;

import com.bci.auth_service.errors.ApiError;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN.value(),
            "No tiene permisos para acceder a este recurso");
        String timestamp = apiError.getTimestamp().atOffset(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String json = String.format(
            "{\"error\":[{\"codigo\":%d,\"detail\":\"%s\",\"timestamp\":\"%s\"}]}",
            apiError.getCodigo(), apiError.getDetail(), timestamp);
        response.getWriter().write(json);
    }

}

