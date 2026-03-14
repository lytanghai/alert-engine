package com.finance.alert_engine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NotificationUpdateRequest {

    @JsonProperty("message_ids")
    private List<Integer> messageIds;
}
