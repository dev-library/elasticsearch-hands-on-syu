FROM fluent/fluentd:v1.18.0-debian-1.3

USER root

# 시스템 패키지 업데이트 및 필요한 도구 설치
RUN apt-get update && apt-get install -y \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# ES 8.x와 완전히 호환되는 버전으로 고정 설치
RUN gem uninstall elasticsearch -aIx || true \
 && gem install elasticsearch -v 7.17.11 --no-document \
 && gem install fluent-plugin-elasticsearch -v 5.4.3 --no-document \
 && gem install oj --no-document

# 디렉토리 생성
RUN mkdir -p /fluentd/buffer /fluentd/error_logs \
 && chown -R fluent:fluent /fluentd/buffer /fluentd/error_logs

USER fluent