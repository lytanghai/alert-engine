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

    @Scheduled(fixedRate = 300000)
    private void alert() {
        String trend = "=";
        double diff = 0.0;

        wake();

        XauResponse xauResponse = restTemplate.getForObject(Dictionary.xau_url, XauResponse.class);
        if (xauResponse == null || xauResponse.getPrice() == null)  {
            log.info("xauResponse not is empty");
            return;
        }
        double latestPrice = xauResponse.getPrice();

        if(objectCache.getAll().isEmpty()){
            log.info("first price init!");
            objectCache.add(latestPrice);
            return;
        }

        double previousPriceClosed = Double.valueOf((Double) objectCache.getLastN(1).get(0));

        if(latestPrice > previousPriceClosed) {
            trend = ">";
            diff = latestPrice - previousPriceClosed;
        } else {
            trend = "<";
            diff = previousPriceClosed - latestPrice;
        }
        sendTelegramAlert(getDiffResult(diff, trend), diff, latestPrice, previousPriceClosed);

        objectCache.add(latestPrice);

    }
    private void sendTelegramAlert(String alertType, double amount, double current, double previous) {
        String message = String.format(
                "<b>%s</b>\n" +
                "📉 <b>Changed</b>:<b>%s</b>\n" +
                "📊 <b>Previous</b>: <b>%s</b>",
                "💰 <b>Current</b>: <b>%s</b>\n" +
                alertType,
                fmt(amount),
                fmt(previous),
                fmt(current)
        );

        telegramService.sendMessage(message);
        log.info("{} detected. Change: {}, Previous: {}, Current: {}", alertType, amount, previous, current);
    }

    private String fmt(double value) {
        return String.format("%.2f", value);
    }

    private String getDiffResult(double diff, String trend) {
        if ("<".equals(trend)) {
            if(diff >= 25 && diff <= 50) {
                return  diff + " 🔻Small Drop Alert!"; // 25–49
            }

            if(diff >= 50 && diff <= 100) {
                return diff + " 🔻Medium Drop Alert!"; // 50–99
            }

            if(diff >= 100 && diff <= 150) {
                return diff + " 🔻Big Drop Alert!"; // 100–149
            }

            if(diff > 150) {
                return diff + " 🔻Major Drop Alert!"; // 200+
            }
        } else if(">".equals(trend)) {
            if(diff >= 25 && diff <= 50) {
                return  diff + " 🔺Small Rise Alert!"; // 25–49
            }

            if(diff >= 50 && diff <= 100) {
                return diff + " 🔺Medium Rise Alert!"; // 50–99
            }

            if(diff >= 100 && diff <= 150) {
                return diff + " 🔺Big Rise Alert!"; // 100–149
            }

            if(diff > 150) {
                return diff + " 🔺Major Rise Alert!"; // 200+
            }
        }
        return diff + " Remain Unchanged!!!";

    }

}
