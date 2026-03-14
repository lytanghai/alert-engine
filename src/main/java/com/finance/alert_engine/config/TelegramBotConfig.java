package com.finance.alert_engine.config;

import com.finance.alert_engine.components.TelegramComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Slf4j
public class TelegramBotConfig {
    
    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramComponent telegramComponent) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramComponent);
            log.info("🚀 Bot successfully registered with Telegram!");
            return botsApi;
        } catch (TelegramApiException e) {
            log.error("❌ Failed to register bot: {}", e.getMessage());
            throw new RuntimeException("Failed to register bot", e);
        }
    }
}