package com.finance.alert_engine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForexCalendarResp {
    private String title;
    private String country;
    private String date;
    private String impact;
    private String forecast;
    private String previous;

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) Impact: %s | Forecast: %s | Previous: %s",
                date, title, country, impact, forecast, previous);
    }
}