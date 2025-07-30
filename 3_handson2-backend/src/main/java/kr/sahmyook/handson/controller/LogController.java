package kr.sahmyook.handson.controller;

import kr.sahmyook.handson.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping("log")
    public ResponseEntity logTest(){
        logService.logTest();
        return ResponseEntity.ok("logging success\n");
    }

    @GetMapping("loop")
    public ResponseEntity loggingLoop(@RequestParam String msg, @RequestParam int loopTimes, @RequestParam int term) {
        // 비동기적으로 로그 작업 시작
        logService.logLoop(msg, loopTimes, term);
        // 즉시 응답 반환
        return ResponseEntity.accepted().body("Async logging started. Check logs for progress.\n");
    }
}
