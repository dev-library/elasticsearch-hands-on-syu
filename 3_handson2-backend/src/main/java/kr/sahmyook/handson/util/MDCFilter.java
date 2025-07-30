package kr.sahmyook.handson.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class MDCFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 요청이 들어올 때마다 새로운 trace ID와 span ID 생성
            MDCUtil.setTraceAndSpanIds();
            
            // 응답 헤더에 trace ID 추가
            response.addHeader("X-Trace-Id", MDCUtil.getTraceId());
            response.addHeader("X-Span-Id", MDCUtil.getSpanId());
            
            filterChain.doFilter(request, response);
        } finally {
            // 요청 처리가 끝나면 MDC 정보 정리
            MDCUtil.clearTraceAndSpanIds();
        }
    }
} 