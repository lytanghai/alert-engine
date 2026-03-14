package com.finance.alert_engine.controller;

import com.finance.alert_engine.components.TelegramComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TelegramWebhookController {
    
    private final TelegramComponent telegramComponent;
    
    @PostMapping("${telegram.webhook.path:/webhook}")
    public BotApiMethod<?> handleWebhook(@RequestBody Update update) {
        log.debug("📨 Webhook received: {}", update.getUpdateId());
        return telegramComponent.onWebhookUpdateReceived(update);
    }
}