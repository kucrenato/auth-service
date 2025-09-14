package com.bci.auth_service.commons.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @DisplayName("Debe generar y parsear un token JWT correctamente")
    @Test
    void generateAndParseToken_success() {
        String userId = "user-123";
        String email = "test@example.com";
        String token = jwtUtil.generateToken(userId, email);
        assertNotNull(token);

        Claims claims = jwtUtil.parseToken(token);
        assertEquals(userId, claims.getSubject());
        assertEquals(email, claims.get("email", String.class));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @DisplayName("Debe lanzar JwtException si el token es inválido")
    @Test
    void parseToken_invalidToken() {
        String invalidToken = "invalid.token.value";
        assertThrows(JwtException.class, () -> jwtUtil.parseToken(invalidToken));
    }
}

