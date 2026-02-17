package com.finance.alert_engine.util.date;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtilz {

    public static final String DATE_WITH_TIME_1 = "dd-MM-yyyy HH:mm:ss";

    public static final String DATE_FORMAT_3 = "yyyy-MM-dd";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


    public static String convertDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy:HH:mm:ss");
        return now.format(formatter);
    }
    public static String format(Date date) {
        return format(date, DATE_WITH_TIME_1);
    }


    public static String format(Date date, String format) {
        return format(date, format, null);
    }

    public static String format(Date date, String format, String defaultValue) {
        return date == null ? defaultValue : new SimpleDateFormat(format).format(date);
    }

    public static String toPhnomPenhTime(String input) {
        ZonedDateTime sourceDateTime = ZonedDateTime.parse(input);

        // Convert to Asia/Phnom_Penh time zone
        ZonedDateTime phnomPenhTime = sourceDateTime.withZoneSameInstant(ZoneId.of("Asia/Phnom_Penh"));

        // Format output
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        return phnomPenhTime.format(formatter);
    }
}
