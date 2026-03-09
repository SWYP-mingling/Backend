# 🤝 Mingling Backend
<p align="center">
  <img src="https://github.com/user-attachments/assets/beb03991-6522-4caf-bfb7-028ea1cccf59" width="600"/>
</p>

<p align="center">
  <a href="https://mingling.kr">🌐 밍글링 바로가기</a>
</p>
**Mingling**은 약속 및 모임 참여자들의 **출발역을 기반으로 최적의 중간 지점을 추천**하고, 모임 목적에 맞는 장소를 탐색할 수 있도록 지원하는 백엔드 서버입니다.

---

## ✨ Features
| 기능 | 화면 |
|:---:|-----|
| 모임 생성 | <img src="https://github.com/user-attachments/assets/edad101d-e82c-4abb-ad8f-dc99de97da15" width="600"/> |
| 모임 참여 | <img src="https://github.com/user-attachments/assets/22de76ac-7330-4967-9a11-9533b43933d1" width="600"/> |
| 출발지 등록 | <img src="https://github.com/user-attachments/assets/7253df65-81b9-44e2-a611-b9d3253f70f5" width="600"/> |
| 중간지점 결과보기 | <img src="https://github.com/user-attachments/assets/a161fd4e-fa04-4b48-9f11-5775e78d553c" width="600"/> |
| 장소 추천 | <img src="https://github.com/user-attachments/assets/0ec6302a-a7ef-49eb-93ff-d621307f8a44" width="600"/> |

---

## 🛠 Tech Stack

### Language

![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?style=flat&logo=openjdk&logoColor=white)
### Framework

![Spring Boot](https://img.shields.io/badge/-Spring%20Boot-brightgreen?logo=spring&logoColor=white)

### Database

![MariaDB](https://img.shields.io/badge/MySQL-black?&logo=mariadb&logoColor=white)

### Documentation
![Swagger](https://img.shields.io/badge/Swagger-UI)

---

## 아키텍처

<img width="1478" height="815" alt="Image" src="https://github.com/user-attachments/assets/8c36a44c-4e4d-4128-9ef3-c9ca59203e38" />

### 📊 Entity Relationship Diagram (ERD)

<img width="688" height="531" alt="Image" src="https://github.com/user-attachments/assets/7fe10a00-b2b7-4bc3-9d9a-e5d1971a9554" />

## 🚀 API Specification

### 📦 Common Response Format

모든 API 응답은 아래의 공통 규격을 따릅니다.

**✅ Success Response**

```json
{
  "success": true,
  "data": {
    "items": [],
    "page": 1
  },
  "timestamp": "2026-03-06T14:00:00Z"
}
```

**❌ Failure Response**

```json
{
  "success": false,
  "code": "INVALID_TOKEN",
  "message": "토큰이 유효하지 않습니다.",
  "data": null,
  "timestamp": "2026-03-06T14:00:00Z"
}
```

---

## 🌿 Branch Strategy

브랜치 생성 시 아래 규칙을 준수하며, 작업 단위별로 명확히 분리합니다.

> **Format**: `<type>/<jira-issue-number>-<short-description>`

| Type         | Description             | Example                           |
| ------------ | ----------------------- | --------------------------------- |
| **feature**  | 신규 기능 개발          | `feature/SW-10-jwt-refresh-token` |
| **fix**      | 버그 수정               | `fix/SW-21-user-profile-error`    |
| **refactor** | 코드 리팩토링           | `refactor/SW-35-order-validation` |
| **chore**    | 설정, 환경 및 기타 작업 | `chore/SW-01-setup-gradle`        |

---

## 💬 Commit Message Convention

**AngularJS Git Commit Convention**을 따르며, `subject`는 **한국어**로 명확하게 작성합니다.

> **Format**: `[issue-number] <type>: <subject>`

- **feat**: 새로운 기능 추가
- **fix**: 버그 수정
- **docs**: 문서 수정 (README, Swagger 등)
- **style**: 코드 포맷팅 (로직 변경 없음)
- **refactor**: 코드 리팩토링
- **test**: 테스트 코드 추가 및 수정
- **chore**: 빌드 업무, 패키지 설정 등

**💡 Example:**
`[SW-94] fix: 장소 추천 API 런타임 오류 수정`

---

## 🌐 Deployment

### 🏗 Service Architecture
전체적인 시스템 구조는 **Nginx**를 리버스 프록시로 활용하며, 애플리케이션 서버와 데이터베이스 모두 **Docker 컨테이너**로 독립 운영됩니다. 서비스의 안정적인 운영을 위해 로그 데이터는 호스트 서버와 볼륨 마운트를 통해 영구 보관됩니다.

* **Web Server**: Nginx (Reverse Proxy)
* **Application Server**: Docker Containers (Spring Boot 3.x)
* **Database**: Docker Container (MariaDB 10.11+)
* **Log Management**: Host-Container Volume Mapping (`/logs`)

---

### 💻 Infrastructure Detail
| Infrastructure | Detail |
| :--- | :--- |
| **Cloud** | **NCP (Naver Cloud Platform)** |
| **Instance** | Micro Server (Ubuntu 22.04 LTS) |
| **Database** | MariaDB 10.11 (Dockerized) |
| **Container** | Docker, Docker-compose |

---

### 🚀 CI/CD Pipeline
GitHub Actions와 NCP Container Registry를 연동하여 배포 자동화를 구축했습니다.

1.  **GitHub Actions**: `main` 브랜치에 코드 Push 시 빌드 및 테스트 자동 수행
2.  **NCP Container Registry (NCR)**: 빌드된 이미지를 NCP 전용 컨테이너 저장소에 Push 및 관리
3.  **Deployment Flow**:
    * GitHub Actions에서 프로젝트 빌드 (Gradle)
    * Docker 이미지 생성 후 **NCP Container Registry**로 Push
    * 대상 서버에 SSH 접속 후 최신 이미지 `pull` 및 `docker-compose` 재실행

---

### 💾 Log Management
컨테이너 재배포 시에도 과거의 에러 기록을 보존하기 위해 호스트 서버의 파일 시스템과 동기화하여 관리합니다.

* **로그 보관 경로**: `/home/ncp-user/mingling-logs` (Host) ↔ `/logs` (Container)
* **로그 파일 구성**:
    * `error.log`: 모든 **ERROR** 레벨 로그 기록 (서비스 장애 추적용)
    * `warn.log`: **WARN** 이상 레벨 로그 기록 (잠재적 문제 모니터링용)
* **모니터링**: 서버 터미널에서 `tail -f` 명령어를 통해 실시간으로 시스템 상태를 확인할 수 있습니다.

---

## 📄 API Documentation

상세한 API 명세 및 테스트는 아래 Swagger UI 링크를 참조하세요.
👉 **[Mingling Swagger UI 바로가기](https://test-api.mingling.kr/swagger-ui/index.html)**
