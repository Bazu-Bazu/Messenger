package messenger.personal.chat.service.aop;

import aop.BaseLoggingAspect;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Log4j2
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
            "execution(public * messenger.personal.chat.service.client..*(..)) || " +
            "execution(public * messenger.personal.chat.service.controller..*(..)) || " +
            "execution(public * messenger.personal.chat.service.service..*(..)) || " +
            "execution(public * messenger.personal.chat.service.kafka..*(..))"
    )
    public void applicationPackage() {}
}
