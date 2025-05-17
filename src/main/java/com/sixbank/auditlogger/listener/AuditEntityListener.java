package com.sixbank.auditlogger.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixbank.auditlogger.AuditLog;
import com.sixbank.auditlogger.context.AuditContext;
import com.sixbank.auditlogger.dispatcher.AuditDispatcher;
import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * AuditEntityListener automatically logs entity state changes.
 *
 * Example usage:
 *
 * @Entity
 * @EntityListeners(AuditEntityListener.class)
 * public class User { ... }
 */
public class AuditEntityListener {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @PrePersist
    @PreUpdate
    @PreRemove
    public void audit(Object entity) {
        try {
            AuditLog log = AuditLog.builder()
                    .entityName(entity.getClass().getSimpleName())
                    .entityId(getId(entity))
                    .action(resolveAction())
                    .changedBy(AuditContext.getCurrentUser())
                    .sourceIp(AuditContext.getCurrentIp())
                    .requestUri(AuditContext.getCurrentUri())
                    .serviceName("kyc-service")
                    .timestamp(LocalDateTime.now())
                    .newValue(OBJECT_MAPPER.writeValueAsString(entity))
                    .complianceTag("KYC")
                    .build();

            AuditDispatcher.dispatch(log);

        } catch (Exception e) {
            System.err.println("Audit logging failed: " + e.getMessage());
        }
    }

    private String getId(Object entity) throws IllegalAccessException {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                return String.valueOf(field.get(entity));
            }
        }
        return null;
    }

    private String resolveAction() {
        return "UPDATE";
    }
}

