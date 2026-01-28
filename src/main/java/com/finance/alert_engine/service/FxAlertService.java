package com.finance.alert_engine.service;

import com.finance.alert_engine.Dictionary;
import com.finance.alert_engine.dto.XauResponse;
import com.finance.alert_engine.service.cache.ObjectCache;
import com.finance.alert_engine.service.provider.TelegramService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
public class FxAlertService {
    private final RestTemplate restTemplate;
    private final ObjectCache objectCache;
    private final TelegramService telegramService;

    @PostConstruct
    public void runOnceOnStartup() {
        telegramService.sendMessage("Application is starting!");
    }

    public void wake() {
        String url = "https://disciplinary-maren-tanghai-2617c143.koyeb.app/health";
        restTemplate.getForObject(url, String.class);
    }

    @Scheduled(fixedRate = 300000) // 5 minutes
    private void alert() {

        log.info("Incoming...");
        wake();

        XauResponse xauResponse =
                restTemplate.getForObject(Dictionary.xau_url, XauResponse.class);

        if (xauResponse == null || xauResponse.getPrice() == null) {
            log.warn("Failed to fetch XAU price");
            return;
        }

        double latest = xauResponse.getPrice();

        if (objectCache.getAll().isEmpty()) {
            objectCache.add(latest);
            log.info("Initial price saved: {}", latest);
            return;
        }

        double previous = (double) objectCache.getLastN(1).get(0);

        double diff = latest - previous;
        boolean isUp = latest > previous;

        String alertType = getDiffResult(diff, isUp);

        if (alertType != null) {
            sendTelegramAlert(alertType, diff, latest, previous);
        }

        objectCache.add(latest);
    }

    private void sendTelegramAlert(String alertType,
                                   double amount,
                                   double current,
                                   double previous) {

        String valueEmoji = current > previous ? "🟢" : "🔴";
        String arrowEmoji = current > previous ? "⬆️" : "⬇️";

        String message = String.format(
                "%s <b>%s</b>\n\n" +
                        "📉 <b>Change</b>: %s <b>%s</b>\n" +
                        "💰 <b>Current</b>: <b>%s</b>\n" +
                        "📊 <b>Previous</b>: <b>%s</b>",
                arrowEmoji,
                alertType,
                valueEmoji,
                fmt(amount),
                fmt(current),
                fmt(previous)
        );

        telegramService.sendMessage(message);

        log.info("{} | Change: {} | Prev: {} | Curr: {}",
                alertType, amount, previous, current);
    }


    private String fmt(double value) {
        return String.format("%.2f", value);
    }

    private String getDiffResult(double diff, boolean isUp) {

        if (diff < 25) return "Consolidation";


        if (isUp) {
            if (diff < 50) return "Small Rise Alert";
            if (diff < 100) return "Medium Rise Alert";
            if (diff < 150) return "Big Rise Alert";
            return "Major Rise Alert";
        } else {
            if (diff < 50) return "Small Drop Alert";
            if (diff < 100) return "Medium Drop Alert";
            if (diff < 150) return "Big Drop Alert";
            return "Major Drop Alert";
        }
    }


}
