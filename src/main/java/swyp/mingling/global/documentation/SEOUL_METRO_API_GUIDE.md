# 중간지점 찾기 API 개발을 위한 서울 열린데이터광장 지하철 API 연동 가이드

## 📋 목차
1. [개요](#개요)
2. [아키텍처 및 호출 순서](#아키텍처-및-호출-순서)
3. [클래스별 상세 설명](#클래스별-상세-설명)
4. [사용 예시](#사용-예시)
5. [환경 설정](#환경-설정)
6. [테스트](#테스트)

---

## 개요

### 목적
서울시 열린데이터광장의 **지하철 최단경로 API**를 활용하여 두 역 간의 경로 정보를 조회하고, 중간지점 찾기 기능을 구현하기 위한 기반 모듈입니다.

### 주요 기능
- 출발역과 도착역 간 최단 경로 조회
- 총 이동시간, 총 이동거리 계산
- 환승 경로 정보 제공
- 경유 역 리스트 제공

### 사용 API
- **API 명**: 서울시 지하철 최단경로 이동정보 서비스 (getShtrmPath)
- **제공처**: 서울 열린데이터광장
- **요청 URL**: `http://openapi.seoul.go.kr:8088/{API_KEY}/json/getShtrmPath/1/5/{출발역}/{도착역}/{시간}`
- **응답 형식**: JSON

---

## 아키텍처 및 호출 순서

### 전체 호출 흐름

```
중간지점 찾기 API
    ↓
SubwayRouteService (서비스 계층)
    ↓
SeoulMetroClient (외부 API 호출)
    ↓
[서울시 지하철 API]
    ↓
SeoulMetroRouteResponse (API 응답 DTO)
    ↓
SeoulMetroRouteParser (응답 파싱)
    ↓
SubwayRouteInfo (도메인 DTO)
    ↓
중간지점 찾기 API (최종 응답)
```

### 상세 호출 순서 다이어그램

```
┌─────────────────┐
│ 중간지점 찾기 API   │
└──────┬──────────┘
       │ 1. getRoute(startStation, endStation)
       ↓
┌─────────────────────┐
│ SubwayRouteService  │
└──────┬──────────────┘
       │ 2. searchRoute(startStation, endStation)
       ↓
┌─────────────────┐
│ SeoulMetroClient│
└──────┬──────────┘
       │ 3. HTTP GET 요청
       ↓
┌───────────────────────┐
│ Seoul Metro API       │
│ (서울 열린데이터광장) │
└──────┬────────────────┘
       │ 4. JSON 응답
       ↓
┌──────────────────────────┐
│ SeoulMetroRouteResponse  │
└──────┬───────────────────┘
       │ 5. parse(response)
       ↓
┌──────────────────────────┐
│ SeoulMetroRouteParser    │
└──────┬───────────────────┘
       │ 6. 데이터 변환
       ↓
┌──────────────────┐
│ SubwayRouteInfo  │
└──────┬───────────┘
       │ 7. 반환
       ↓
┌─────────────────┐
│ 중간지점 찾기 API   │
└─────────────────┘
```

---

## 클래스별 상세 설명

### 1. SeoulMetroClient (외부 API 클라이언트)

**위치**: `src/main/java/swyp/mingling/external/SeoulMetroClient.java`

**역할**: 서울시 지하철 API를 호출하는 WebClient 기반 클라이언트

**의존성**:
- `WebClient` (seoulMetroWebClient Bean)

**주요 메서드**:
```java
public SeoulMetroRouteResponse searchRoute(String startStationName, String endStationName)
```

**기능**:
- 역 이름에서 "역" 접미사 자동 제거 (`replaceAll("역$", "")`)
- 현재 시간을 기준으로 경로 조회
- WebClient를 사용한 비동기 API 호출
- 요청 URL 로깅

**사용 예시**:
```java
@Autowired
private SeoulMetroClient seoulMetroClient;

SeoulMetroRouteResponse response = seoulMetroClient.searchRoute("강남", "신사");
```

**생성되는 요청 URL 형식**:
```
http://openapi.seoul.go.kr:8088/4569585948737379313138635468706d/json/getShtrmPath/1/5/강남/신사/2026-01-31 18:14:55
```

---

### 2. SeoulMetroRouteResponse (API 응답 DTO)

**위치**: `src/main/java/swyp/mingling/external/dto/response/SeoulMetroRouteResponse.java`

**역할**: 서울시 API의 JSON 응답을 매핑하는 DTO

**주요 내부 클래스**:

#### Header
```java
public static class Header {
    private String resultCode;  // "00" = 성공
    private String resultMsg;   // "성공"
}
```

#### Body
```java
public static class Body {
    private String searchType;      // 검색 타입
    private Integer totalDstc;      // 총 거리 (m)
    private Integer totalreqHr;     // 총 시간 (초)
    private Integer totalCardCrg;   // 총 카드 요금
    private Integer trsitNmtm;      // 환승 횟수
    private List<PathInfo> paths;   // 경로 정보 목록
}
```

#### PathInfo (경로 구간 정보)
```java
public static class PathInfo {
    private StationInfo dptreStn;   // 출발역 정보
    private StationInfo arvlStn;    // 도착역 정보
    private Integer stnSctnDstc;    // 구간 거리 (m)
    private Integer reqHr;          // 구간 소요시간 (초)
    private Integer wtngHr;         // 대기시간 (초)
    private String trsitYn;         // 환승 여부 (Y/N)
}
```

#### StationInfo (역 정보)
```java
public static class StationInfo {
    private String stnCd;    // 역 코드
    private String stnNo;    // 역 번호
    private String stnNm;    // 역 이름
    private String lineNm;   // 호선명
}
```

**주요 메서드**:
```java
// API 호출 성공 여부 확인
public boolean isSuccess() {
    return header != null && "00".equals(header.getResultCode());
}

// 경로 정보 목록 반환
public List<PathInfo> getPathInfoList() {
    if (body == null || body.getPaths() == null) {
        return List.of();
    }
    return body.getPaths();
}
```

**실제 응답 구조 예시**:
```json
{
  "header": {
    "resultCode": "00",
    "resultMsg": "성공"
  },
  "body": {
    "searchType": "duration",
    "totalDstc": 2400,
    "totalreqHr": 267,
    "totalCardCrg": 2250,
    "trsitNmtm": 0,
    "paths": [
      {
        "dptreStn": {
          "stnCd": "4307",
          "stnNo": "D7",
          "stnNm": "강남",
          "lineNm": "신분당선"
        },
        "arvlStn": {
          "stnCd": "4306",
          "stnNo": "D6",
          "stnNm": "신논현",
          "lineNm": "신분당선"
        },
        "stnSctnDstc": 900,
        "reqHr": 67,
        "wtngHr": 0,
        "trsitYn": "N"
      }
    ]
  }
}
```

---

### 3. SeoulMetroRouteParser (응답 파서)

**위치**: `src/main/java/swyp/mingling/domain/subway/parser/SeoulMetroRouteParser.java`

**역할**: 복잡한 API 응답을 간소화된 도메인 DTO로 변환

**의존성**: 없음 (순수 파싱 로직)

**메서드 호출 관계**:
```
SubwayRouteService.getRoute()
    ↓
parse() (공개 메서드)
    ├─→ buildTransferPath() (private)
    │       └─→ formatLineNumber() (private)
    └─→ buildStationInfoList() (private)
            └─→ formatLineNumber() (private)
```

**주요 메서드**:

#### 1. parse() - 메인 파싱 메서드
```java
/**
 * 서울시 API 응답을 도메인 객체로 변환
 *
 * @param response 서울시 지하철 API 응답
 * @return 간소화된 경로 정보
 */
public SubwayRouteInfo parse(SeoulMetroRouteResponse response)
```

**호출 시점**: `SubwayRouteService.getRoute()` 메서드에서 호출
```java
// SubwayRouteService.java:39
SubwayRouteInfo routeInfo = routeParser.parse(response);
```

**처리 과정**:
1. 경로 정보 리스트 추출 (`response.getPathInfoList()`)
2. 출발역 = 첫 번째 경로의 출발역 (`pathList.get(0).getDptreStn().getStnNm()`)
3. 도착역 = 마지막 경로의 도착역 (`pathList.get(pathList.size() - 1).getArvlStn().getStnNm()`)
4. 총 소요시간 계산 (초 → 분 변환: `totalreqHr / 60`)
5. 총 이동거리 계산 (미터 → km 변환: `totalDstc / 1000.0`)
6. **환승 경로 생성** (`buildTransferPath()` 호출) - Line 49
7. **역별 상세 정보 생성** (`buildStationInfoList()` 호출) - Line 52

#### 2. buildTransferPath() - 환승 경로 생성
```java
/**
 * 환승 경로 문자열 생성
 * 예: "신분당선 > 2호선 > 1호선"
 */
private String buildTransferPath(List<SeoulMetroRouteResponse.PathInfo> pathList)
```

**호출 시점**: `parse()` 메서드 내부에서 호출
```java
// SeoulMetroRouteParser.java:49
String transferPath = buildTransferPath(pathList);
```

**로직**:
- 호선이 변경될 때마다 새로운 호선을 추가
- 중복 제거 (같은 호선이 연속되면 생략)
- ` > `로 연결
- **formatLineNumber() 호출**: Line 116, 126에서 호선명 포맷팅

**실행 흐름**:
```java
1. 첫 번째 경로의 출발역 호선 추가 (Line 115-117)
   → formatLineNumber() 호출
2. 각 경로의 도착역 호선 확인 (Line 121-129)
   → 호선 변경 시 formatLineNumber() 호출하여 추가
```

#### 3. buildStationInfoList() - 역별 상세 정보 생성
```java
/**
 * 역별 상세 정보 리스트 생성
 */
private List<SubwayRouteInfo.StationInfo> buildStationInfoList(
    List<SeoulMetroRouteResponse.PathInfo> pathList)
```

**호출 시점**: `parse()` 메서드 내부에서 호출
```java
// SeoulMetroRouteParser.java:52
List<SubwayRouteInfo.StationInfo> stations = buildStationInfoList(pathList);
```

**로직**:
1. 첫 번째 경로의 출발역 추가 (소요시간 0분) - Line 76-84
   - **formatLineNumber() 호출**: Line 79
2. 각 경로의 도착역을 순차적으로 추가 - Line 86-97
   - **formatLineNumber() 호출**: Line 92
3. 환승역 여부 판단 (`trsitYn == "Y"`)
4. 소요시간 변환 (초 → 분)

**실행 흐름**:
```java
for (int i = 0; i < pathList.size(); i++) {
    if (i == 0) {
        // 출발역 추가 (Line 77-83)
        formatLineNumber(출발역 호선) → StationInfo 생성
    }
    // 도착역 추가 (Line 86-96)
    formatLineNumber(도착역 호선) → StationInfo 생성
}
```

#### 4. formatLineNumber() - 호선명 포맷팅
```java
/**
 * 호선 번호 포맷팅
 * 예: "1" -> "1호선", "신분당선" -> "신분당선"
 */
private String formatLineNumber(String lineNumber)
```

**호출 시점**:
- `buildTransferPath()` 메서드에서 호출 (Line 116, 126)
- `buildStationInfoList()` 메서드에서 호출 (Line 79, 92)

**호출 횟수**:
- 각 역마다 호출되므로 경로의 역 개수만큼 호출됨
- 예: 강남 → 신사 (4개 역) = 최소 8회 호출

**포맷팅 규칙**:
```java
// Line 147: 숫자만 있는 경우
"1" → "1호선"
"2" → "2호선"

// Line 152: 이미 "호선" 포함
"신분당선" → "신분당선" (그대로)

// Line 157: 특수 노선
"경의중앙선" → "경의중앙선" (그대로)
```

---

### 4. SubwayRouteInfo (도메인 DTO)

**위치**: `src/main/java/swyp/mingling/domain/subway/dto/SubwayRouteInfo.java`

**역할**: 지하철 경로 정보를 담는 간소화된 도메인 객체

**주요 필드**:
```java
private final String startStation;              // 출발역
private final String endStation;                // 도착역
private final Integer totalTravelTime;          // 총 소요시간 (분)
private final Double totalDistance;             // 총 거리 (km)
private final String transferPath;              // 환승경로 (예: "신분당선 > 2호선")
private final List<StationInfo> stations;       // 역별 상세정보
```

**주요 메서드**:
```java
/**
 * 경유 역 이름만 리스트로 반환
 *
 * @return 경유 역 이름 리스트 (예: ["강남", "신논현", "논현", "신사"])
 */
public List<String> getStationNames() {
    return stations.stream()
            .map(StationInfo::getStationName)
            .collect(Collectors.toList());
}
```

**내부 클래스 - StationInfo**:
```java
@Getter
@Builder
public static class StationInfo {
    private final String stationName;               // 역 이름
    private final String lineNumber;                // 호선명
    private final Integer travelTime;               // 이 역까지의 소요시간 (분)
    private final boolean isTransfer;               // 환승역 여부
    private final String transferStationName;       // 환승역 이름
}
```

**사용 예시**:
```java
SubwayRouteInfo routeInfo = subwayRouteService.getRoute("강남", "신사");

// 기본 정보
String start = routeInfo.getStartStation();          // "강남"
String end = routeInfo.getEndStation();              // "신사"
Integer time = routeInfo.getTotalTravelTime();       // 4 (분)
Double distance = routeInfo.getTotalDistance();      // 2.4 (km)
String path = routeInfo.getTransferPath();           // "신분당선"

// 경유 역 이름 리스트
List<String> stations = routeInfo.getStationNames(); // ["강남", "신논현", "논현", "신사"]

// 역별 상세 정보
for (StationInfo station : routeInfo.getStations()) {
    System.out.println(station.getStationName());    // 강남, 신논현, 논현, 신사
    System.out.println(station.getLineNumber());     // 신분당선
    System.out.println(station.getTravelTime());     // 0, 1, 1, 1
    System.out.println(station.isTransfer());        // false, false, false, false
}
```

---

### 5. SubwayRouteService (비즈니스 로직)

**위치**: `src/main/java/swyp/mingling/domain/subway/service/SubwayRouteService.java`

**역할**: 지하철 경로 조회의 전체 흐름을 관리하는 서비스 계층

**의존성**:
- `SeoulMetroClient` (API 클라이언트)
- `SeoulMetroRouteParser` (응답 파서)

**주요 메서드**:
```java
/**
 * 지하철 경로 조회
 *
 * @param startStation 출발역 이름
 * @param endStation 도착역 이름
 * @return SubwayRouteInfo 경로 정보
 */
public SubwayRouteInfo getRoute(String startStation, String endStation)
```

**처리 흐름**:
1. **API 호출**: `seoulMetroClient.searchRoute(startStation, endStation)`
2. **응답 검증**:
   - `response == null` 체크
   - `response.isSuccess()` 체크
   - `response.getPathInfoList().isEmpty()` 체크
3. **응답 파싱**: `routeParser.parse(response)`
4. **로깅**: 조회 시작/완료 로그 출력
5. **반환**: `SubwayRouteInfo` 객체

**에러 처리**:
```java
if (response == null || !response.isSuccess() || response.getPathInfoList().isEmpty()) {
    log.warn("조회된 지하철 경로 데이터가 없습니다.");
    throw new RuntimeException("해당 구간의 지하철 경로 정보를 찾을 수 없습니다.");
}
```

---

### 6. SubwayRouteServiceIntegrationTest (통합 테스트)

**위치**: `src/test/java/swyp/mingling/domain/subway/service/SubwayRouteServiceIntegrationTest.java`

**역할**: 실제 서울시 API를 호출하는 통합 테스트

**테스트 설정**:
```java
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "kakao.rest-api-key=b8dc9109fa2b70ee4cd0c982d120f541",
    "seoul.metro.api-key=4569585948737379313138635468706d",
    "spring.data.redis.host=localhost",
    "spring.data.redis.port=6379"
})
```

**테스트 내용**:
```java
@Test
@DisplayName("실제 API 호출 - 강남역에서 신사역까지 전체 경로 조회")
void testGetRoute_RealAPI() {
    // Given
    String startStation = "강남";
    String endStation = "신사";

    // When
    SubwayRouteInfo routeInfo = subwayRouteService.getRoute(startStation, endStation);

    // Then
    assertThat(routeInfo).isNotNull();
    assertThat(routeInfo.getStartStation()).isEqualTo(startStation);
    assertThat(routeInfo.getEndStation()).isEqualTo(endStation);
    assertThat(routeInfo.getTotalTravelTime()).isGreaterThan(0);
    assertThat(routeInfo.getTotalDistance()).isGreaterThan(0.0);
    assertThat(routeInfo.getTransferPath()).isNotEmpty();
    assertThat(routeInfo.getStations()).isNotEmpty();
    assertThat(routeInfo.getStationNames()).isNotEmpty();
    assertThat(routeInfo.getStationNames()).contains(startStation, endStation);
}
```

**실행 결과 예시**:
```
====== 강남역 → 신사역 경로 정보 ======
출발역: 강남
도착역: 신사
소요시간: 4분
이동거리: 2.4km
환승경로: 신분당선

====== 경유 역 리스트 ======
총 경유 역 수: 4
1. 강남 (신분당선)
2. 신논현 (신분당선)
3. 논현 (신분당선)
4. 신사 (신분당선)

경유 역 이름 리스트: [강남, 신논현, 논현, 신사]
====================================
```

---

## 사용 예시

### 1. 중간지점 찾기 usecase 에서 중간지점 API 구현

```java
@RestController
@RequestMapping("/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final SubwayRouteService subwayRouteService;
    private final ParticipantRepository participantRepository;
    private final HotPlaceRepository hotPlaceRepository;

    @GetMapping("/{meetingId}/midpoint")
    public ApiResponse<GetMidpointResponse> getMidpoint(
            @PathVariable UUID meetingId,
            @SessionAttribute String userName) {

        // 1. 모임의 모든 참여자 조회
        List<Participant> participants = participantRepository.findByMeetingId(meetingId);

        // 2. 중간지점 후보(HotPlace) 조회
        List<HotPlace> hotPlaces = hotPlaceRepository.findAll(); // 또는 특정 조건으로 필터링

        // 3. 각 중간지점별로 모든 참여자의 이동시간 계산
        List<GetMidpointResponse.MidpointDto> midpoints = hotPlaces.stream()
            .map(hotPlace -> {
                // 모든 참여자의 이동시간 계산
                List<Integer> travelTimes = participants.stream()
                    .map(participant -> {
                        SubwayRouteInfo route = subwayRouteService.getRoute(
                            participant.getDepartureStation(),
                            hotPlace.getName()
                        );
                        return route.getTotalTravelTime();
                    })
                    .toList();

                // 평균 이동시간 계산
                int avgTravelTime = (int) travelTimes.stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0);

                // 현재 로그인한 사용자의 경로 정보
                Participant currentUser = participants.stream()
                    .filter(p -> p.getName().equals(userName))
                    .findFirst()
                    .orElseThrow();

                SubwayRouteInfo userRoute = subwayRouteService.getRoute(
                    currentUser.getDepartureStation(),
                    hotPlace.getName()
                );

                return GetMidpointResponse.MidpointDto.builder()
                    .name(hotPlace.getName())
                    .latitude(hotPlace.getLatitude())
                    .longitude(hotPlace.getLongitude())
                    .avgTravelTime(avgTravelTime)
                    .transferPath(userRoute.getTransferPath())
                    .build();
            })
            .sorted(Comparator.comparing(GetMidpointResponse.MidpointDto::getAvgTravelTime))
            .limit(3) // 상위 3개만 반환
            .toList();

        // 4. 참여자별 경로 정보 구성
        String finalMidpoint = midpoints.get(0).getName(); // 최적 중간지점

        List<GetMidpointResponse.ParticipantPath> participantPaths = participants.stream()
            .map(participant -> {
                SubwayRouteInfo route = subwayRouteService.getRoute(
                    participant.getDepartureStation(),
                    finalMidpoint
                );

                return GetMidpointResponse.ParticipantPath.builder()
                    .userName(participant.getName())
                    .departureStation(participant.getDepartureStation())
                    .transferPath(route.getTransferPath())
                    .travelTime(route.getTotalTravelTime())
                    .stationNames(route.getStationNames()) // 경유 역 리스트
                    .build();
            })
            .toList();

        // 5. 응답 구성
        GetMidpointResponse response = GetMidpointResponse.builder()
            .midpoints(midpoints)
            .participantPaths(participantPaths)
            .build();

        return ApiResponse.success(response);
    }
}
```

### 2. Response 예시

```json
{
  "success": true,
  "data": {
    "midpoints": [
      {
        "name": "합정역",
        "latitude": 37.5484757,
        "longitude": 126.912071,
        "avgTravelTime": 28,
        "transferPath": "2호선 > 6호선"
      },
      {
        "name": "서울역",
        "latitude": 37.554648,
        "longitude": 126.972559,
        "avgTravelTime": 32,
        "transferPath": "1호선 > 4호선"
      }
    ],
    "participantPaths": [
      {
        "userName": "사용자A",
        "departureStation": "강남역",
        "transferPath": "신분당선 > 2호선 > 6호선",
        "travelTime": 25,
        "stationNames": ["강남", "신논현", "논현", "신사", "압구정로데오", "한티", "합정"]
      },
      {
        "userName": "사용자B",
        "departureStation": "신림역",
        "transferPath": "2호선",
        "travelTime": 35,
        "stationNames": ["신림", "봉천", "서울대입구", "낙성대", "사당", "방배", "서초", "교대", "강남", "합정"]
      },
      {
        "userName": "사용자C",
        "departureStation": "구로디지털단지역",
        "transferPath": "2호선 > 6호선",
        "travelTime": 24,
        "stationNames": ["구로디지털단지", "신도림", "영등포구청", "당산", "합정"]
      }
    ]
  }
}
```

---

## 환경 설정

### 1. application.yml 설정

```yaml
seoul:
  metro:
    base-url: http://openapi.seoul.go.kr:8088
    api-key: ${SEOUL_METRO_API_KEY}
```

### 2. 환경변수 설정

**개발 환경**:
```bash
export SEOUL_METRO_API_KEY=4569585948737379313138635468706d
```

**테스트 환경** (`src/test/resources/application.properties`):
```properties
SEOUL_METRO_API_KEY=4569585948737379313138635468706d
```

### 3. WebClient 설정

`SeoulMetroWebClientConfig.java`:
```java
@Configuration
public class SeoulMetroWebClientConfig {

    @Value("${seoul.metro.base-url}")
    private String baseUrl;

    @Value("${seoul.metro.api-key}")
    private String apiKey;

    @Bean
    public WebClient seoulMetroWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl + "/" + apiKey + "/json")
                .build();
    }
}
```

**생성되는 Base URL**:
```
http://openapi.seoul.go.kr:8088/4569585948737379313138635468706d/json
```

---

## 중요 참고사항

### 1. API 호출 제한
- 서울시 공공데이터 API는 **하루 호출 횟수 제한**이 있을 수 있습니다
- 운영 환경에서는 **캐싱 전략**을 반드시 고려하세요
- 같은 경로를 반복 조회하는 경우 캐싱 필수(추후 반영)

**캐싱 적용 예시**:
```java
@Service
@RequiredArgsConstructor
public class SubwayRouteService {

    private final SeoulMetroClient seoulMetroClient;
    private final SeoulMetroRouteParser routeParser;

    @Cacheable(
        cacheNames = "subway-route",
        key = "#startStation + ':' + #endStation"
    )
    public SubwayRouteInfo getRoute(String startStation, String endStation) {
        // ... 기존 로직
    }
}
```

### 2. 역 이름 입력
- **"역" 접미사**는 자동으로 제거되므로 "강남역" 또는 "강남" 모두 가능
- 정확한 **공식 역 이름**을 사용해야 합니다
  - ✅ "강남", "신사", "합정"
  - ❌ "강남역사거리", "신사동", "합정역입구"

### 3. 시간 계산
- API는 **요청 시점의 시간**을 기준으로 경로를 계산합니다
- **실시간 열차 운행 상황**에 따라 결과가 달라질 수 있습니다
- 새벽 시간대나 심야 시간대에는 경로가 다를 수 있습니다

### 4. 데이터 단위 변환
- **시간**: 초(API) → 분(DTO) 자동 변환
- **거리**: 미터(API) → km(DTO) 자동 변환
- 소수점 처리 주의 (거리는 Double 타입)

### 5. 환승 경로
- `transferPath`는 호선 변경 시에만 추가됩니다
- 같은 호선 내에서는 중복되지 않습니다
- 예: "신분당선 > 2호선 > 6호선"

### 6. 경유 역 리스트
- `stationNames` 필드는 **출발역부터 도착역까지 모든 역**을 포함합니다
- 프론트엔드에서 경로 시각화에 활용 가능
- Response DTO에 추가하여 사용자에게 제공

---

## 관련 파일 및 디렉토리 구조

```
src/
├── main/
│   ├── java/swyp/mingling/
│   │   ├── domain/
│   │   │   └── subway/
│   │   │       ├── dto/
│   │   │       │   └── SubwayRouteInfo.java              ✅ 도메인 DTO
│   │   │       ├── parser/
│   │   │       │   └── SeoulMetroRouteParser.java        ✅ 응답 파서
│   │   │       └── service/
│   │   │           └── SubwayRouteService.java           ✅ 서비스 계층
│   │   ├── external/
│   │   │   ├── SeoulMetroClient.java                    ✅ API 클라이언트
│   │   │   └── dto/response/
│   │   │       └── SeoulMetroRouteResponse.java         ✅ API 응답 DTO
│   │   └── global/
│   │       └── config/
│   │           └── SeoulMetroWebClientConfig.java       ✅ WebClient 설정
│   └── resources/
│       └── application.yml                               ✅ 설정 파일
└── test/
    ├── java/swyp/mingling/domain/subway/service/
    │   └── SubwayRouteServiceIntegrationTest.java       ✅ 통합 테스트
    └── resources/
        └── application.properties                        ✅ 테스트 설정
```

---

## 버전 정보 및 참고 링크

### 버전 정보
- **작성일**: 2026-01-31
- **Spring Boot**: 3.x
- **Java**: 21
- **WebClient**: Spring WebFlux

### 참고 링크
- [서울 열린데이터광장](https://data.seoul.go.kr/)
- [서울시 지하철 API 문서](https://data.seoul.go.kr/dataList/OA-12764/S/1/datasetView.do)
- [Spring WebClient 공식 문서](https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html)

---
