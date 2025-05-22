package com.sixbank.auditlogger.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixbank.auditlogger.log.AuditLog;
import com.sixbank.auditlogger.context.AuditContext;
import com.sixbank.auditlogger.dispatcher.AuditDispatcher;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * Listener that hooks into JPA lifecycle events to automatically audit entity changes.
 * <p>
 * Supported events:
 * <ul>
 *     <li>{@code @PrePersist} – for create operations</li>
 *     <li>{@code @PreUpdate} – for update operations</li>
 *     <li>{@code @PreRemove} – for delete operations</li>
 * </ul>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * '@Entity'
 * '@EntityListeners(AuditEntityListener.class)'
 * public class User {
 *     @Id
 *     private Long id;
 *     private String name;
 * }
 * }</pre>
 */
@Component
public class AuditEntityListener {

    /**
     * JSON serializer used to convert entity objects to string.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Name of the service for audit tagging, read from application properties.
     * Defaults to "default-service" if not provided.
     */
    @Value("${audit.service-name:default-service}")
    private String serviceName;

    /**
     * Compliance tag to be added to audit logs, read from application properties.
     * Defaults to "GENERAL" if not provided.
     */
    @Value("${audit.compliance-tag:GENERAL}")
    private String complianceTag;

    /**
     * Handles the JPA {@code @PrePersist} event to log create actions.
     *
     * @param entity The entity being persisted.
     */
    @PrePersist
    public void prePersist(Object entity) {
        audit(entity, "CREATE");
    }

    /**
     * Handles the JPA {@code @PreUpdate} event to log update actions.
     *
     * @param entity The entity being updated.
     */
    @PreUpdate
    public void preUpdate(Object entity) {
        audit(entity, "UPDATE");
    }

    /**
     * Handles the JPA {@code @PreRemove} event to log delete actions.
     *
     * @param entity The entity being removed.
     */
    @PreRemove
    public void preRemove(Object entity) {
        audit(entity, "DELETE");
    }

    /**
     * Performs the actual auditing logic by building an {@link AuditLog}
     * object and dispatching it through the {@link AuditDispatcher}.
     *
     * @param entity The entity being changed.
     * @param action The type of action (CREATE, UPDATE, DELETE).
     */
    private void audit(Object entity, String action) {
        try {
            AuditLog log = AuditLog.builder()
                    .entityName(entity.getClass().getSimpleName())
                    .entityId(getId(entity))
                    .action(action)
                    .changedBy(AuditContext.getCurrentUser())
                    .sourceIp(AuditContext.getCurrentIp())
                    .requestUri(AuditContext.getCurrentUri())
                    .serviceName(serviceName)
                    .timestamp(LocalDateTime.now())
                    .newValue(OBJECT_MAPPER.writeValueAsString(entity))
                    .complianceTag(complianceTag)
                    .build();

            AuditDispatcher.dispatch(log);
        } catch (Exception e) {
            System.err.println("Audit logging failed: " + e.getMessage());
        }
    }

    /**
     * Extracts the value of the field annotated with {@link Id} from the given entity.
     *
     * @param entity The entity instance.
     * @return The ID value as a string, or null if no {@code @Id} field is found.
     * @throws IllegalAccessException If the field cannot be accessed.
     */
    private String getId(Object entity) throws IllegalAccessException {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                return String.valueOf(field.get(entity));
            }
        }
        return null;
    }
}
