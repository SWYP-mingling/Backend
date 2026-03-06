# 🤝 Mingling Backend

**Mingling**은 약속 및 모임 참여자들의 **출발역을 기반으로 최적의 중간 지점을 추천**하고, 모임 목적에 맞는 장소를 탐색할 수 있도록 지원하는 백엔드 서버입니다.

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
````

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
````

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

## ⚙️ Getting Started

### Prerequisites

- JDK 26
- MariaDB (Local or Docker)

### Installation & Run

1. **Repository Clone**

```bash
git clone [https://github.com/SWYP-mingling/Backend.git](https://github.com/SWYP-mingling/Backend.git)

```

2. **Build**

```bash
./gradlew build

```

3. **Run Application**

```bash
./gradlew bootRun

```

---

## 📄 API Documentation

상세한 API 명세 및 테스트는 아래 Swagger UI 링크를 참조하세요.
👉 **[Mingling Swagger UI 바로가기](https://test-api.mingling.kr/swagger-ui/index.html)**
