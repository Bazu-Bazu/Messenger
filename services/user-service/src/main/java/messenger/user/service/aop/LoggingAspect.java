package messenger.user.service.aop;

import aop.BaseLoggingAspect;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Log4j2
public class LoggingAspect extends BaseLoggingAspect {

    @Before(
            "execution(public * messenger.user.service.client.*.*.*(..)) || " +
            "execution(public * messenger.user.service.controller.*.*.*(..)) || " +
            "execution(public * messenger.user.service.service.*.*(..)) || " +
            "execution(public * messenger.user.service.validation.*.*(..))"
    )
    public void before(JoinPoint joinPoint) {
        logBefore(joinPoint);
    }

    @AfterReturning(
            pointcut =
            "execution(public * messenger.user.service.client.*.*.*(..)) || " +
            "execution(public * messenger.user.service.controller.*.*.*(..)) || " +
            "execution(public * messenger.user.service.service.*.*(..)) || " +
            "execution(public * messenger.user.service.validation.*.*(..))",
            returning = "result"
    )
    public void afterReturning(JoinPoint joinPoint, Object result) {
        logAfterReturning(joinPoint, result);
    }

    @AfterThrowing(
            pointcut =
            "execution(public * messenger.user.service.client.*.*.*(..)) || " +
            "execution(public * messenger.user.service.controller.*.*.*(..)) || " +
            "execution(public * messenger.user.service.service.*.*(..)) || " +
            "execution(public * messenger.user.service.validation.*.*(..))",
            throwing = "exception"
    )
    public void afterTrowing(JoinPoint joinPoint, Exception exception) {
        logAfterTrowing(joinPoint, exception);
    }

}
