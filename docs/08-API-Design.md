# API Contract

> **Project:** SentinelRisk – Payment Risk Assessment & Fraud Detection Engine
> **Version:** 1.0
> **API Version:** v1
> **Protocol:** REST
> **Content-Type:** `application/json`

---

# Table of Contents

1. API Standards
2. Authentication
3. Common Headers
4. Response Format
5. Authentication APIs
6. Risk Evaluation APIs
7. Blacklist APIs
8. Health APIs
9. Error Codes
10. HTTP Status Codes

---

# 1. API Standards

* RESTful APIs
* JSON Request/Response
* UTF-8 Encoding
* Stateless Communication
* HTTPS Only
* Versioned Endpoints (`/api/v1`)
* JWT Bearer Authentication
* Correlation ID Support
* Idempotency Support for Financial Operations

---

# 2. Base URL

```http
http://localhost:8080/api/v1
```

Production

```http
https://api.sentinelrisk.com/api/v1
```

---

# 3. Authentication

All protected APIs require a JWT Access Token.

Example:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

# 4. Common Headers

| Header           | Required       | Description                  |
| ---------------- | -------------- | ---------------------------- |
| Content-Type     | ✅              | application/json             |
| Accept           | ✅              | application/json             |
| Authorization    | Protected APIs | Bearer Token                 |
| X-Correlation-Id | Optional       | Request Trace ID             |
| Idempotency-Key  | Risk APIs      | Prevent Duplicate Processing |

Example

```http
Content-Type: application/json
Accept: application/json
Authorization: Bearer eyJhbGc...
X-Correlation-Id: 76c21d8f-2b7d-4d89-91c4-b4b2e03d7b91
Idempotency-Key: 5c9bdbb8-8578-4d1f-b9d2-cdc2fda0d6d3
```

---

# 5. Standard Response

## Success

```json
{
  "success": true,
  "timestamp": "2026-06-29T10:30:00Z",
  "traceId": "76c21d8f",
  "data": {}
}
```

## Error

```json
{
  "success": false,
  "timestamp": "2026-06-29T10:30:00Z",
  "traceId": "76c21d8f",
  "errorCode": "VALIDATION_ERROR",
  "message": "Merchant Code is required."
}
```

---

# 6. Authentication APIs

## 6.1 Register User

### Endpoint

```http
POST /api/v1/auth/register
```

### Authentication

Not Required

### Headers

```http
Content-Type: application/json
Accept: application/json
```

### Request

```json
{
  "username": "rohan",
  "email": "rohan@example.com",
  "password": "Password@123"
}
```

### Validation

| Field    | Validation                                                                    |
| -------- | ----------------------------------------------------------------------------- |
| username | Required, 4-50 characters                                                     |
| email    | Valid email                                                                   |
| password | Minimum 8 characters, 1 uppercase, 1 lowercase, 1 number, 1 special character |

### Success Response

**201 Created**

```json
{
  "success": true,
  "message": "User registered successfully."
}
```

### Error Responses

| Status | Error                   |
| ------ | ----------------------- |
| 400    | Validation Failed       |
| 409    | Username Already Exists |

---

## 6.2 Login

### Endpoint

```http
POST /api/v1/auth/login
```

### Authentication

Not Required

### Headers

```http
Content-Type: application/json
Accept: application/json
```

### Request

```json
{
  "username": "rohan",
  "password": "Password@123"
}
```

### Success Response

**200 OK**

```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "tokenType": "Bearer",
  "expiresIn": 900,
  "refreshExpiresIn": 604800
}
```

### Response Headers

```http
Cache-Control: no-store
Pragma: no-cache
```

### Error Responses

| Status | Error               |
| ------ | ------------------- |
| 400    | Invalid Request     |
| 401    | Invalid Credentials |
| 403    | User Disabled       |

---

## 6.3 Refresh Token

### Endpoint

```http
POST /api/v1/auth/refresh
```

### Headers

```http
Content-Type: application/json
Authorization: Bearer <refresh-token>
```

### Request

```json
{}
```

### Success Response

```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "expiresIn": 900
}
```

---

# 7. Risk Evaluation API

## Evaluate Payment

### Endpoint

```http
POST /api/v1/risk/evaluate
```

### Authentication

JWT Required

### Headers

```http
Authorization: Bearer <access-token>
Content-Type: application/json
Accept: application/json
X-Correlation-Id: <UUID>
Idempotency-Key: <UUID>
```

### Request

```json
{
  "transactionId": "TXN-1001",
  "merchantCode": "MERCHANT-001",
  "customerId": "CUSTOMER-100",
  "amount": 15000,
  "currency": "INR",
  "deviceId": "DEVICE-001",
  "ipAddress": "192.168.1.10",
  "country": "IN"
}
```

### Validation Rules

| Field         | Rule             |
| ------------- | ---------------- |
| transactionId | Required, Unique |
| merchantCode  | Required         |
| customerId    | Required         |
| amount        | Greater than 0   |
| currency      | ISO-4217         |
| deviceId      | Required         |
| ipAddress     | Valid IPv4/IPv6  |

### Business Rules

* Merchant must exist.
* Merchant must be ACTIVE.
* Transaction must not be duplicated.
* Customer must not be blacklisted.
* Device must not be blacklisted.
* Velocity threshold must not be exceeded.

### Success Response

**200 OK**

```json
{
  "transactionId": "TXN-1001",
  "riskScore": 28,
  "decision": "APPROVED",
  "reason": "No suspicious activity detected."
}
```

### Error Responses

| Status | Error                 |
| ------ | --------------------- |
| 400    | Validation Failed     |
| 401    | Unauthorized          |
| 403    | Blacklisted Entity    |
| 404    | Merchant Not Found    |
| 409    | Duplicate Transaction |
| 500    | Internal Server Error |

### Sequence Flow

```mermaid
sequenceDiagram

participant Client
participant Controller
participant RiskService
participant Redis
participant RuleEngine
participant Repository
participant Kafka

Client->>Controller: POST /risk/evaluate
Controller->>RiskService: Evaluate Request
RiskService->>Redis: Check Duplicate & Velocity
Redis-->>RiskService: Result
RiskService->>RuleEngine: Execute Rules
RuleEngine-->>RiskService: Risk Score
RiskService->>Repository: Save Evaluation
Repository-->>RiskService: Saved
RiskService->>Kafka: Publish Event
Kafka-->>RiskService: Ack
RiskService-->>Controller: Risk Decision
Controller-->>Client: HTTP 200
```

---

# 8. Blacklist APIs

## Add Entity

```http
POST /api/v1/blacklist
```

### Headers

```http
Authorization: Bearer <access-token>
Content-Type: application/json
```

### Request

```json
{
  "entityType": "DEVICE",
  "entityValue": "DEVICE-001",
  "reason": "Fraudulent Activity"
}
```

---

## Remove Entity

```http
DELETE /api/v1/blacklist/{id}
```

---

## List Blacklisted Entities

```http
GET /api/v1/blacklist
```

Supports Pagination

```http
?page=0&size=20
```

---

# 9. Health APIs

```http
GET /actuator/health
```

```http
GET /actuator/info
```

```http
GET /actuator/prometheus
```

---

# 10. Error Codes

| Code                  | Meaning           |
| --------------------- | ----------------- |
| VALIDATION_ERROR      | Invalid Request   |
| INVALID_CREDENTIALS   | Login Failed      |
| INVALID_TOKEN         | JWT Invalid       |
| TOKEN_EXPIRED         | JWT Expired       |
| MERCHANT_NOT_FOUND    | Merchant Missing  |
| BLACKLISTED_ENTITY    | Entity Blocked    |
| DUPLICATE_TRANSACTION | Already Processed |
| INTERNAL_SERVER_ERROR | Unexpected Error  |

---

# 11. HTTP Status Codes

| Status | Description           |
| ------ | --------------------- |
| 200    | Success               |
| 201    | Created               |
| 204    | Deleted               |
| 400    | Bad Request           |
| 401    | Unauthorized          |
| 403    | Forbidden             |
| 404    | Not Found             |
| 409    | Conflict              |
| 500    | Internal Server Error |
| 503    | Service Unavailable   |

---

# 12. Design Decisions

* JWT Access Tokens for stateless authentication.
* Refresh Tokens to avoid frequent logins.
* Idempotency-Key for safe retries.
* Correlation ID for request tracing.
* Standard response wrapper for consistency.
* API Versioning for backward compatibility.

---

# 13. Future APIs

* Rule Management
* Fraud Dashboard
* Bulk Risk Evaluation
* Risk Analytics
* ML Prediction APIs
* Merchant Sync API
