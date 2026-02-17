package com.finance.alert_engine.dto.response;

import com.finance.alert_engine.dto.builder.ResponseBuilder;
import com.finance.alert_engine.util.TraceUtil;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor
public class ResponseMessage {

    private final String SUCCESS = "SUCCESS";
    private final Tracer tracer;

    @ResponseStatus(HttpStatus.OK)
    public <T> ResponseBuilder<T> success() {
        return ResponseBuilder.<T>builder()
                .traceId(TraceUtil.getTraceId(tracer))
                .message(SUCCESS)
                .errorCode(SUCCESS)
                .data(null)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    public <T> ResponseBuilder<T> success(T data) {

        return ResponseBuilder.<T>builder()
                .traceId(TraceUtil.getTraceId(tracer))
                .message(SUCCESS)
                .errorCode(SUCCESS)
                .data(data)
                .build();
    }

}
