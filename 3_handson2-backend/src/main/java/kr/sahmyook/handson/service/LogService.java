package kr.sahmyook.handson.service;

import kr.sahmyook.handson.util.MDCUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {

    private final ThreadPoolTaskExecutor taskExecutor;
    
    private final Random random = new Random();
    
    public void logTest(){
        log.info("test logging");
    }

    public void logLoop(String msg, int loopTimes, int term) {
        // 현재 스레드의 MDC 컨텍스트 복사
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        
        // 작업 제출 속도 제어를 위한 배치 크기
        int batchSize = 20;
        
        for (int i=0; i<loopTimes; i++) {
            final int index = i;
            try {
                // CompletableFuture를 사용하여 명시적으로 다른 스레드에서 실행
                CompletableFuture.runAsync(() -> {
                    processLog(msg, index, term, contextMap);
                }, taskExecutor);
                
                // 배치 크기마다 잠시 대기하여 작업 제출 속도 조절
                if (i > 0 && i % batchSize == 0) {
                    Thread.sleep(100);
                }
            } catch (RejectedExecutionException e) {
                log.warn("Task rejected, executing in the current thread: {}", e.getMessage());
                // 거부된 경우 현재 스레드에서 실행
                processLog(msg, index, term, contextMap);
            } catch (InterruptedException e) {
                log.error("Thread interrupted while throttling task submission", e);
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private void processLog(String msg, int index, int term, Map<String, String> contextMap) {
        // 부모 스레드의 MDC 컨텍스트를 현재 스레드에 설정
        if (contextMap != null) {
            MDC.setContextMap(contextMap);
        } else {
            // 컨텍스트가 없는 경우 새로 생성
            MDCUtil.setTraceAndSpanIds();
        }
        
        try {
            // 자식 스레드에 대한 새로운 span ID 생성
            MDCUtil.setSpanId();
            
            try {
                // Reduce sleep time to 300 milliseconds multiplier
                Thread.sleep((long) term * 300);
                
                // 현재 스레드 이름 로깅
                String threadName = Thread.currentThread().getName();
                
                // Occasionally log error (10% chance)
                if (random.nextInt(10) == 0) {
                    log.error("Thread: {}, Message: {}, Iteration: {}", threadName, msg, index);
                } else {
                    log.info("Thread: {}, Message: {}, Iteration: {}", threadName, msg, index);
                }
            } catch (InterruptedException e) {
                log.error("Thread interrupted", e);
                Thread.currentThread().interrupt();
            }
        } finally {
            // 스레드 작업이 끝나면 MDC 정리
            MDC.clear();
        }
    }
}