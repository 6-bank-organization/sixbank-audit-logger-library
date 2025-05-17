package com.sixbank.auditlogger.repository;

import com.sixbank.auditlogger.log.AuditLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for saving audit logs.
 */
public interface AuditLogElasticRepository extends ElasticsearchRepository<AuditLog, String> {
}