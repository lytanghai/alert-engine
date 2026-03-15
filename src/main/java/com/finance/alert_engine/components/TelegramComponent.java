//package com.finance.alert_engine.components;
//
//import com.finance.alert_engine.service.NotificationService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.bots.TelegramWebhookBot;
//import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
//import org.telegram.telegrambots.meta.api.objects.Update;
//
//@Component
//@Slf4j
//public class TelegramComponent extends TelegramWebhookBot {
//
//    @Value("${telegram.bot.token}")
//    private String botToken;
//
//    @Value("${telegram.bot.username}")
//    private String botUsername;
//
//    @Value("${telegram.webhook.path:/webhook}")
//    private String webhookPath;
//
//    @Autowired
//    private NotificationService notificationService;
//
//    @Override
//    public String getBotUsername() {
//        return botUsername;
//    }
//
//    @Override
//    public String getBotToken() {
//        return botToken;
//    }
//
//    @Override
//    public String getBotPath() {
//        return webhookPath;
//    }
//
//    @Override
//    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String[] messageText = update.getMessage().getText().split("\\r?\\n");
//            String msg = "";
//            String value = "";
//            String extra = "";
//
//            if(messageText.length > 1 && !update.getMessage().hasViaBot()) {
//                for(int i = 0; i < messageText.length; i++){
//                    if(i == 0) msg = messageText[0];
//                    if(i == 1) value = messageText[1];
//                    if(i == 2) extra = messageText[2];
//                }
//                notificationService.createFromTelegram(msg, value, extra, update.getMessage().getDate());
//
//                // Optional: Send confirmation
////                return new SendMessage(
////                        update.getMessage().getChatId().toString(),
////                        "✅ Notification created successfully!"
////                );
//            }
//        } else {
//            log.info("⚠️ Update received but no text message: {}", update);
//        }
//        return null; // No response needed
//    }
//}