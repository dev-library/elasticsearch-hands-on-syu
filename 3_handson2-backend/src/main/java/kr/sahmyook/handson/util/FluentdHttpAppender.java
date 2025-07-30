package kr.sahmyook.handson.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FluentdHttpAppender extends AppenderBase<ILoggingEvent> {
    
    private String fluentdUrl;
    private String tag;
    private HttpClient httpClient;
    private ObjectMapper objectMapper;
    private ExecutorService executorService;
    
    public FluentdHttpAppender() {
        System.out.println("FluentdHttpAppender constructor called");
    }
    
    public void setFluentdUrl(String fluentdUrl) {
        System.out.println("Setting FluentdUrl: " + fluentdUrl);
        this.fluentdUrl = fluentdUrl;
    }
    
    public void setTag(String tag) {
        System.out.println("Setting Tag: " + tag);
        this.tag = tag;
    }
    
    @Override
    public void start() {
        if (fluentdUrl == null || tag == null) {
            addError("FluentdUrl and Tag must be set. URL: " + fluentdUrl + ", Tag: " + tag);
            return;
        }
        
        addInfo("Starting FluentdHttpAppender with URL: " + fluentdUrl + ", Tag: " + tag);
        
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper();
        this.executorService = Executors.newFixedThreadPool(2);
        
        addInfo("FluentdHttpAppender started successfully");
        super.start();
    }
    
    @Override
    public void stop() {
        if (executorService != null) {
            executorService.shutdown();
        }
        super.stop();
    }
    
    @Override
    protected void append(ILoggingEvent event) {
        if (!isStarted()) {
            return;
        }
        
        // 비동기적으로 로그 전송
        executorService.submit(() -> {
            try {
                Map<String, Object> logData = new HashMap<>();
                logData.put("timestamp", event.getTimeStamp() / 1000.0);
                logData.put("level", event.getLevel().toString());
                logData.put("logger", event.getLoggerName());
                logData.put("thread", event.getThreadName());
                logData.put("message", event.getFormattedMessage());
                logData.put("service", "workshop2");
                
                // MDC 정보 추가
                if (event.getMDCPropertyMap() != null) {
                    logData.putAll(event.getMDCPropertyMap());
                }
                
                String jsonBody = objectMapper.writeValueAsString(logData);
                
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(fluentdUrl + "/" + tag))
                        .header("Content-Type", "application/json")
                        .timeout(Duration.ofSeconds(10))
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .build();
                
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .whenComplete((response, throwable) -> {
                            if (throwable != null) {
                                addWarn("Failed to send log to Fluentd: " + throwable.getMessage());
                            } else {
                                addInfo("Log sent to Fluentd successfully. Status: " + response.statusCode());
                            }
                        });
                        
            } catch (Exception e) {
                addWarn("Error processing log event: " + e.getMessage());
            }
        });
    }
} 