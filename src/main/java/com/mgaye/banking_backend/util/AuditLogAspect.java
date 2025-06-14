// AuditLogAspect.java
@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {
    private final AuditLogRepository auditRepo;

    @Around("@annotation(auditable)")
    public Object logAudit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        String action = auditable.action();
        Map<String, Object> params = getMethodParams(joinPoint);

        AuditLog log = AuditLog.builder()
                .action(action)
                .parameters(params)
                .timestamp(Instant.now())
                .build();

        try {
            Object result = joinPoint.proceed();
            log.setStatus("SUCCESS");
            return result;
        } catch (Exception e) {
            log.setStatus("FAILED")
                    .setErrorDetails(e.getMessage());
            throw e;
        } finally {
            auditRepo.save(log);
        }
    }
}