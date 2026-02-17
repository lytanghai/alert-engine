package com.finance.alert_engine.controller;

import com.finance.alert_engine.custom.response.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "App up and running";
    }

    @GetMapping("/hello")
    public ResponseBuilder<Map<String, String>> hello() {
        Map<String, String> data = Map.of("message", "Hello World");
        return ResponseBuilder.success(data);
    }
}
