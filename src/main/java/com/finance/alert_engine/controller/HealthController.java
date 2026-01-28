package com.finance.alert_engine.controller;

import com.finance.alert_engine.service.provider.TelegramService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class HealthController {


    private final TelegramService telegramService;

    @GetMapping("/health")
    public String health() {
        return "App up and running";
    }

    @GetMapping("/tanghai")
    public void testMsg(){
        telegramService.sendMessage("Hey there!");
    }
}
