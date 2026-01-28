# Mingling API 문서 (프론트엔드용)

## 📋 목차
- [공통 사항](#공통-사항)
- [모임 API](#모임-api)
- [참여자 API](#참여자-api)
- [에러 코드 전체 목록](#에러-코드-전체-목록)

---

## 공통 사항

### 기본 URL
```
Base URL: http://localhost:8888
```

### 공통 응답 형식

모든 API는 다음 형식으로 응답합니다:

#### 성공 응답
```json
{
    "success": true,
    "code": null,
    "message": null,
    "data": { ... },
    "timestamp": "2026-01-29T10:30:00"
}
```

#### 에러 응답
```json
{
    "success": false,
    "code": "ERROR_CODE",
    "message": "에러 메시지",
    "data": null,
    "timestamp": "2026-01-29T10:30:00"
}
```

#### 검증 에러 응답 (여러 필드 에러 시)
```json
{
    "success": false,
    "code": "VALIDATION_ERROR",
    "message": "요청 값이 유효하지 않습니다.",
    "data": {
        "deadline": "마감 시간은 현재 시간 이후여야 합니다."
    },
    "timestamp": "2026-01-29T10:30:00"
}
```

### 인증 방식
- 세션 기반 인증 사용
- 쿠키에 `nickname`, `fakeSessionId` 포함
- 인증 필요한 API는 각 섹션에서 명시

---

## 모임 API

### 1. 모임 생성

새로운 모임을 생성합니다.

**엔드포인트**
```
POST /meeting
```

**인증 필요**: ❌ 없음

**Request Body**
```json
{
    "meetingName": "신년회",
    "purposes": ["친목", "회식"],
    "purposeCount": 2,
    "capacity": 10,
    "deadline": "2026-01-30T23:59:59"
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| meetingName | String | ✅ | 모임 이름 |
| purposes | Array[String] | ✅ | 모임 목적 리스트 (친목, 회식, 스터디 등) |
| purposeCount | Integer | ✅ | 모임 목적 개수 |
| capacity | Integer | ✅ | 모임 정원 (최소 2명 이상) |
| deadline | DateTime | ✅ | 마감 시간 (ISO 8601 형식, 미래 시간이어야 함) |

**Success Response (200 OK)**
```json
{
    "success": true,
    "data": {
        "meetingUrl": "https://mingling.com/meeting/62db1c35-f7db-4aad-acc8-0ad64f61a312",
        "meetingId": "62db1c35-f7db-4aad-acc8-0ad64f61a312"
    },
    "timestamp": "2026-01-29T10:30:00"
}
```

**Error Responses**

| HTTP Status | Error Code | Message | 발생 조건 |
|-------------|------------|---------|-----------|
| 400 | VALIDATION_ERROR | 요청 값이 유효하지 않습니다. | 검증 실패 (우선순위 순으로 하나만 반환) |
| 400 | PURPOSE_NOT_FOUND | 일부 모임 목적을 찾을 수 없습니다. | 정의되지 않은 목적명 포함 |

**검증 에러 우선순위**
1. `deadline` - 마감 시간은 현재 시간 이후여야 합니다.
2. `capacity` - 모임 인원은 최소 2명 이상이어야 합니다.
3. 기타 필수 필드

**검증 에러 예시**
```json
{
    "success": false,
    "code": "VALIDATION_ERROR",
    "message": "요청 값이 유효하지 않습니다.",
    "data": {
        "deadline": "마감 시간은 현재 시간 이후여야 합니다."
    },
    "timestamp": "2026-01-29T10:30:00"
}
```

---

### 2. 모임 참여 현황 조회

모임의 전체 정원, 현재 참여자 수, 참여자 목록을 조회합니다.

**엔드포인트**
```
GET /meeting/{meetingId}/status
```

**인증 필요**: ✅ 세션 필요

**Path Parameters**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| meetingId | UUID | ✅ | 모임 식별자 |

**Success Response (200 OK)**
```json
{
    "success": true,
    "data": {
        "totalParticipantCount": 10,
        "currentParticipantCount": 2,
        "deadlineAt": "2026-01-23T23:00:00",
        "participants": [
            {
                "userName": "김밍글",
                "stationName": "구로디지털단지역",
                "latitude": 37.485266,
                "longitude": 126.901401
            },
            {
                "userName": "이밍글",
                "stationName": "합정역",
                "latitude": 37.549556,
                "longitude": 126.913878
            }
        ]
    },
    "timestamp": "2026-01-29T10:30:00"
}
```

**Error Responses**

| HTTP Status | Error Code | Message | 발생 조건 |
|-------------|------------|---------|-----------|
| 401 | USER_UNAUTHORIZED | 사용자 인증에 실패했습니다. | 세션 없음 또는 만료 |
| 404 | MEETING_NOT_FOUND | 모임을 찾을 수 없습니다. | 존재하지 않는 meetingId |

---

### 3. 중간지점 조회

모임 참여자들의 중간지점 후보들을 조회합니다.

**엔드포인트**
```
GET /meeting/{meetingId}/midpoint
```

**인증 필요**: ✅ 세션 필요

**Path Parameters**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| meetingId | UUID | ✅ | 모임 식별자 |

**Session Attributes**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| userName | String | ✅ | 세션에 저장된 사용자 이름 |

**Success Response (200 OK)**
```json
{
    "success": true,
    "data": {
        "midpoints": [
            {
                "name": "합정역",
                "latitude": 37.5484757,
                "longitude": 126.912071,
                "avgTravelTime": 30,
                "transferPath": "2호선 > 6호선"
            },
            {
                "name": "서울역",
                "latitude": 37.554648,
                "longitude": 126.972559,
                "avgTravelTime": 35,
                "transferPath": "1호선 > 4호선"
            }
        ]
    },
    "timestamp": "2026-01-29T10:30:00"
}
```

**Error Responses**

| HTTP Status | Error Code | Message | 발생 조건 |
|-------------|------------|---------|-----------|
| 401 | USER_UNAUTHORIZED | 사용자 인증에 실패했습니다. | 세션 없음 또는 만료 |
| 404 | MEETING_NOT_FOUND | 모임을 찾을 수 없습니다. | 존재하지 않는 meetingId |

---

### 4. 장소 추천

중간지점 근처의 모임 목적에 맞는 장소를 추천합니다.

**엔드포인트**
```
GET /meeting/{meetingId}/recommend?midPlace={중간지점명}&category={모임목적}
```

**인증 필요**: ✅ 세션 필요

**Path Parameters**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| meetingId | UUID | ✅ | 모임 식별자 |

**Query Parameters**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| midPlace | String | ✅ | 중간 지점 장소명 (예: "합정역") |
| category | String | ✅ | 모임 목적 (예: "회식") |

**Success Response (200 OK)**
```json
{
    "success": true,
    "data": [
        {
            "title": "카페1",
            "roadAddress": "서울 동작구 동작대로..."
        },
        {
            "title": "카페2",
            "roadAddress": "서울 서초구 방배천로..."
        }
    ],
    "timestamp": "2026-01-29T10:30:00"
}
```

**Error Responses**

| HTTP Status | Error Code | Message | 발생 조건 |
|-------------|------------|---------|-----------|
| 401 | USER_UNAUTHORIZED | 사용자 인증에 실패했습니다. | 세션 없음 또는 만료 |
| 404 | MEETING_NOT_FOUND | 모임을 찾을 수 없습니다. | 존재하지 않는 meetingId |

---

### 5. 모임 결과 공유

모임 결과 URL을 조회합니다.

**엔드포인트**
```
GET /meeting/{meetingId}/result
```

**인증 필요**: ✅ 세션 필요

**Path Parameters**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| meetingId | String | ✅ | 모임 식별자 |

**Success Response (200 OK)**
```json
{
    "success": true,
    "data": {
        "meetingUrl": "https://mingling.com/meeting/62db1c35-f7db-4aad-acc8-0ad64f61a312"
    },
    "timestamp": "2026-01-29T10:30:00"
}
```

**Error Responses**

| HTTP Status | Error Code | Message | 발생 조건 |
|-------------|------------|---------|-----------|
| 401 | USER_UNAUTHORIZED | 사용자 인증에 실패했습니다. | 세션 없음 또는 만료 |
| 404 | MEETING_NOT_FOUND | 모임을 찾을 수 없습니다. | 존재하지 않는 meetingId |

---

## 참여자 API

### 1. 모임 입장

닉네임과 비밀번호로 모임에 입장합니다.

**엔드포인트**
```
POST /participant/{meetingId}/enter
```

**인증 필요**: ❌ 없음 (이 API로 세션 생성)

**Path Parameters**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| meetingId | UUID | ✅ | 모임 식별자 |

**Request Body**
```json
{
    "userId": "스위프",
    "password": "password123"
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| userId | String | ✅ | 사용자 이름 (닉네임) |
| password | String | ✅ | 비밀번호 |

**Success Response (200 OK)**
```json
{
    "success": true,
    "data": null,
    "timestamp": "2026-01-29T10:30:00"
}
```

**Response Headers**
- `Set-Cookie`: 세션 쿠키 포함 (nickname, fakeSessionId)

**Error Responses**

| HTTP Status | Error Code | Message | 발생 조건 |
|-------------|------------|---------|-----------|
| 400 | VALIDATION_ERROR | 요청 값이 유효하지 않습니다. | 필수 필드 누락 |
| 401 | INVALID_CREDENTIALS | 인증 정보가 올바르지 않습니다. | 닉네임은 존재하지만 비밀번호 불일치 |
| 404 | MEETING_NOT_FOUND | 모임을 찾을 수 없습니다. | 존재하지 않는 meetingId |

---

### 2. 출발역 등록

참여자의 출발역을 등록합니다.

**엔드포인트**
```
POST /participant/{meetingId}/departure
```

**인증 필요**: ✅ 세션 필요

**Path Parameters**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| meetingId | UUID | ✅ | 모임 식별자 |

**Session Attributes**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| nickname | String | ✅ | 세션에 저장된 닉네임 |

**Request Body**
```json
{
    "nickname": "김밍글",
    "departure": "구로디지털단지역"
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| nickname | String | ✅ | 참여자 이름 |
| departure | String | ✅ | 출발역 이름 |

**Success Response (200 OK)**
```json
{
    "success": true,
    "data": {
        "nickname": "김밍글",
        "departure": "구로디지털단지역",
        "latitude": 37.485266,
        "longitude": 126.901401
    },
    "timestamp": "2026-01-29T10:30:00"
}
```

**Error Responses**

| HTTP Status | Error Code | Message | 발생 조건 |
|-------------|------------|---------|-----------|
| 400 | VALIDATION_ERROR | 요청 값이 유효하지 않습니다. | 필수 필드 누락 |
| 400 | STATION_NOT_FOUND | 유효하지 않은 역 이름입니다. | 존재하지 않는 역 이름 |
| 401 | USER_UNAUTHORIZED | 사용자 인증에 실패했습니다. | 세션 없음 또는 만료 |
| 404 | MEETING_NOT_FOUND | 모임을 찾을 수 없습니다. | 존재하지 않는 meetingId |
| 409 | MEETING_CLOSED | 이미 마감된 모임입니다. | 모임이 마감된 상태 |

---

### 3. 출발역 수정

참여자의 출발역을 수정합니다.

**엔드포인트**
```
PATCH /participant/{meetingId}/departure
```

**인증 필요**: ✅ 세션 필요

**Path Parameters**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| meetingId | UUID | ✅ | 모임 식별자 |

**Session Attributes**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| nickname | String | ✅ | 세션에 저장된 닉네임 |

**Request Body**
```json
{
    "departure": "합정역"
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| departure | String | ✅ | 수정할 출발역 이름 |

**Success Response (200 OK)**
```json
{
    "success": true,
    "data": {
        "nickname": "홍길동",
        "departure": "강남역",
        "latitude": 37.497942,
        "longitude": 127.027621
    },
    "timestamp": "2026-01-29T10:30:00"
}
```

**Error Responses**

| HTTP Status | Error Code | Message | 발생 조건 |
|-------------|------------|---------|-----------|
| 400 | STATION_NOT_FOUND | 유효하지 않은 역 이름입니다. | 존재하지 않는 역 이름 |
| 401 | USER_UNAUTHORIZED | 사용자 인증에 실패했습니다. | 세션 없음 또는 만료 |
| 404 | MEETING_NOT_FOUND | 모임을 찾을 수 없습니다. | 존재하지 않는 meetingId |
| 409 | MEETING_CLOSED | 이미 마감된 모임입니다. | 모임이 마감된 상태 |

---

## 에러 코드 전체 목록

### 400 Bad Request

| 에러 코드 | 메시지 | 설명 |
|-----------|--------|------|
| BAD_REQUEST | 잘못된 요청입니다. | 일반적인 잘못된 요청 |
| VALIDATION_ERROR | 요청 값이 유효하지 않습니다. | 입력값 검증 실패 (data에 상세 정보 포함) |
| PURPOSE_NOT_FOUND | 일부 모임 목적을 찾을 수 없습니다. | 정의되지 않은 모임 목적 포함 |
| STATION_NOT_FOUND | 유효하지 않은 역 이름입니다. | 존재하지 않는 지하철역 |

### 401 Unauthorized

| 에러 코드 | 메시지 | 설명 |
|-----------|--------|------|
| UNAUTHORIZED | 인증이 필요합니다. | 인증 정보 없음 |
| USER_UNAUTHORIZED | 사용자 인증에 실패했습니다. | 세션 없음 또는 만료 |
| INVALID_CREDENTIALS | 인증 정보가 올바르지 않습니다. | 닉네임/비밀번호 불일치 |

### 403 Forbidden

| 에러 코드 | 메시지 | 설명 |
|-----------|--------|------|
| FORBIDDEN | 접근 권한이 없습니다. | 권한 없음 |

### 404 Not Found

| 에러 코드 | 메시지 | 설명 |
|-----------|--------|------|
| NOT_FOUND | 요청한 리소스를 찾을 수 없습니다. | 일반적인 리소스 없음 |
| MEETING_NOT_FOUND | 모임을 찾을 수 없습니다. | 존재하지 않는 모임 ID |

### 409 Conflict

| 에러 코드 | 메시지 | 설명 |
|-----------|--------|------|
| CONFLICT | 요청이 현재 리소스 상태와 충돌합니다. | 일반적인 충돌 |
| MEETING_CLOSED | 이미 마감된 모임입니다. | 모임이 마감된 상태 |

### 500 Internal Server Error

| 에러 코드 | 메시지 | 설명 |
|-----------|--------|------|
| INTERNAL_SERVER_ERROR | 서버 내부 오류가 발생했습니다. | 서버 오류 |
| HOT_PLACE_NOT_FOUND | 핫플레이스 정보를 찾을 수 없습니다. | 핫플레이스 조회 실패 |

---

## 참고 사항

### DateTime 형식
- ISO 8601 형식 사용: `YYYY-MM-DDTHH:mm:ss`
- 예시: `2026-01-30T23:59:59`
- 시간대: Asia/Seoul (KST)

### UUID 형식
- 표준 UUID 형식 (36자)
- 예시: `62db1c35-f7db-4aad-acc8-0ad64f61a312`

### 세션 관리
- 쿠키 기반 세션
- 세션 유효 기간: 7일 (60 * 60 * 24 * 7초)
- 쿠키 이름: `nickname`, `fakeSessionId`

### 검증 에러 응답 우선순위
모임 생성 API에서 여러 필드에 에러가 있을 경우, 다음 우선순위로 **하나의 에러만** 반환됩니다:
1. deadline (마감 시간)
2. capacity (정원)
3. 기타 필수 필드 (순서 무관)

---

**마지막 업데이트**: 2026-01-29
