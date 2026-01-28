package com.finance.alert_engine.controller;

import com.finance.alert_engine.dto.TelegramUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telegram")
@Slf4j
public class TelegramWebhookController {

    //https://api.telegram.org/bot<YOUR_BOT_TOKEN>/setWebhook?url=https://disciplinary-maren-tanghai-2617c143.koyeb.app/telegram/webhook
    @PostMapping("/webhook")
    public ResponseEntity<Void> onUpdate(@RequestBody TelegramUpdate update) {

        if (update.getMessage() != null) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChat().getId();

            log.info("Received message: {} {}", text, chatId);

            // Example commands
            if ("/ping".equalsIgnoreCase(text)) {
                log.info("Pong");
                // reply
            }

            if ("/status".equalsIgnoreCase(text)) {
                // return price / status
            }
        }

        return ResponseEntity.ok().build();
    }
}
