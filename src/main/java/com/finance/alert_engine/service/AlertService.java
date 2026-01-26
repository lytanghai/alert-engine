package com.finance.alert_engine.service;

import com.finance.alert_engine.Dictionary;
import com.finance.alert_engine.dto.XauResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AlertService {

    private final RestTemplate restTemplate;
    private final ObjectCache objectCache;
    private final TelegramService telegramService;

    @Scheduled(fixedRate = 300000)
    public void checkPriceCorrection() {
        log.info("fetching latest price...");
        wake();
        XauResponse xauResponse =
                restTemplate.getForObject(Dictionary.xau_url, XauResponse.class);

        if (xauResponse == null || xauResponse.getPrice() == null) return;

        BigDecimal latestPrice = xauResponse.getPrice();

        List<BigDecimal> lastPrices = objectCache.getLastN(1);
        if (lastPrices.isEmpty()) {
            objectCache.add(latestPrice);
            log.info("No previous price found. Saved latest price.");
            return;
        }

        BigDecimal previousPrice = lastPrices.get(0);

        // Only care about DROP
        if (latestPrice.compareTo(previousPrice) >= 0) {
            objectCache.add(latestPrice);
            return;
        }

        BigDecimal dropAmount = previousPrice.subtract(latestPrice); // positive drop

        String alertType = getDropType(dropAmount);

        if (alertType != null) {
            String message = String.format(
                    "📉 <b> ~ %s</b>\n" +
                    "🔻 Drop: <b>%s</b>\n" +
                    "💰 Current: <b>%s</b>\n" +
                    "📊 Previous: <b>%s</b>",
                    alertType,
                    dropAmount,
                    latestPrice,
                    previousPrice
            );

            telegramService.sendMessage(message);

            log.info("{} detected. Drop: {}, Previous: {}, Current: {}",
                    alertType, dropAmount, previousPrice, latestPrice);
        }

        objectCache.add(latestPrice);
    }

    private String getDropType(BigDecimal drop) {

        if (drop.compareTo(BigDecimal.valueOf(Dictionary.dropDown25OunceDropDown)) >= 0
                && drop.compareTo(BigDecimal.valueOf(Dictionary.dropDown50OunceDropDown)) < 0) {
            return "Small Drop Alert!";
        }

        if (drop.compareTo(BigDecimal.valueOf(Dictionary.dropDown50OunceDropDown)) >= 0
                && drop.compareTo(BigDecimal.valueOf(Dictionary.dropDown100OunceDropDown)) <= 0) {
            return "Big Drop Alert!";
        }

        if (drop.compareTo(BigDecimal.valueOf(Dictionary.dropDown100OunceDropDown)) > 0) {
            return "Major Drop Alert!";
        }

        return null; // below 25 → ignore
    }

    void wake() {
        String url = "https://disciplinary-maren-tanghai-2617c143.koyeb.app/health";
        restTemplate.getForObject(url, String.class);
    }

}
