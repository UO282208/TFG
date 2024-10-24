package com.example.application.handler;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public enum BusinessErrorCodes {

    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "Sin código"),
    BAD_CREDENTIALS(300, HttpStatus.FORBIDDEN, "El usuario o la contraseña son incorrectos");
    
    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description){
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
