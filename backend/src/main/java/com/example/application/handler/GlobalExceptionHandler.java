package com.example.application.handler;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exp){
        exp.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ExceptionResponse.builder()
            .bussinessErrorDescription("Internal error, contact developers")
            .error(exp.getMessage())
            .build()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exp){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ExceptionResponse.builder()
            .bussinessErrorCode(BusinessErrorCodes.BAD_CREDENTIALS.getCode())
            .bussinessErrorDescription(BusinessErrorCodes.BAD_CREDENTIALS.getDescription())
            .error(exp.getMessage())
            .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exp){
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors().forEach(error ->{
            var errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ExceptionResponse.builder()
            .validationErrors(errors)
            .build()
        );
    }
}
