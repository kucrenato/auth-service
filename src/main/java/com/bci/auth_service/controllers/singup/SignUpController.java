package com.bci.auth_service.controllers.singup;

import com.bci.auth_service.controllers.singup.dtos.SignUpRequest;
import com.bci.auth_service.controllers.singup.dtos.SignUpResponse;
import com.bci.auth_service.services.singup.SignUpService;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping(path = "/sign-up", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {

        SignUpResponse response = this.signUpService.signUpUser(signUpRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
