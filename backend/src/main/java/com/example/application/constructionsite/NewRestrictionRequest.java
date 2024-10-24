package com.example.application.constructionsite;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NewRestrictionRequest {

    @Min(value = 0, message = "El campo transformadores debe tener un valor igual o superior a 0")
    private int transformers;
    @Min(value = 0, message = "El campo tanques de expansión debe tener un valor igual o superior a 0")
    private int expansionTanks;
    @Min(value = 0, message = "El campo radiadores debe tener un valor igual o superior a 0")
    private int radiators;
    @Min(value = 0, message = "El campo puntos de conexión debe tener un valor igual o superior a 0")
    private int connectionPoints;
    @Min(value = 0, message = "El campo muros cortafuegos tener un valor igual o superior a 0")
    private int firewalls;
    @NotNull(message = "El campo fecha de inicio no debe estar vacío")
    private LocalDateTime startDate;
    @NotNull(message = "El campo fecha de fin no debe estar vacío")
    private LocalDateTime endDate;
    private boolean shouldAppear;
}
