package com.bci.auth_service.controllers.login.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String id;

    private LocalDateTime created;

    private LocalDateTime lastLogin;

    private String token;

    @JsonProperty("isActive")
    private boolean isActive;

    private String name;

    private String email;

    private String password;

    private List<PhoneResponse> phones;

}
