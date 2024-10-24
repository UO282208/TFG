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

    @NotEmpty(message = "El campo nombre no debe estar vacío")
    @NotBlank(message = "El campo nombre no debe estar vacío")
    private String name;

    @Min(value = 1, message = "El campo número de trabajadores debe tener un valor superior a 0")
    private int numOfWorkers;  
}
