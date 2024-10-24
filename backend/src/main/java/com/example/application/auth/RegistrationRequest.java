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
public class RegistrationRequest {

    @NotEmpty(message = "El campo nombre no debe estar vacío")
    @NotBlank(message = "El campo nombre no debe estar vacío")
    private String name;

    @NotEmpty(message = "El campo contraseña no debe estar vacío")
    @NotBlank(message = "El campo contraseña no debe estar vacío")
    @Size(min = 6, message = "El campo contraseña debe tener 6 carácteres como mínimo")
    private String password;
    
    @Email(message = "El campo email tiene un formato incorrecto")
    @NotEmpty(message = "El campo email no debe estar vacío")
    @NotBlank(message = "El campo email no debe estar vacío")
    private String email;
}
