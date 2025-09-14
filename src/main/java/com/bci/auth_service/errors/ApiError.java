package com.bci.auth_service.errors;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiError {

    private final LocalDateTime timestamp = LocalDateTime.now();

    private int codigo;

    private String detail;

}