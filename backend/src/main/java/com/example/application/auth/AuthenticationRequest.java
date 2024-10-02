package com.example.application.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {

    @NotEmpty(message = "Password must not be empty")
    @NotBlank(message = "Password  must not be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    
    @Email(message = "Incorrect email format")
    @NotEmpty(message = "Email must not be empty")
    @NotBlank(message = "Email must not be empty")
    private String email;
}
