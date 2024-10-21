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

    @Min(value = 0, message = "Transformers be greater or equal to zero")
    private int transformers;
    @Min(value = 0, message = "Expansion Tanks must be greater or equal to zero")
    private int expansionTanks;
    @Min(value = 0, message = "Radiators must be greater or equal to zero")
    private int radiators;
    @Min(value = 0, message = "Connection Points must be greater or equal to zero")
    private int connectionPoints;
    @Min(value = 0, message = "Firewalls must be greater or equal to zero")
    private int firewalls;
    @NotNull(message = "The Start Date must not be null")
    private LocalDateTime startDate;
    @NotNull(message = "The End Date must not be null")
    private LocalDateTime endDate;
    private boolean shouldAppear;
}
