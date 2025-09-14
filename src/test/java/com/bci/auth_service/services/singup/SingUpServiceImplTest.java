package com.bci.auth_service.services.singup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.bci.auth_service.commons.utils.JwtUtil;
import com.bci.auth_service.controllers.singup.dtos.PhoneRequest;
import com.bci.auth_service.controllers.singup.dtos.SignUpRequest;
import com.bci.auth_service.controllers.singup.dtos.SignUpResponse;
import com.bci.auth_service.entities.UserEntity;
import com.bci.auth_service.errors.EmailAlreadyExistsException;
import com.bci.auth_service.repositories.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SingUpServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private SignUpServiceImpl singUpService;

    private SignUpRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = SignUpRequest.builder()
            .name("Test User")
            .email("test@example.com")
            .password("password")
            .phones(Collections.emptyList())
            .build();
    }

    @DisplayName("Debe registrar un usuario exitosamente cuando el email no existe")
    @Test
    void signUpUser_success() {
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("jwt-token");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        SignUpResponse response = singUpService.signUpUser(request);
        assertEquals("jwt-token", response.getToken());
        assertTrue(response.isActive());
    }

    @DisplayName("Debe lanzar EmailAlreadyExistsException si el email ya está registrado")
    @Test
    void signUpUser_emailAlreadyExists() {
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new UserEntity()));
        assertThrows(EmailAlreadyExistsException.class, () -> singUpService.signUpUser(request));
    }

    @DisplayName("Debe registrar un usuario con teléfonos asociados")
    @Test
    void signUpUser_withPhones() {
        request.setPhones(java.util.Arrays.asList(
                PhoneRequest.builder()
                    .number(123456789L)
                    .citycode(1)
                    .contrycode("PE")
                    .build()
            ));

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("jwt-token");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        SignUpResponse response = singUpService.signUpUser(request);
        assertNotNull(response.getId());
        assertEquals("jwt-token", response.getToken());
        assertTrue(response.isActive());
    }

}
