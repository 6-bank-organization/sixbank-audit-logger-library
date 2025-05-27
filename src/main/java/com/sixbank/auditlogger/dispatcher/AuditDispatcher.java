package com.sixbank.auditlogger.dispatcher;

import com.sixbank.auditlogger.log.AuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Dispatches audit logs via Kafka to decouple microservices from Elasticsearch.
 */
@Component
@RequiredArgsConstructor
public class AuditDispatcher {

    private final AuditKafkaProducer kafkaProducer;

    /**
     * Dispatches the audit log by pushing it into Kafka.
     *
     * @param auditLog the audit log event
     */
    public void dispatch(AuditLog auditLog) {
        //kafkaProducer.send(auditLog);
    }
}
