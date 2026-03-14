package com.finance.alert_engine.config;

import com.finance.alert_engine.components.TelegramComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.webhook.Webhook;


@Configuration
@Slf4j
public class TelegramBotConfig {

    @Value("${telegram.webhook.url}")
    private String webhookUrl;

    @Value("${telegram.webhook.secret:}")
    private String webhookSecret;

    @Bean
    public Webhook webhook() {
        return Webhook.builder()
                .url(webhookUrl)
                .secretToken(webhookSecret) // Optional: for security
                .build();
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramComponent telegramComponent, Webhook webhook) {
        try {
            // Use webhook instead of polling
            TelegramBotsWebhookApplication botsWebhookApplication = new TelegramBotsWebhookApplication();
            botsWebhookApplication.registerBotWithWebhook(telegramComponent, webhook);

            log.info("🚀 Bot successfully registered with Telegram WEBHOOK mode!");
            log.info("📡 Webhook URL: {}", webhookUrl);

            return null; // Not needed for webhook
        } catch (TelegramApiException e) {
            log.error("❌ Failed to register bot webhook: {}", e.getMessage());

            // Fallback to polling? (Optional - but careful with multiple instances)
            if (isDevelopmentEnvironment()) {
                return registerPollingFallback(telegramComponent);
            }

            throw new RuntimeException("Failed to register bot webhook", e);
        }
    }

    private TelegramBotsApi registerPollingFallback(TelegramComponent telegramComponent) {
        try {
            log.warn("⚠️ Falling back to polling mode (development only)");
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramComponent);
            return botsApi;
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to register bot in polling mode", e);
        }
    }

    private boolean isDevelopmentEnvironment() {
        String profile = System.getProperty("spring.profiles.active", "dev");
        return "dev".equals(profile) || "development".equals(profile);
    }
}