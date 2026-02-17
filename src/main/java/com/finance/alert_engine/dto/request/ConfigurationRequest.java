package com.finance.alert_engine.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfigurationRequest {

    @NotNull @NotBlank
    private String name;

    @NotNull @NotBlank
    private String value;
}
