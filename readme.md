# 프로젝트 개요

## 1_cluster_setting

이 폴더는 다음을 위해 사용됩니다:

a. ElasticSearch 문법 및 Kibana 연동 학습
b. [이 레포지토리](https://github.com/dev-library/sd_day2_esbaseapp.git)에 있는 ElasticSearch 애플리케이션의 persistence 영역으로 활용

### 목적
이 설정의 목적은 ElasticSearch와 Kibana에 대한 기초적인 이해를 제공하고, 이러한 도구들을 실제 응용 프로그램에 통합하는 것입니다.

### Docker Compose 사용법
`1_cluster_setting` 디렉토리에서 환경을 설정하려면 다음 명령어를 실행하세요:
```bash
docker-compose up -d
```
환경을 내리려면 다음 명령어를 사용하세요:
```bash
docker-compose down
```

## 2_springboot_efk_integration_handson_setting

이 폴더는 다음을 위해 사용됩니다:

a. 로그 추적을 위한 EFK 스택 구성

### 목적
이 설정의 목적은 EFK(Elasticsearch, Fluentd, Kibana) 스택의 통합을 통해 로그 추적을 가능하게 하는 것입니다.

### Docker Compose 사용법
`2_springboot_efk_integration_handson_setting` 디렉토리에서 환경을 설정하려면 다음 명령어를 실행하세요:
```bash
docker-compose up -d
```
환경을 내리려면 다음 명령어를 사용하세요:
```bash
docker-compose down
```
