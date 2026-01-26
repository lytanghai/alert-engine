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

        telegramService.sendMessage("Application has lived!");

        if(objectCache.getAll().isEmpty())
            log.info("no previous prices found");

        XauResponse xauResponse = restTemplate.getForObject(Dictionary.xau_url, XauResponse.class);

        if(xauResponse == null) return;

        BigDecimal latestPrice = xauResponse.getPrice();

        List<BigDecimal> lastTwo = objectCache.getLastN(1);

        if (lastTwo.size() == 1) {
            BigDecimal previousPrice = lastTwo.get(0); // second-to-last
            BigDecimal difference = latestPrice.subtract(previousPrice).abs(); // absolute value

            if (difference.compareTo(BigDecimal.valueOf(Dictionary.setOunceDropDown)) > 0) {
                telegramService.sendMessage("DROP DOWN 25");
                log.info("Price changed more than 25! Previous: {}, Latest: {}", previousPrice, latestPrice);
            } else {
                log.info("Price change within range. Previous: {}, Latest: {}", previousPrice, latestPrice);
            }
        }
        objectCache.add(latestPrice);

    }
}
