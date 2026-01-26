package com.finance.alert_engine.service;

import com.finance.alert_engine.component.TelegramSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class TelegramService {

    private final RestTemplate restTemplate;
    private final String botToken;
    private final String channel;

    public TelegramService(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String channel
    ) {
        this.restTemplate = new RestTemplate();
        this.botToken = botToken;
        this.channel = channel;
    }

    public void send(String message) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "chat_id", channel,
                "text", message,
                "parse_mode", "Markdown"
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        restTemplate.postForEntity(url, entity, String.class);
    }

}
