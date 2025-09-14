package com.bci.auth_service.services.singup;

import com.bci.auth_service.commons.utils.EncryptorUtil;
import com.bci.auth_service.commons.utils.JwtUtil;
import com.bci.auth_service.controllers.singup.dtos.PhoneRequest;
import com.bci.auth_service.controllers.singup.dtos.SignUpRequest;
import com.bci.auth_service.controllers.singup.dtos.SignUpResponse;
import com.bci.auth_service.entities.PhoneEntity;
import com.bci.auth_service.entities.UserEntity;
import com.bci.auth_service.errors.EmailAlreadyExistsException;
import com.bci.auth_service.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Override
    public SignUpResponse signUpUser(SignUpRequest signUpRequest) {

        Optional<UserEntity> userByEmail = this.userRepository.findByEmail(
            signUpRequest.getEmail());
        if (userByEmail.isPresent()) {
            throw new EmailAlreadyExistsException(
                "Ya existe un usuario registrado con este correo");
        }

        UserEntity userEntity = UserEntity.builder()
            .id(UUID.randomUUID().toString())
            .name(signUpRequest.getName())
            .email(signUpRequest.getEmail())
            .password(EncryptorUtil.encrypt(signUpRequest.getPassword()))
            .created(LocalDateTime.now())
            .isActive(true)
            .build();

        List<PhoneEntity> phoneEntities = null;
        if (signUpRequest.getPhones() != null && !signUpRequest.getPhones().isEmpty()) {
            phoneEntities = new ArrayList<>();
            for (PhoneRequest phoneReq : signUpRequest.getPhones()) {
                PhoneEntity phoneEntity = PhoneEntity.builder()
                    .number(phoneReq.getNumber())
                    .cityCode(phoneReq.getCitycode())
                    .countryCode(phoneReq.getContrycode())
                    .user(userEntity)
                    .build();
                phoneEntities.add(phoneEntity);
            }
            userEntity.setPhones(phoneEntities);
        }

        this.userRepository.save(userEntity);
        String jwt = jwtUtil.generateToken(userEntity.getId(), userEntity.getEmail());

        return SignUpResponse.builder()
            .id(userEntity.getId())
            .created(userEntity.getCreated().toString())
            .lastLogin(
                userEntity.getLastLogin() != null ? userEntity.getLastLogin().toString() : null)
            .token(jwt)
            .isActive(userEntity.getIsActive())
            .build();
    }

}
