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
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * AuditLog is the core structure for audit records.
 *
 * It is designed to be GDPR/KYC compliant and indexed into Elasticsearch.
 */
@Document(indexName = "audit-logs")
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private OffsetDateTime timestamp;

    /** Flexible metadata container */
    private Map<String, Object> metadata;


    public AuditLog() {
    }

    public AuditLog(String id, String entityName, String entityId, String action, String changedBy, String sourceIp, String requestUri, String oldValue, String newValue, String serviceName, String complianceTag, OffsetDateTime timestamp, Map<String, Object> metadata) {
        this.id = id;
        this.entityName = entityName;
        this.entityId = entityId;
        this.action = action;
        this.changedBy = changedBy;
        this.sourceIp = sourceIp;
        this.requestUri = requestUri;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.serviceName = serviceName;
        this.complianceTag = complianceTag;
        this.timestamp = timestamp;
        this.metadata = metadata;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getComplianceTag() {
        return complianceTag;
    }

    public void setComplianceTag(String complianceTag) {
        this.complianceTag = complianceTag;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}

