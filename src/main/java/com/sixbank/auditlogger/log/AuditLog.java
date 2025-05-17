package com.sixbank.auditlogger.log;

/*
 * Audit Logger Library for Microservices
 *
 * This library provides a reusable, GDPR-compliant auditing mechanism for microservices.
 * It includes:
 * - AuditLog entity structure with Elasticsearch indexing support
 * - Entity listener for automatic audit event generation
 * - Dispatcher for pushing logs to Elasticsearch
 * - Request context management for user/IP tracking
 *
 * Usage:
 * 1. Annotate entities with @EntityListeners(AuditEntityListener.class)
 * 2. Include spring-data-elasticsearch dependency in your service
 * 3. Register the AuditDispatcher bean during startup
 *
 * Authors: Six Bank Engineering
 * Version: 1.0.0
 */
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * AuditLog is the core structure for audit records.
 *
 * It is designed to be GDPR/KYC compliant and indexed into Elasticsearch.
 */
@Document(indexName = "audit-logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    /** Unique ID for the audit record */
    @Id
    private String id = UUID.randomUUID().toString();

    /** Name of the entity being audited */
    private String entityName;

    /** ID of the entity */
    private String entityId;

    /** Action performed: CREATE, UPDATE, DELETE */
    private String action;

    /** User ID or name who triggered the change */
    private String changedBy;

    /** Source IP address of the request */
    private String sourceIp;

    /** Endpoint URI that caused the change */
    private String requestUri;

    /** JSON snapshot of the old entity state */
    private String oldValue;

    /** JSON snapshot of the new entity state */
    private String newValue;

    /** Name of the microservice where this change occurred */
    private String serviceName;

    /** Compliance-related tag: e.g., GDPR, KYC */
    private String complianceTag;

    /** Timestamp of the change */
    private LocalDateTime timestamp;

    /** Flexible metadata container */
    private Map<String, Object> metadata;
}

