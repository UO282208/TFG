package com.example.application.constructionsite;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetConstructionSiteRequest {

    @NotEmpty(message = "Token must not be empty")
    @NotBlank(message = "Token must not be empty")
    private String token;
}
