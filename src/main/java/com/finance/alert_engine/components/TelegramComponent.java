package com.finance.alert_engine.components;

import com.finance.alert_engine.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TelegramComponent extends TelegramLongPollingBot {
    
    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Autowired
    private NotificationService notificationService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String[] messageText = update.getMessage().getText().split("\\r?\\n");
            String msg = "";
            String value = "";
            String extra = "";
            if(messageText.length > 1 && !update.getMessage().hasViaBot()) {
                for(int i = 0; i < messageText.length; i++){
                    if(i == 0) msg = messageText[0];
                    if(i == 1) value = messageText[1];
                    if(i == 2) extra = messageText[2];
                }
                notificationService.createFromTelegram(msg, value,extra, update.getMessage().getDate());
            }

        } else {
            log.info("⚠️ Update received but no text message: {}", update);
        }
    }
}