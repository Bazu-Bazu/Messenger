package messenger.group.chat.service.aop;

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
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        logAfterTrowing(joinPoint, exception);
    }

    @Pointcut(
            "execution(public * messenger.group.chat.service.client.grpc.UserGrpcClient.*(..)) || " +
            "execution(public * messenger.group.chat.service.controller..*(..)) || " +
            "execution(public * messenger.group.chat.service.service..*(..)) || " +
            "execution(public * messenger.group.chat.service.validator..*(..)) || " +
            "execution(public * messenger.group.chat.service.kafka..*(..))"
    )
    public void applicationPackage() {}
}
