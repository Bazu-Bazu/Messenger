package messenger.user.service.aop;

import aop.BaseLoggingAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect extends BaseLoggingAspect {

    @Before("applicationPackage()")
    public void before(JoinPoint joinPoint) {
        logBefore(joinPoint);
    }

    @AfterReturning(pointcut = "applicationPackage()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        logAfterReturning(joinPoint, result);
    }

    @AfterThrowing(pointcut = "applicationPackage()", throwing = "exception")
    public void afterTrowing(JoinPoint joinPoint, Exception exception) {
        logAfterTrowing(joinPoint, exception);
    }

    @Pointcut(
            "execution(public * messenger.user.service.controller.api.*.*(..)) || " +
            "execution(public * messenger.user.service.controller.grpc.UserGrpcServer.*(..)) || " +
            "execution(public * messenger.user.service.service.event.*.*(..)) || " +
            "execution(public * messenger.user.service.service.outbox.OutboxEventService.*(..)) || " +
            "execution(public * messenger.user.service.service.*.*(..)) || " +
            "execution(public * messenger.user.service.validation.validator.*.*(..)) || " +
            "execution(public * messenger.user.service.kafka..*(..))"
    )
    public void applicationPackage() {}
}
