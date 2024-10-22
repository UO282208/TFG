package com.example.application.constructionsite;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ModifyConstructionSiteRequest {

    @NotEmpty(message = "Name must not be empty")
    @NotBlank(message = "Name must not be empty")
    private String name;

    @Min(value = 1, message = "Workers must be greater than zero")
    private int numOfWorkers;  
}
