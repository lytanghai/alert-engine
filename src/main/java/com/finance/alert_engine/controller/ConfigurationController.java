package com.finance.alert_engine.controller;

import com.finance.alert_engine.custom.response.ResponseBuilder;
import com.finance.alert_engine.dto.request.ConfigurationRequest;
import com.finance.alert_engine.model.Configuration;
import com.finance.alert_engine.service.operation.ConfigurationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/config")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @PostMapping("/create")
    public ResponseBuilder<Configuration> createConfig(@RequestBody ConfigurationRequest configuration) {
        return ResponseBuilder.success(configurationService.createConfig(configuration));
    }

}
