package com.finance.alert_engine.dto;

import lombok.Data;

@Data
public class TelegramUpdate {
    private Message message;

    @Data
    public static class Message {
        private String text;
        private Chat chat;
    }

    @Data
    public static class Chat {
        private Long id;
    }
}
