package com.finance.alert_engine.service;

import com.finance.alert_engine.Dictionary;
import com.finance.alert_engine.dto.XauResponse;
import com.finance.alert_engine.service.cache.ObjectCache;
import com.finance.alert_engine.service.provider.TelegramService;
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
        log.info("Fetching latest price...");
        wake(); // self-ping to prevent cold sleep

        XauResponse xauResponse = restTemplate.getForObject(Dictionary.xau_url, XauResponse.class);

        if (xauResponse == null || xauResponse.getPrice() == null)  {
            log.info("xauResponse not is empty");
            return;
        }

        log.info("Latest price is {}", xauResponse.getPrice());

        BigDecimal latestPrice = xauResponse.getPrice();

        if(!objectCache.getAll().isEmpty()) {
            List<BigDecimal> lastPrices = objectCache.getLastN(1);
            BigDecimal previous = lastPrices.get(0);

            BigDecimal difference = previous.subtract(latestPrice);

            log.info("previous {} - latest {} = {}", previous, latestPrice, difference);
            if (lastPrices.isEmpty()) {
                objectCache.add(latestPrice);
                log.info("No previous price found. Saved latest price.");
                return;
            }

            BigDecimal previousPrice = lastPrices.get(0);

            // DROP logic
            if (latestPrice.compareTo(previousPrice) < 0) {
                BigDecimal dropAmount = previousPrice.subtract(latestPrice); // positive
                String alertType = getDropType(dropAmount);
                if (alertType != null) {
                sendTelegramAlert("📉", alertType, dropAmount, latestPrice, previousPrice);
                }
            }
            // RISE logic
            else if (latestPrice.compareTo(previousPrice) > 0) {
                BigDecimal riseAmount = latestPrice.subtract(previousPrice); // positive
                String alertType = getRiseType(riseAmount);
                if (alertType != null) {
                sendTelegramAlert("📈", alertType, riseAmount, latestPrice, previousPrice);
                }
            }
        }
        objectCache.add(latestPrice);
    }

    private void sendTelegramAlert(String emoji, String alertType, BigDecimal amount, BigDecimal current, BigDecimal previous) {
        String message = String.format(
                "%s <b>%s</b>\n" +
                        "⬆️/⬇️ Change: <b>%s</b>\n" +
                        "💰 Current: <b>%s</b>\n" +
                        "📊 Previous: <b>%s</b>",
                emoji,
                alertType,
                amount,
                current,
                previous
        );

        telegramService.sendMessage(message);
        log.info("{} detected. Change: {}, Previous: {}, Current: {}", alertType, amount, previous, current);
    }


    private String getDropType(BigDecimal drop) {

        if (drop.compareTo(BigDecimal.valueOf(Dictionary.dropDown25OunceDropDown)) >= 0
                && drop.compareTo(BigDecimal.valueOf(Dictionary.dropDown50OunceDropDown)) < 0) {
            return "🔻Small Drop Alert!"; // 25–49
        }

        if (drop.compareTo(BigDecimal.valueOf(Dictionary.dropDown50OunceDropDown)) >= 0
                && drop.compareTo(BigDecimal.valueOf(Dictionary.dropDown100OunceDropDown)) < 0) {
            return "🔻Medium Drop Alert!"; // 50–99
        }

        if (drop.compareTo(BigDecimal.valueOf(Dictionary.dropDown100OunceDropDown)) >= 0
                && drop.compareTo(BigDecimal.valueOf(Dictionary.dropDown150OunceDropDown)) < 0) {
            return "🔻Big Drop Alert!"; // 100–149
        }

        if (drop.compareTo(BigDecimal.valueOf(Dictionary.dropDown200OunceDropDown)) >= 0) {
            return "🔻Major Drop Alert!"; // 200+
        }

        return null; // below 25 → ignore
    }

    private String getRiseType(BigDecimal rise) {

        if (rise.compareTo(BigDecimal.valueOf(Dictionary.dropDown25OunceDropDown)) >= 0
                && rise.compareTo(BigDecimal.valueOf(Dictionary.dropDown50OunceDropDown)) < 0) {
            return "🔺 Small Rise Alert!"; // 25–49
        }

        if (rise.compareTo(BigDecimal.valueOf(Dictionary.dropDown50OunceDropDown)) >= 0
                && rise.compareTo(BigDecimal.valueOf(Dictionary.dropDown100OunceDropDown)) < 0) {
            return "🔺 Medium Rise Alert!"; // 50–99
        }

        if (rise.compareTo(BigDecimal.valueOf(Dictionary.dropDown100OunceDropDown)) >= 0
                && rise.compareTo(BigDecimal.valueOf(Dictionary.dropDown150OunceDropDown)) < 0) {
            return "🔺 Big Rise Alert!"; // 100–149
        }

        if (rise.compareTo(BigDecimal.valueOf(Dictionary.dropDown200OunceDropDown)) >= 0) {
            return  "🔺 Major Rise Alert!"; // 200+
        }

        return null; // below 25 → ignore
    }


    void wake() {
        String url = "https://disciplinary-maren-tanghai-2617c143.koyeb.app/health";
        restTemplate.getForObject(url, String.class);
    }
}
