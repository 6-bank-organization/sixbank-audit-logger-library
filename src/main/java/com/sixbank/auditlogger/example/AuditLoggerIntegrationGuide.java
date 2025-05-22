package com.sixbank.auditlogger.example;

/**
 * <h1>AuditLogger Library - Integration Guide</h1>
 *
 * This class is intended for documentation purposes only.
 * It demonstrates how to integrate the SixBank AuditLogger into a Spring Boot microservice.
 *
 * <h2>Overview</h2>
 * The AuditLogger automatically logs entity changes (CREATE, UPDATE, DELETE) by listening to JPA events
 * and pushing the audit logs to Elasticsearch or other log aggregators.
 *
 * <h2>Steps to Integrate</h2>
 *
 * <ol>
 *     <li>Add the AuditLogger dependency to your project</li>
 *     <li>Annotate your JPA entities with <code>@EntityListeners(AuditEntityListener.class)</code></li>
 *     <li>Register the required configuration beans</li>
 *     <li>Set configuration values in your <code>application.yaml</code> or <code>application.properties</code></li>
 * </ol>
 *
 * @author Six Bank
 * @version 1.0.0
 */
public class AuditLoggerIntegrationGuide {

    /**
     * <h2>Step 1: Annotate your JPA entity</h2>
     *
     * <p>
     * Add <code>@EntityListeners(AuditEntityListener.class)</code> to the entity class you want to audit.
     * </p>
     *
     * <pre>{@code
     * import com.sixbank.auditlogger.listener.AuditEntityListener;
     * import jakarta.persistence.*;
     * import java.util.UUID;
     *
     * @Entity
     * @EntityListeners(AuditEntityListener.class)
     * public class Customer {
     *
     *     @Id
     *     @GeneratedValue
     *     private UUID id;
     *
     *     private String fullName;
     *
     *     private String email;
     *
     *     // other fields...
     * }
     * }</pre>
     */
    public static class EntityExample {}

    /**
     * <h2>Step 2: Register Configuration</h2>
     *
     * <p>
     * If your library requires a configuration class (e.g., for dispatching logs), provide a bean like this:
     * </p>
     *
     * <pre>{@code
     * import com.sixbank.auditlogger.dispatcher.AuditDispatcher;
     * import com.sixbank.auditlogger.dispatcher.ElasticsearchAuditDispatcher;
     * import org.springframework.context.annotation.Bean;
     * import org.springframework.context.annotation.Configuration;
     *
     * @Configuration
     * public class AuditLoggerConfig {
     *
     *     @Bean
     *     public AuditDispatcher auditDispatcher() {
     *         return new ElasticsearchAuditDispatcher();
     *     }
     * }
     * }</pre>
     */
    public static class ConfigExample {}

    /**
     * <h2>Step 3: Configure application.yaml</h2>
     *
     * <p>
     * Place these properties in your <code>src/main/resources/application.yaml</code>:
     * </p>
     *
     * <pre>
     * audit:
     *   enabled: true
     *   service-name: kyc-aml-service
     *   compliance-tag: KYC
     *
     * spring:
     *   data:
     *     elasticsearch:
     *       client:
     *         reactive:
     *           endpoints: localhost:9200
     * </pre>
     */
    public static class YamlConfig {}

    /**
     * <h2>Step 3 (Alternative): Configure application.properties</h2>
     *
     * <pre>
     * audit.enabled=true
     * audit.service-name=kyc-aml-service
     * audit.compliance-tag=KYC
     *
     * spring.data.elasticsearch.client.reactive.endpoints=localhost:9200
     * </pre>
     */
    public static class PropertiesConfig {}

    /**
     * <h2>Step 4: Ensure Request Context (Optional but Recommended)</h2>
     *
     * <p>
     * To log information like <code>changedBy</code> and <code>sourceIp</code>, set up a filter or interceptor:
     * </p>
     *
     * <pre>{@code
     * import com.sixbank.auditlogger.context.RequestContext;
     * import jakarta.servlet.*;
     * import jakarta.servlet.http.HttpServletRequest;
     * import org.springframework.stereotype.Component;
     *
     * import java.io.IOException;
     *
     * @Component
     * public class AuditContextFilter implements Filter {
     *
     *     @Override
     *     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
     *             throws IOException, ServletException {
     *
     *         HttpServletRequest httpRequest = (HttpServletRequest) request;
     *
     *         // Extract user and IP info
     *         String userId = httpRequest.getHeader("X-User-Id");
     *         String ip = httpRequest.getRemoteAddr();
     *         String uri = httpRequest.getRequestURI();
     *
     *         // Set in RequestContext
     *         RequestContext.set(userId, ip, uri);
     *
     *         chain.doFilter(request, response);
     *     }
     * }
     * }</pre>
     */
    public static class RequestContextExample {}

}
