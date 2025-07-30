# Workshop2 애플리케이션

## Fluentd 로그 전송 설정

이 애플리케이션은 로그를 Fluentd 서버로 전송하도록 설정되어 있습니다.

### 로컬 환경에서 Fluentd 실행하기

1. Docker와 Docker Compose가 설치되어 있는지 확인합니다.

2. 프로젝트 루트 디렉토리에서 다음 명령어를 실행하여 Fluentd 컨테이너를 시작합니다:

```bash
docker-compose up -d
```

3. 로그 디렉토리를 생성합니다:

```bash
mkdir -p logs
```

4. 애플리케이션을 실행합니다:

```bash
./gradlew bootRun
```

### Fluentd 설정 변경하기

Fluentd 설정을 변경하려면 다음 파일들을 수정하세요:

- `application.properties`: Fluentd 서버 호스트, 포트, 태그 설정
- `fluentd.conf`: Fluentd 서버 설정
- `src/main/resources/logger/logback-default.xml`: Logback Fluentd Appender 설정

### 운영 환경에서 설정하기

운영 환경에서는 다음과 같이 설정하세요:

1. 운영 환경의 Fluentd 서버 정보로 `application.properties` 파일을 업데이트합니다:

```properties
fluentd.host=your-fluentd-server
fluentd.port=24224
fluentd.tag=workshop2
```

2. 애플리케이션을 다시 시작하여 변경사항을 적용합니다. 