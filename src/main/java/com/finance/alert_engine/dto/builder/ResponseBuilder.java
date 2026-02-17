package com.finance.alert_engine.dto.builder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBuilder<T> implements Serializable {

    @Serial
    @JsonIgnore
    private static final long serialVersionUID = -6627775308255795557L;

    @JsonProperty("trace_id")
    private String traceId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("data")
    private transient T data;

    @JsonIgnore
    private HttpStatus httpStatus;
}
