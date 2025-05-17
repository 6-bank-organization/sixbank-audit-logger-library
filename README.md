# 📜 SIX Bank Audit Logging Library

A reusable audit logging module for microservices across the SIX Bank platform.  
It captures and stores auditable events such as entity creation, updates, and deletions in a structured, GDPR-compliant format, with support for Elasticsearch integration.

---

## ✨ Features

- 🔄 **Automatic audit logging** via JPA entity lifecycle (`@EntityListeners`)
- 🧵 **Thread-safe request context tracking** (user ID, IP, endpoint)
- 📦 **Decoupled log dispatching** (pluggable to Elasticsearch, Kafka, etc.)
- 🛡 **GDPR-compliant** data fields and access tracking
- 🔍 **Searchable audit logs** via Elasticsearch
- 🧪 **Fully testable and modular** architecture

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
│   └── AuditDispatcher.java            // Dispatch mechanism
└── repository/
└── AuditLogElasticRepository.java  // Elasticsearch log sink

````

---

## 🚀 Getting Started

### 1️⃣ Add as a Dependency

This library is published to the internal SIX Bank Maven registry. Add it to your microservice:

```xml
<dependency>
  <groupId>com.sixbank</groupId>
  <artifactId>audit-logger</artifactId>
  <version>1.0.0</version>
</dependency>
````

---

### 2️⃣ Configure Elasticsearch in `application.yml`

```yaml
spring:
  data:
    elasticsearch:
      client:
        reactive:
          endpoints: localhost:9200
      repositories:
        enabled: true
```

---

### 3️⃣ Initialize Audit Dispatcher

Register the Elasticsearch dispatcher on startup:

```java
@Configuration
public class AuditConfig {
    @Autowired
    public void register(AuditLogElasticRepository repo) {
        AuditDispatcher.init(repo);
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

## 📄 Example Audit Log Output

```json
{
  "eventType": "UPDATE",
  "entity": "KycDocument",
  "entityId": "dcf8423c-912c-11ee-b9d1-0242ac120002",
  "changedFields": {
    "status": ["PENDING", "VERIFIED"]
  },
  "performedBy": "admin@sixbank.com",
  "ipAddress": "10.42.0.15",
  "requestUri": "/kyc/verify",
  "timestamp": "2025-05-17T11:33:41Z"
}
```

---

## 🛡 GDPR Compliance

This library supports compliance with GDPR and other data regulations by:

* Capturing user IDs and IPs for traceability
* Avoiding sensitive field values in logs unless explicitly required
* Supporting log retention policies (via Elasticsearch TTL or archive strategies)

---

## 📘 Generating Javadocs

Run the following command to generate documentation:

```bash
mvn javadoc:javadoc
```

Docs will be available under `target/site/apidocs`.

---

## 🤝 Contributing

All services using this library are encouraged to raise issues, request enhancements, or contribute additional log sinks (e.g., Kafka or file-based).

Please follow the SIX Bank internal contribution guidelines.

---

## 🧾 License

© 2025 SIX Bank Group. Internal use only.
