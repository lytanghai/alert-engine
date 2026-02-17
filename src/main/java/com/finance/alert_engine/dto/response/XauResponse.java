package com.finance.alert_engine.dto.response;

import com.finance.alert_engine.util.date.DateUtilz;
import lombok.Data;

@Data
public class XauResponse {

    private String name;
    private Double price;
    private String symbol;
    private String timestamp = DateUtilz.convertDate();

}
