package com.bci.auth_service.controllers.singup.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SignUpResponse {

    private String id;

    private String created;

    private String lastLogin;

    private String token;

    @JsonProperty("isActive")
    private boolean isActive;

}
