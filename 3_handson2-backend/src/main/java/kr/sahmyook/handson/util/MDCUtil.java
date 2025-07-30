package kr.sahmyook.handson.util;

import org.slf4j.MDC;

import java.util.UUID;

public class MDCUtil {
    
    public static final String TRACE_ID = "traceId";
    public static final String SPAN_ID = "spanId";
    
    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    public static String generateSpanId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
    
    public static void setTraceId() {
        MDC.put(TRACE_ID, generateTraceId());
    }
    
    public static void setSpanId() {
        MDC.put(SPAN_ID, generateSpanId());
    }
    
    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }
    
    public static String getSpanId() {
        return MDC.get(SPAN_ID);
    }
    
    public static void setTraceAndSpanIds() {
        setTraceId();
        setSpanId();
    }
    
    public static void clearTraceAndSpanIds() {
        MDC.remove(TRACE_ID);
        MDC.remove(SPAN_ID);
    }
} 