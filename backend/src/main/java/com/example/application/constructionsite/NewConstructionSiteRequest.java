package com.example.application.constructionsite;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NewConstructionSiteRequest {

    @NotEmpty(message = "Token must not be empty")
    @NotBlank(message = "Token must not be empty")
    private String token;

    @NotEmpty(message = "Name must not be empty")
    @NotBlank(message = "Name must not be empty")
    private String name;

    @Min(value = 1, message = "From must be greater than zero")
    private int numOfWorkers;
    
}
