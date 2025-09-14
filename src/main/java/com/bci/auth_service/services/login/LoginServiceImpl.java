package com.bci.auth_service.services.login;

import com.bci.auth_service.commons.utils.EncryptorUtil;
import com.bci.auth_service.commons.utils.JwtUtil;
import com.bci.auth_service.controllers.login.dtos.LoginResponse;
import com.bci.auth_service.entities.PhoneEntity;
import com.bci.auth_service.entities.UserEntity;
import com.bci.auth_service.errors.RequestContextNotFoundException;
import com.bci.auth_service.errors.UserNotFoundException;
import com.bci.auth_service.mappers.PhoneMapper;
import com.bci.auth_service.repositories.UserRepository;
import io.jsonwebtoken.Claims;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final PhoneMapper phoneMapper;

    @Override
    public LoginResponse loginUser() {

        ServletRequestAttributes attr
            = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr == null) {
            throw new RequestContextNotFoundException();
        }
        HttpServletRequest request = attr.getRequest();
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Claims claims = jwtUtil.parseToken(token);
        String email = claims.get("email", String.class);

        Optional<UserEntity> userByEmail = this.userRepository.findByEmail(email);

        if (!userByEmail.isPresent()) {
            throw new UserNotFoundException();
        }

        UserEntity user = userByEmail.get();

        // Persist last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        List phoneResponses = null;
        if (user.getPhones() != null) {
            phoneResponses = new ArrayList<>();
            for (PhoneEntity phoneEntity : user.getPhones()) {
                phoneResponses.add(phoneMapper.phoneEntityToPhoneResponse(phoneEntity));
            }
        }

        String renewedToken = jwtUtil.generateToken(user.getId(), user.getEmail());

        return LoginResponse.builder()
            .id(user.getId())
            .created(user.getCreated())
            .lastLogin(LocalDateTime.now())
            .token(renewedToken)
            .isActive(user.getIsActive())
            .name(user.getName())
            .email(user.getEmail())
            .password(EncryptorUtil.decrypt(user.getPassword()))
            .phones(phoneResponses)
            .build();
    }

}
