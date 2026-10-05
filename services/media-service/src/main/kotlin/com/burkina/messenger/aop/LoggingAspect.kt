package com.burkina.messenger.aop

import com.burkina.common.aop.BaseLoggingAspect
import lombok.extern.log4j.Log4j2
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Component
@Aspect
@Log4j2
class LoggingAspect : BaseLoggingAspect() {

    @Before("applicationPackage()")
    fun before(joinPoint: JoinPoint) {
        logBefore(joinPoint)
    }

    @AfterReturning(pointcut = "applicationPackage()", returning = "result")
    fun afterReturning(
        joinPoint: JoinPoint,
        result: Any?
    ) {
        logAfterReturning(joinPoint, result)
    }

    @AfterThrowing(pointcut = "applicationPackage()", throwing = "exception")
    fun afterThrowing(
        joinPoint: JoinPoint,
        exception: Exception
    ) {
        logAfterTrowing(joinPoint, exception)
    }

    @Pointcut(
        "execution(public * com.burkina.messenger.controller..*(..)) || " +
               "execution(public * com.burkina.messenger.service..*(..))"
    )
    fun applicationPackage() {
    }
}