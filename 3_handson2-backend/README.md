# handson 애플리케이션


## 목적
이 애플리케이션은 2번 핸즈온을 실행하는 데 필요한 백엔드 애플리케이션입니다. 주로 로그 추적 및 분석을 위한 EFK 스택과의 통합을 목표로 합니다.

## 설정
1. `2_springboot_efk_integration_handson_setting` 폴더의 EFK 스택을 먼저 설정하고 실행합니다.
2. EFK 스택이 정상적으로 실행된 후, 이 백엔드 애플리케이션을 실행합니다.

## 연동 순서
1. EFK 스택 설정 및 실행
2. 백엔드 애플리케이션 실행

## 특성
- Spring Boot 기반의 애플리케이션으로, 로그 데이터를 EFK 스택으로 전송하여 실시간으로 로그를 추적하고 분석할 수 있습니다.
- Fluentd를 통해 로그가 Elasticsearch로 전송되며, Kibana를 통해 시각화할 수 있습니다.


### 의존성
- **Spring Boot**: 애플리케이션의 기본 프레임워크로 사용됩니다.
- **Spring Scheduling**: 비동기 작업을 위한 `AsyncConfigurer`와 `ThreadPoolTaskExecutor`를 설정합니다.
- **Logback**: 로그를 관리하고, `FluentdHttpAppender`를 통해 Fluentd로 로그를 전송합니다.
- **Jakarta Servlet**: `MDCFilter`에서 HTTP 요청을 필터링하는 데 사용됩니다.
- **Jackson**: JSON 직렬화를 위해 사용됩니다.
- **Java HTTP Client**: Fluentd로 로그를 전송하기 위해 사용됩니다.

### 내부 호출 구조
- **`HandsonApplication`**: Spring Boot 애플리케이션의 진입점입니다.
- **`LogController`**: HTTP 요청을 처리하며, `LogService`를 호출하여 로그 작업을 수행합니다.
  - `logTest()`: 단일 로그 테스트를 수행합니다.
  - `loggingLoop()`: 비동기적으로 여러 번의 로그 작업을 수행합니다.
- **`LogService`**: 실제 로그 작업을 수행하는 서비스 계층입니다.
- **`AsyncConfig`**: 비동기 작업을 위한 스레드 풀을 설정합니다.
- **`MDCFilter`**: 각 요청에 대해 trace ID와 span ID를 생성하고, 응답 헤더에 추가합니다.
- **`FluentdHttpAppender`**: 로그 이벤트를 Fluentd로 전송하는 커스텀 Appender입니다.

### 로그 저장 원리
- **로그 생성**: `LogController`에서 로그 요청을 받으면 `LogService`를 통해 로그가 생성됩니다.
- **MDC 사용**: `MDCFilter`를 통해 각 요청에 대해 고유한 trace ID와 span ID가 생성되어 로그에 포함됩니다.
- **Fluentd 전송**: `FluentdHttpAppender`는 로그 이벤트를 JSON 형식으로 직렬화하여 Fluentd로 전송합니다.
- **Fluentd 수집 및 저장**: Fluentd는 수신한 로그를 Elasticsearch에 저장하며, Kibana를 통해 시각화할 수 있습니다.

### 로그백 설정
- **파일 저장**: 로그는 `RollingFileAppender`를 사용하여 `log/simplelogging.log`에 저장됩니다. 로그 파일은 날짜별로 압축되어 저장됩니다.
- **콘솔 출력**: `ConsoleAppender`를 통해 콘솔에 로그가 출력됩니다. 로그에는 trace ID와 span ID가 포함됩니다.
- **Fluentd 전송**: `FluentdHttpAppender`를 사용하여 로그가 Fluentd로 전송됩니다. Fluentd URL과 태그는 Spring 설정에서 가져옵니다.
- **루트 로그 레벨**: 로그 레벨은 `INFO`로 설정되어 있으며, 파일, 콘솔, Fluentd로 로그가 전송됩니다.

### Zipkin을 통한 분산 추적
- **MDC 사용**: `MDCFilter`를 통해 각 요청에 대해 trace ID와 span ID가 생성됩니다. 이는 로그에 포함되어 Zipkin을 통해 분산 추적이 가능합니다.
- **분산 추적 목적**: Zipkin을 사용하여 애플리케이션의 분산된 서비스 간의 호출 관계를 추적하고, 성능 병목 현상을 분석할 수 있습니다.

