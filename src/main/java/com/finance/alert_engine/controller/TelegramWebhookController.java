package com.finance.alert_engine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.alert_engine.components.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TelegramWebhookController {
    
    private final TelegramBot telegramBot;
    private final ObjectMapper objectMapper; // Add this for JSON parsing


//    curl -F "url=https://your-render-app.onrender.com/webhook" \
//            "https://api.telegram.org/botYOUR_BOT_TOKEN/setWebhook"

//    verify: curl "https://api.telegram.org/botYOUR_BOT_TOKEN/getWebhookInfo"

    @PostMapping
    public ResponseEntity<Void> onUpdateReceived(@RequestBody String updateJson) {
        log.info("Received webhook update: {}", updateJson);

        try {
            Update update = objectMapper.readValue(updateJson, Update.class);
            telegramBot.consume(update);
        } catch (Exception e) {
            log.error("Failed to parse update: {}", e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}