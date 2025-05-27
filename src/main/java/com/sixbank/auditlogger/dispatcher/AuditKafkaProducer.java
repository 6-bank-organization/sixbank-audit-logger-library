package com.sixbank.auditlogger.dispatcher;

import com.sixbank.auditlogger.log.AuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Dispatches audit logs to Kafka topic for asynchronous indexing into Elasticsearch.
 *
 * This enables scalable logging across microservices.
 */
@Component
@RequiredArgsConstructor
public class AuditKafkaProducer {

    private final KafkaTemplate<String, AuditLog> kafkaTemplate;

    /** Topic name to which audit logs are published */
    @Value("${audit.kafka.topic:audit-logs}")
    private final String topic;

    /**
     * Sends the audit log to the configured Kafka topic.
     *
     * @param auditLog the log object to be sent
     */
    public void send(AuditLog auditLog) {
        kafkaTemplate.send(topic, auditLog.getId(), auditLog);
    }
}

