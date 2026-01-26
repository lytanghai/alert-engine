package com.finance.alert_engine.dto;

import com.finance.alert_engine.util.DateUtil;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class XauResponse {

    private String name;
    private BigDecimal price;
    private String symbol;
    private String timestamp = DateUtil.convertDate();

}
