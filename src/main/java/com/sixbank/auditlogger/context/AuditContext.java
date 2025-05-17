package com.sixbank.auditlogger.context;

/**
 * AuditContext holds per-request user and request metadata.
 *
 * Use AuditContext.set(...) in interceptors or filters.
 */
public class AuditContext {

    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();
    private static final ThreadLocal<String> sourceIp = new ThreadLocal<>();
    private static final ThreadLocal<String> requestUri = new ThreadLocal<>();

    public static void set(String user, String ip, String uri) {
        currentUser.set(user);
        sourceIp.set(ip);
        requestUri.set(uri);
    }

    public static String getCurrentUser() { return currentUser.get(); }
    public static String getCurrentIp() { return sourceIp.get(); }
    public static String getCurrentUri() { return requestUri.get(); }

    /**
     * Clears the current thread-local audit context.
     */
    public static void clear() {
        currentUser.remove();
        sourceIp.remove();
        requestUri.remove();
    }
}
