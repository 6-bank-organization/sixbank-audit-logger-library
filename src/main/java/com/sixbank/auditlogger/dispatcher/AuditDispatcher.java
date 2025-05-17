package com.sixbank.auditlogger.dispatcher;


import log.AuditLog;
import com.sixbank.auditlogger.repository.AuditLogElasticRepository;
import lombok.RequiredArgsConstructor;

/**
 * AuditDispatcher is a pluggable mechanism for routing audit logs.
 *
 * Inject the repository during startup.
 */
@RequiredArgsConstructor
public class AuditDispatcher {

    private static AuditLogElasticRepository repository;

    public static void init(AuditLogElasticRepository repo) {
        repository = repo;
    }

    public static void dispatch(AuditLog log) {
        if (repository != null) {
            repository.save(log);
        }
    }
}
