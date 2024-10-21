package com.example.application.handler;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

    private Integer bussinessErrorCode;
    private String bussinessErrorDescription;
    private String error;
    private Set<String> validationErrors;
    private Set<String> restrictionErrors;
    private Map<String, String> errors;

}
