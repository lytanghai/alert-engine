package com.finance.alert_engine.components;

import com.finance.alert_engine.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final String botToken;
    private final TelegramClient telegramClient;

    @Autowired
    private NotificationService notificationService;

    public TelegramBot(
            @Value("${telegram.bot.token}") String botToken,
            TelegramClient telegramClient) {
        this.botToken = botToken;
        this.telegramClient = telegramClient;
        log.info("Telegram bot initialized with token: {}", botToken.substring(0, 10) + "...");
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
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
                notificationService.createFromTelegram(msg, value, extra, update.getMessage().getDate());

            }
        } else {
            log.info("⚠️ Update received but no text message: {}", update);
        }
    }
    
    private void sendMessage(long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        
        try {
            telegramClient.execute(message);
            log.info("Response sent to chatId {}: {}", chatId, text);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chatId {}: {}", chatId, e.getMessage());
        }
    }
}