package com.finance.alert_engine.util.string;

import com.finance.alert_engine.dto.response.ForexCalendarResp;

import java.util.List;

public class Formatter {

    public static String formatForexCalendar(List<ForexCalendarResp> events) {
        StringBuilder sb = new StringBuilder();
        for (ForexCalendarResp e : events) {
            String impactLevel = "";
            if(e.getImpact().equals("𝙇𝙊𝙒")) {
                impactLevel = "🟢";
            } else if(e.getImpact().equals("𝙃𝙄𝙂𝙃")) {
                impactLevel = "🔴" ;
            } else {
                impactLevel = "🟠";
            }
            sb.append("📅 ").append(e.getDate()).append("\n")
                    .append("| ចំណងជើង: ").append(e.getTitle()).append("\n")
                    .append("| រូបិយប័ណ្ណ: ").append(e.getCountry()).append("\n")
                    .append("| ផលប៉ះពាល់: ").append(e.getImpact()).append(" ").append(impactLevel).append("\n")
                    .append("| ទិន្ន័យចាស់: ").append(e.getPrevious() != null ? e.getPrevious() : "-").append("\n")
                    .append("| ការទស្សន៍ទាយ: ").append(e.getForecast() != null ? e.getForecast() : "-")
                    .append("\n\n\n");
        }
        return sb.toString().trim();
    }

    static double calculateToLocalPrice(double price) {
        return price * 1.2;
    }


    private static double safeGet(Double value) {
        return value != null ? value : 0.0;
    }

    private static String formatDiff(double value) {
        if (value == 0) return "—";
        return (value > 0 ? "+" : "") + String.format("%.2f", value);
    }


    public static String assetRegisterTemplate() {
        StringBuilder sb = new StringBuilder();
        sb.append("*asset:").append("\n")
        .append("amount: ").append("\n")
        .append("converted: ").append("\n")
        .append("symbol: ").append("\n")
        .append("exchange: ").append("\n")
        .append("network_type: ").append("\n")
        .append("network_fee: ").append("\n")
        .append("buy_at: ").append("\n");
        return sb.toString();
    }
}