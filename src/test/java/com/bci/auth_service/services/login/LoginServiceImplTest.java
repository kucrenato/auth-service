package com.bci.auth_service.services.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bci.auth_service.commons.utils.EncryptorUtil;
import com.bci.auth_service.commons.utils.JwtUtil;
import com.bci.auth_service.controllers.login.dtos.LoginResponse;
import com.bci.auth_service.entities.UserEntity;
import com.bci.auth_service.errors.RequestContextNotFoundException;
import com.bci.auth_service.errors.UserNotFoundException;
import com.bci.auth_service.mappers.PhoneMapper;
import com.bci.auth_service.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class LoginServiceImplTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhoneMapper phoneMapper;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private LoginServiceImpl loginService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Debe iniciar sesión con éxito para un usuario válido")
    @Test
    void loginUser_success() {
        // Arrange
        String token = "valid.jwt.token";
        String email = "test@example.com";
        String userId = "user-123";
        UserEntity user = UserEntity.builder()
            .id(userId)
            .email(email)
            .name("Test User")
            .password(EncryptorUtil.encrypt("password"))
            .created(LocalDateTime.now())
            .isActive(true)
            .phones(Collections.emptyList())
            .build();
        Claims claims = mock(Claims.class);
        when(claims.get("email", String.class)).thenReturn(email);
        when(jwtUtil.parseToken(token)).thenReturn(claims);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("nuevo-jwt-token");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

        // Act
        LoginResponse response = loginService.loginUser();

        // Assert
        assertEquals(userId, response.getId());
        assertEquals(email, response.getEmail());
        assertTrue(response.isActive());
        assertNotNull(response.getToken());
    }

    @DisplayName("Debe lanzar UserNotFoundException si el usuario no existe")
    @Test
    void loginUser_userNotFound() {

        String token = "valid.jwt.token";
        String email = "notfound@example.com";
        Claims claims = mock(Claims.class);
        when(claims.get("email", String.class)).thenReturn(email);
        when(jwtUtil.parseToken(token)).thenReturn(claims);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

        assertThrows(UserNotFoundException.class, () -> loginService.loginUser());
    }

    @DisplayName("Debe lanzar RequestContextNotFoundException si no hay contexto de request")
    @Test
    void loginUser_noRequestContext() {

        RequestContextHolder.resetRequestAttributes();
        assertThrows(RequestContextNotFoundException.class, () -> loginService.loginUser());
    }

}
