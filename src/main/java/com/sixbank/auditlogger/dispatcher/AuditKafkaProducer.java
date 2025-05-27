package com.sixbank.auditlogger.dispatcher;

import com.sixbank.auditlogger.log.AuditLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Responsible for sending audit log messages to a Kafka topic.
 * <p>
 * This class enables asynchronous and decoupled logging by publishing
 * {@link AuditLog} events to Kafka, allowing downstream systems such as
 * Elasticsearch to consume and process audit logs independently.
 * </p>
 */
@Component
public class AuditKafkaProducer {

    /**
     * Kafka template used for sending audit log messages.
     */
    private final KafkaTemplate<String, AuditLog> kafkaTemplate;

    /**
     * The name of the Kafka topic to which audit logs are published.
     * <p>
     * This value is configurable via the application property
     * {@code audit.kafka.topic}. If not specified, it defaults to {@code audit-logs}.
     * </p>
     */
    private final String topic;

    /**
     * Constructs a new {@code AuditKafkaProducer} instance with the required dependencies.
     *
     * @param kafkaTemplate the Kafka template for sending {@link AuditLog} messages
     * @param topic         the Kafka topic name to which audit logs are published;
     *                      injected from application properties
     */
    public AuditKafkaProducer(
            KafkaTemplate<String, AuditLog> kafkaTemplate,
            @Value("${audit.kafka.topic:audit-logs}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    /**
     * Publishes an audit log message to the configured Kafka topic.
     *
     * @param auditLog the {@link AuditLog} event to send
     */
    public void send(AuditLog auditLog) {
        kafkaTemplate.send(topic, auditLog.getId(), auditLog);
    }
}
