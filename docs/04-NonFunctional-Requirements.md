# Non-Functional Requirements Specification (NFR)

> **Project:** SentinelRisk
> **Version:** 1.0
> **Status:** Draft
> **Owner:** Rohan Badgujar
> **Last Updated:** June 2026

---

# Table of Contents

1. Introduction
2. Purpose
3. Quality Attributes
4. Performance Requirements
5. Availability Requirements
6. Scalability Requirements
7. Reliability Requirements
8. Security Requirements
9. Maintainability Requirements
10. Observability Requirements
11. Disaster Recovery
12. Compliance
13. Capacity Planning
14. Deployment Requirements
15. Constraints
16. Risks
17. Success Criteria

---

# 1. Introduction

This document defines the non-functional requirements for SentinelRisk. Unlike functional requirements that describe **what** the system should do, this document specifies **how well** the system must perform.

The objective is to establish engineering standards for building a production-ready backend service capable of operating in a fintech environment.

---

# 2. Purpose

The primary objectives of this document are:

* Define measurable engineering standards.
* Establish Service Level Objectives (SLOs).
* Ensure production readiness.
* Improve maintainability and scalability.
* Reduce operational risk.

---

# 3. Quality Attributes

The system must satisfy the following quality attributes.

| Attribute       | Priority |
| --------------- | -------- |
| Security        | Critical |
| Availability    | Critical |
| Reliability     | Critical |
| Scalability     | Critical |
| Performance     | High     |
| Observability   | High     |
| Maintainability | High     |
| Extensibility   | Medium   |
| Portability     | Medium   |

---

# 4. Performance Requirements

## API Latency

| Metric                | Target   |
| --------------------- | -------- |
| Average Response Time | < 150 ms |
| P95 Response Time     | < 250 ms |
| P99 Response Time     | < 500 ms |

---

## Throughput

| Metric                | Target |
| --------------------- | ------ |
| Requests Per Second   | 1,000+ |
| Kafka Events / Second | 5,000+ |

---

## Database

* Query execution < 50 ms
* Connection pooling enabled
* Proper indexing on frequently queried columns
* Pagination mandatory for list endpoints

---

## Redis

* Cache lookup < 5 ms
* TTL configurable
* Connection pooling enabled

---

# 5. Availability Requirements

The service shall be highly available.

| Requirement              | Target          |
| ------------------------ | --------------- |
| Availability             | 99.9%           |
| Planned Downtime         | < 2 hours/month |
| Zero Downtime Deployment | Supported       |

Health endpoints:

* `/actuator/health`
* `/actuator/readiness`
* `/actuator/liveness`

---

# 6. Scalability Requirements

The application shall support horizontal scaling.

### Application Layer

* Stateless services
* Multiple application instances
* Load balancer support

### Database

* Read replicas
* Connection pooling
* Index optimization

### Kafka

* Topic partitioning
* Consumer groups
* Horizontal consumer scaling

### Redis

* Redis Cluster compatible
* Distributed cache support

---

# 7. Reliability Requirements

The system shall tolerate transient failures.

Requirements:

* Retry with exponential backoff
* Configurable timeout policies
* Graceful degradation
* Dead Letter Queue (DLQ) for failed Kafka messages
* Idempotent event processing
* Circuit breaker support (future enhancement)

---

# 8. Security Requirements

Security is a first-class concern.

## Authentication

* JWT Access Token
* Refresh Token
* Token expiration
* Secure password hashing (BCrypt)

---

## Authorization

* Role-Based Access Control (RBAC)
* Principle of Least Privilege

Supported roles:

* ADMIN
* RISK_ANALYST
* MERCHANT
* SYSTEM

---

## Encryption

### Data in Transit

* HTTPS only
* TLS 1.2+

### Data at Rest

Sensitive data encrypted using AES-256.

Examples:

* Customer Email
* Phone Number
* Device Fingerprint

Passwords must never be encrypted; they must be hashed using BCrypt.

---

## Input Validation

The application shall protect against:

* SQL Injection
* Cross-Site Scripting (XSS)
* Command Injection
* Invalid JSON payloads
* Oversized requests

---

## Secret Management

Secrets must never be committed to source control.

Use:

* Environment Variables
* Docker Secrets
* Kubernetes Secrets (future)

---

## Logging

Sensitive information must never be written to logs.

Forbidden fields:

* Password
* JWT
* Refresh Token
* AES Keys
* Card Numbers
* CVV

---

# 9. Maintainability Requirements

The project shall follow Clean Architecture principles.

Requirements:

* Layered architecture
* SOLID principles
* Constructor-based dependency injection
* Centralized exception handling
* Modular package structure
* API versioning
* Comprehensive JavaDoc for public APIs

---

# 10. Observability Requirements

The service shall expose operational metrics.

## Metrics

* Total Requests
* Success Rate
* Error Rate
* Average Response Time
* Kafka Publish Count
* Redis Hit Ratio
* JVM Memory Usage
* CPU Utilization

---

## Logging

Every request shall include:

* Correlation ID
* Trace ID
* Request URI
* Response Status
* Execution Time

Structured JSON logging shall be used.

---

## Monitoring

Technology stack:

* Spring Boot Actuator
* Micrometer
* Prometheus
* Grafana

---

# 11. Disaster Recovery

## Recovery Point Objective (RPO)

< 5 minutes

---

## Recovery Time Objective (RTO)

< 30 minutes

---

Database backups shall be performed regularly.

Recovery procedures must be documented and tested.

---

# 12. Compliance

The system should be designed with industry best practices in mind.

Future compliance targets:

* PCI DSS
* GDPR
* ISO 27001
* SOC 2

---

# 13. Capacity Planning

Initial assumptions:

| Resource            | Target     |
| ------------------- | ---------- |
| Concurrent Users    | 10,000     |
| Requests Per Second | 1,000      |
| Daily Transactions  | 1 Million  |
| Kafka Messages/Day  | 10 Million |

The architecture should allow horizontal scaling as traffic grows.

---

# 14. Deployment Requirements

Deployment shall support:

* Docker
* Docker Compose
* Kubernetes (future)
* Blue-Green Deployment (future)
* Rolling Updates
* Health Checks
* Graceful Shutdown

---

# 15. Constraints

The following constraints apply:

* Java 21
* Spring Boot 3.x
* PostgreSQL
* Redis
* Apache Kafka
* Maven
* Docker

The service must remain stateless.

---

# 16. Risks

| Risk                | Mitigation                         |
| ------------------- | ---------------------------------- |
| Database bottleneck | Indexing, connection pooling       |
| Redis outage        | Database fallback                  |
| Kafka unavailable   | Retry with backoff                 |
| High traffic spikes | Horizontal scaling                 |
| Secret leakage      | Externalized configuration         |
| Log data exposure   | Log masking and structured logging |

---

# 17. Success Criteria

SentinelRisk will be considered production-ready when:

* API latency meets defined SLAs.
* Authentication and authorization are enforced.
* Sensitive data is encrypted or hashed appropriately.
* Health, metrics, and logs provide complete operational visibility.
* The service scales horizontally without code changes.
* Automated tests cover critical business flows.
* Deployment can be performed with minimal downtime.

---

# Conclusion

The non-functional requirements establish the engineering standards that SentinelRisk must satisfy to operate as a secure, scalable, reliable, and observable fintech backend service. These requirements guide architectural decisions throughout the implementation lifecycle and ensure the system is designed with production-readiness as a primary objective.
