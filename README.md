# TODO Mission

Spring Boot와 Spring Data JPA로 만든 할 일(ToDo) REST API 서버입니다.
회원 가입과 로그인은 제공하지 않습니다.

## 실행 방법

### 요구 환경

- JDK 17 이상
- Gradle Wrapper
- 별도 데이터베이스 설치 불필요

### 실행

H2를 파일 기반으로 사용하므로 별도의 DB를 준비하지 않아도 됩니다.

```bash
./gradlew bootRun
```

Windows에서는 다음 명령을 사용합니다.

```bat
gradlew.bat bootRun
```

서버는 `http://localhost:8080`에서 실행됩니다.
로컬 데이터베이스 파일은 프로젝트의 `db_dev.mv.db`에 생성되며 Git에는 포함되지 않습니다.

테스트 실행 명령은 다음과 같습니다.

```bash
./gradlew test
```

## 기술 및 구조

- Java 17
- Spring Boot 3.5.6
- Spring MVC
- Spring Data JPA
- H2 Database
- Jakarta Bean Validation

주요 패키지는 다음과 같이 역할을 나눴습니다.

```text
controller  HTTP 요청과 응답
service     비즈니스 로직
repository  데이터베이스 접근
entity      JPA 엔터티
dto         API 요청·응답 객체
exception   도메인 예외와 전역 예외 처리
```

JPA 엔터티는 API 요청이나 응답에 직접 사용하지 않고 DTO로 변환합니다.

## 응답 형식

정상 응답과 오류 응답 모두 `RsData` 형식을 사용합니다.

```json
{
  "resultCode": "200-1",
  "msg": "할 일을 조회했습니다.",
  "data": {}
}
```

오류 응답도 같은 최상위 구조를 사용합니다.

```json
{
  "resultCode": "404-1",
  "msg": "해당 할 일을 찾을 수 없습니다. id=999",
  "data": null
}
```

## API 명세

할 일의 응답 데이터는 다음과 같습니다.

```json
{
  "id": 1,
  "title": "Spring 공부하기",
  "completed": false,
  "createdAt": "2026-09-28T12:00:00"
}
```

### 할 일 생성

```http
POST /api/todos
Content-Type: application/json
```

요청 본문:

```json
{
  "title": "Spring 공부하기"
}
```

응답 상태: `201 Created`

제목은 필수이며 공백만 입력할 수 없습니다. 제목은 최대 100자입니다.
생성 시 `completed`는 `false`로 시작합니다.

### 할 일 목록 조회

```http
GET /api/todos
```

응답 상태: `200 OK`

`data`에 할 일 배열을 반환합니다.

### 할 일 단건 조회

```http
GET /api/todos/{id}
```

응답 상태:

- `200 OK`: 조회 성공
- `404 Not Found`: 해당 ID가 없음

### 할 일 수정

```http
PATCH /api/todos/{id}
Content-Type: application/json
```

요청 본문 예시:

```json
{
  "title": "Spring Boot 공부하기",
  "completed": true
}
```

응답 상태:

- `200 OK`: 수정 성공
- `400 Bad Request`: 제목이 공백이거나 100자를 초과함
- `404 Not Found`: 해당 ID가 없음

제목과 완료 여부는 선택적으로 수정할 수 있습니다. 따라서 완료 여부만 바꾸려면 다음과 같이 요청합니다.

```json
{
  "completed": true
}
```

### 할 일 삭제

```http
DELETE /api/todos/{id}
```

응답 상태:

- `200 OK`: 삭제 성공
- `404 Not Found`: 해당 ID가 없음

삭제 성공도 공통 `RsData` 응답 본문을 반환하기 위해 `204 No Content` 대신 `200 OK`를 사용했습니다.

## 오류 처리

전역 예외 처리기에서 오류를 처리합니다.

### 입력값 오류: `400 Bad Request`

```json
{
  "resultCode": "400-1",
  "msg": "입력값이 올바르지 않습니다.",
  "data": {
    "title": "제목은 비어 있을 수 없습니다."
  }
}
```

### 존재하지 않는 할 일: `404 Not Found`

```json
{
  "resultCode": "404-1",
  "msg": "해당 할 일을 찾을 수 없습니다. id=999",
  "data": null
}
```

### 서버 오류: `500 Internal Server Error`

```json
{
  "resultCode": "500-1",
  "msg": "서버 내부 오류가 발생했습니다.",
  "data": null
}
```

## 설계 이유

### URL과 HTTP 메서드

할 일을 `/api/todos`라는 컬렉션 리소스로 표현했습니다.

- `POST /api/todos`: 새로운 리소스 생성
- `GET /api/todos`: 리소스 목록 조회
- `GET /api/todos/{id}`: 특정 리소스 조회
- `PATCH /api/todos/{id}`: 특정 리소스의 일부 수정
- `DELETE /api/todos/{id}`: 특정 리소스 삭제

수정에는 `PATCH`를 사용하여 제목이나 완료 여부 중 필요한 필드만 변경할 수 있도록 했습니다.

### 상태 코드

- `201 Created`: 새로운 할 일 생성
- `200 OK`: 조회·수정·삭제 성공
- `400 Bad Request`: 입력값 또는 요청 형식 오류
- `404 Not Found`: 존재하지 않는 할 일 ID
- `500 Internal Server Error`: 처리하지 못한 서버 오류

### 데이터베이스

H2 파일 데이터베이스를 선택했습니다. 별도의 MySQL이나 PostgreSQL 설치 없이 한 번의 실행 명령으로 서버를 시작할 수 있고, 애플리케이션을 다시 실행해도 로컬 데이터가 유지됩니다. 실제 서비스로 확장할 때는 설정만 변경하여 다른 관계형 데이터베이스로 교체할 수 있습니다.

## 실행 결과 예시

아래 예시는 서버를 실행한 뒤 `curl`로 생성, 목록 조회, 완료 처리, 삭제를 차례로 호출한 결과입니다. ID와 생성 시각은 실행 환경에 따라 달라질 수 있습니다.

### 1. 생성

```bash
curl -i -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"Spring 공부하기"}'
```

```text
HTTP/1.1 201
```

```json
{
  "resultCode": "200-1",
  "msg": "할 일이 생성되었습니다.",
  "data": {
    "id": 1,
    "title": "Spring 공부하기",
    "completed": false,
    "createdAt": "2026-09-28T12:00:00"
  }
}
```

### 2. 목록 조회

```bash
curl -i http://localhost:8080/api/todos
```

```json
{
  "resultCode": "200-1",
  "msg": "할 일 목록을 조회했습니다.",
  "data": [
    {
      "id": 1,
      "title": "Spring 공부하기",
      "completed": false,
      "createdAt": "2026-09-28T12:00:00"
    }
  ]
}
```

### 3. 완료 처리

```bash
curl -i -X PATCH http://localhost:8080/api/todos/1 \
  -H "Content-Type: application/json" \
  -d '{"completed":true}'
```

```json
{
  "resultCode": "200-1",
  "msg": "할 일이 수정되었습니다.",
  "data": {
    "id": 1,
    "title": "Spring 공부하기",
    "completed": true,
    "createdAt": "2026-09-28T12:00:00"
  }
}
```

### 4. 삭제

```bash
curl -i -X DELETE http://localhost:8080/api/todos/1
```

```json
{
  "resultCode": "200-1",
  "msg": "할 일이 삭제되었습니다.",
  "data": null
}
```

### 5. 잘못된 입력: `400 Bad Request`

```bash
curl -i -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"   "}'
```

```json
{
  "resultCode": "400-1",
  "msg": "입력값이 올바르지 않습니다.",
  "data": {
    "title": "제목은 비어 있을 수 없습니다."
  }
}
```

### 6. 없는 할 일: `404 Not Found`

```bash
curl -i http://localhost:8080/api/todos/999
```

```json
{
  "resultCode": "404-1",
  "msg": "해당 할 일을 찾을 수 없습니다. id=999",
  "data": null
}
```
