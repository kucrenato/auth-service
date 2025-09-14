package com.bci.auth_service.controllers.login;

import com.bci.auth_service.controllers.login.dtos.LoginResponse;
import com.bci.auth_service.services.login.LoginService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public LoginResponse login() {

        return this.loginService.loginUser();
    }

}
