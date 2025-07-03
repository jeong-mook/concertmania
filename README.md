# 🎟️ CONCERT MANIA - 콘서트 예약 시스템 백엔드

## 프로젝트 개요

CONCERT MANIA는 수천 명의 동시 접속자가 몰리는 인기 콘서트 티켓 예매 플랫폼의 백엔드 시스템입니다.
좌석 선점 경쟁, 임시 예약 시간 제한, 결제 연동, 대기열 관리 등 복잡한 비즈니스 로직을 Spring Boot 기반으로 구현하여, 실제 서비스 환경에서 요구되는 백엔드 역량을 평가하는 과제 프로젝트입니다.

---

## 주요 기능

* 회원가입 및 로그인, 권한 기반 접근 제어 (일반 사용자 / 관리자)
* 콘서트 정보 및 좌석 관리 (좌석 배치도, 등급별 가격 설정)
* 실시간 좌석 상태 조회 및 임시 점유 (10분 제한)
* 동시성 제어를 통한 좌석 선점 경쟁 방지
* 결제 API 연동 및 실패 처리 (SAGA 패턴 적용)
* 예매 오픈 전 대기열 시스템 및 순차 입장 처리
* 결제 완료 후 이메일/SMS 알림 발송 (비동기 처리)
* 애플리케이션 모니터링 및 구조화된 로깅

---

## 기술 스택

* Language: Java 17
* Framework: Spring Boot 3.x
* Database: MySQL
* 캐싱: Redis, Caffeine
* 메시징: Kafka
* 인증/인가: Spring Security, JWT
* API 문서: Swagger / OpenAPI 3.0
* 테스트: JUnit 5, MockMvc, TestContainers
* 컨테이너: Docker, Docker Compose
* 모니터링: Micrometer, ELK 스택 (Elasticsearch, Logstash, Kibana)

---

## 아키텍처 및 설계

* 모놀리식 + DDD 스타일 패키지 구조 적용
* 도메인 별 책임 분리 및 이벤트 기반 비동기 처리
* 동시성 제어를 위한 Pessimistic Lock 및 Redis 분산 락 병행 적용
* SAGA 패턴을 통한 결제 실패 시 좌석 점유 보상 트랜잭션 처리

---

## 실행 방법

### 1. 환경 준비

* Docker, Docker Compose 설치
* `docker-compose.yml` 파일로 MySQL, Kafka, Redis 등 인프라 컨테이너 실행

```bash
docker-compose up -d
```

### 2. 애플리케이션 빌드 및 실행

```bash
./gradlew clean build
java -jar build/libs/concertmania.jar
```

### 3. API 문서 확인

Swagger UI 접속:

```
http://localhost:8080/swagger-ui.html
```

---

## 주요 API 예시

* `POST /api/users/signup` : 회원가입
* `POST /api/users/login` : 로그인 (JWT 발급)
* `GET /api/concerts` : 콘서트 리스트 조회
* `POST /api/reservations` : 좌석 임시 점유 및 예약 신청
* `POST /api/payments` : 결제 요청

(자세한 API 명세는 Swagger UI 참고)

---

## 테스트

* 단위 테스트 및 통합 테스트 포함
* 부하 테스트 시나리오 (JMeter, K6 등 별도 제공 예정)

---

## 프로젝트 구조

```
com.concertmania
├── domain
│   ├── user
│   ├── concert
│   ├── reservation
│   ├── payment
│   ├── queue
│   └── notification
├── infrastructure
│   ├── config
│   ├── external
│   └── persistence
└── global
    ├── dto
    ├── error
    ├── security
    └── util
```

---

## 기타

* 결제 실패 시 SAGA 패턴으로 좌석 점유 자동 해제 및 예약 취소 처리
* Redis와 Caffeine 캐싱 병행 적용으로 성능 최적화
* Kafka를 활용한 이벤트 기반 아키텍처 및 알림 처리