package com.finance.alert_engine.dto;

import com.finance.alert_engine.util.DateUtil;
import lombok.Data;

@Data
public class XauResponse {

    private String name;
    private Double price;
    private String symbol;
    private String timestamp = DateUtil.convertDate();

}
