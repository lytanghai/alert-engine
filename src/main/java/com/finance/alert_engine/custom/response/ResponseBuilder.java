package com.finance.alert_engine.custom.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NotBlank
public class ResponseBuilder<T> {

    private String status;
    private String code;
    private T data;

    public static <T> ResponseBuilder<T> success(T data) {
        return new ResponseBuilder<>("SUCCESS", "200", data);
    }

    public static <T> ResponseBuilder<T> failure(String code, T data) {
        return new ResponseBuilder<>("FAILURE", code, data);
    }
}