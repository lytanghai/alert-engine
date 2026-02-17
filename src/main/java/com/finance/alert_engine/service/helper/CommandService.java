package com.finance.alert_engine.service.helper;

import com.finance.alert_engine.constants.Dictionary;
import com.finance.alert_engine.dto.response.ForexCalendarResp;
import com.finance.alert_engine.service.cache.ForexCalendarCache;
import com.finance.alert_engine.service.provider.TelegramService;
import com.finance.alert_engine.util.date.DateUtilz;
import com.finance.alert_engine.util.string.Formatter;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CommandService {

    private static final ForexCalendarCache cache = new ForexCalendarCache();

    private TelegramService telegram;
    public void botResponse(String command, String userId) {
        if(command.contains("$") && Dictionary.admin.equals(userId)) {
            String trimResponse= command.replaceAll("\\$","");
            switch (trimResponse) {
                case "evt":
                    telegram.sendMessage(Formatter.formatForexCalendar(economicCalendar()));
                    break;
                case "pft":
                    break;

                default: return;
            }
        }

    }

    public static List<ForexCalendarResp> economicCalendar() {
        // 1️⃣ Return from cache if available
        if (!cache.getAll().isEmpty()) {
            System.out.println("from cache");
            return cache.getAll();
        }

        List<ForexCalendarResp> responseList = new ArrayList<>();
        String current = DateUtilz.format(new Date(), "yyyy-MM-dd");

        // 3️⃣ If no JSON file or error, fallback to API
        try {
            RestTemplate restTemplate = new RestTemplate();
            String resultStr = restTemplate.getForObject(Dictionary.calendar, String.class);
            JSONArray result = new JSONArray(resultStr);

            for (Object i : result) {
                JSONObject each = (JSONObject) i;
                if (each.optString("date").substring(0, 10).equals(current) &&
                        each.optString("country").equals("USD")) {

                    ForexCalendarResp eachResp = new ForexCalendarResp();
                    eachResp.setDate(DateUtilz.toPhnomPenhTime(each.optString("date").replaceAll("ICT", "")));
                    eachResp.setCountry(each.optString("country"));
                    eachResp.setForecast(each.optString("forecast"));
                    eachResp.setImpact(each.optString("impact"));
                    eachResp.setTitle(each.optString("title"));
                    eachResp.setPrevious(each.optString("previous"));

                    responseList.add(eachResp);
                    cache.put(eachResp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseList;
    }
}
