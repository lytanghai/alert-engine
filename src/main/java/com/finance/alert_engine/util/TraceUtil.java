package com.finance.alert_engine.util;


import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class TraceUtil {

    public String getTraceId(Tracer tracer) {
        return Optional.ofNullable(tracer)
                .map(Tracer::currentSpan)
                .map(Span::context)
                .map(TraceContext::traceId)
                .orElse(generateUniqueId());
    }

    public String getSpanId(Tracer tracer) {
        return Optional.ofNullable(tracer)
                .map(Tracer::currentSpan)
                .map(Span::context)
                .map(TraceContext::spanId)
                .orElse(generateUniqueId());
    }

    public static String generateUniqueId() {
        String uniqueID = UUID.randomUUID().toString().replace("-", "");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(new Date()) + uniqueID + " ";
    }
}
