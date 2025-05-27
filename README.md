# 📜 SIX Bank Audit Logging Library

A reusable audit logging module for microservices across the SIX Bank platform.
It captures and publishes auditable events (such as entity creation, updates, and deletions) in a structured, GDPR-compliant format to a Kafka topic for asynchronous downstream processing (e.g., Elasticsearch, data lake, monitoring tools).

---

## ✨ Features

* 🔄 **Automatic audit logging** via JPA entity lifecycle (`@EntityListeners`)
* 🧵 **Thread-safe request context tracking** (user ID, IP, endpoint)
* 📦 **Decoupled log dispatching** using Kafka
* 🛡 **GDPR-compliant** data fields and access tracking
* ☁ **Stream-ready** architecture (logs are consumed by other services)
* 🧪 **Fully testable and modular** design

---

## 📁 Module Structure

```
com.sixbank.auditlogger
├── AuditLog.java                       // Core audit log model
├── context/
│   └── AuditContext.java               // Thread-local request context
├── listener/
│   └── AuditEntityListener.java        // JPA entity lifecycle hook
├── dispatcher/
│   ├── AuditDispatcher.java            // Dispatcher interface
│   └── AuditKafkaProducer.java         // Kafka log producer
```

---

## 🚀 Getting Started

### 1️⃣ Add as a Dependency

This library is published to the internal SIX Bank Maven registry. Add it to your microservice:

```xml
<dependency>
  <groupId>com.sixbank</groupId>
  <artifactId>audit-logger</artifactId>
  <version>1.1.0</version> <!-- Updated version -->
</dependency>
```

---

### 2️⃣ Configure Kafka in `application.yml`

```yaml
spring:
  kafka:
    bootstrap-servers: kafka.internal.sixbank.com:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer

audit:
  kafka:
    topic: audit-logs
```

---

### 3️⃣ Initialize the Kafka Dispatcher

Register the Kafka dispatcher on application startup:

```java
@Configuration
public class AuditConfig {
    @Autowired
    public void register(AuditKafkaProducer producer) {
        AuditDispatcher.init(producer::send);
    }
}
```

---

### 4️⃣ Capture Request Context

Set request metadata using a filter or interceptor:

```java
public class AuditFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletRequest request = (HttpServletRequest) req;
        String userId = request.getHeader("X-User-Id"); // Or from auth token
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();

        AuditContext.set(userId, ip, uri);
        try {
            chain.doFilter(req, res);
        } finally {
            AuditContext.clear();
        }
    }
}
```

---

### 5️⃣ Annotate Entities for Audit

Add the audit listener to your domain models:

```java
@Entity
@EntityListeners(AuditEntityListener.class)
public class KycDocument {
    // ...
}
```

---

## 📄 Example Audit Log Payload (Kafka Message)

```json
{
  "id": "a4d51238-bf14-4d9e-94d3-6d2cbf1e6d6f",
  "action": "UPDATE",
  "entityName": "KycDocument",
  "entityId": "dcf8423c-912c-11ee-b9d1-0242ac120002",
  "changedBy": "admin@sixbank.com",
  "sourceIp": "10.42.0.15",
  "requestUri": "/kyc/verify",
  "serviceName": "kyc-service",
  "timestamp": "2025-05-17T11:33:41Z",
  "newValue": "{\"status\":\"VERIFIED\"}",
  "complianceTag": "KYC_CHANGE"
}
```

---

## 🛡 GDPR Compliance

This library helps with regulatory compliance by:

* Capturing metadata (user ID, IP address) for traceability
* Avoiding logging sensitive fields unless explicitly configured
* Supporting downstream TTL, redaction, and data retention via Kafka consumers

---

## 📘 Generating Javadocs

Run the following command to generate documentation:

```bash
mvn javadoc:javadoc
```

Docs will be available under `target/site/apidocs`.

---

## 🤝 Contributing

All services using this library are encouraged to raise issues, request enhancements, or contribute additional dispatchers (e.g., file, database, event bus).

Please follow the SIX Bank internal contribution guidelines.

---

## 🧾 License

© 2025 SIX Bank Group. Internal use only.
