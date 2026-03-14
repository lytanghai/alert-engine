package com.finance.alert_engine.util.date;

import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtilz {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static Date fromUnixSeconds(long seconds) {
        return new Date(seconds * 1000);
    }

}
