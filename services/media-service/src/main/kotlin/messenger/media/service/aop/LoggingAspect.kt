package messenger.media.service.aop

import jakarta.persistence.Entity
import lombok.extern.log4j.Log4j2
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.hibernate.proxy.HibernateProxy
import org.springframework.stereotype.Component

@Component
@Aspect
@Log4j2
class LoggingAspect {

    private val log: Logger = LogManager.getLogger(LoggingAspect::class.java)

    @Before("applicationPackage()")
    fun logBefore(joinPoint: JoinPoint) {
        val methodName = getMethodName(joinPoint)
        val className = getClassName(joinPoint)
        val args = joinPoint.args

        log.info("▶ {}.{}() called with args: {}", className, methodName, argsToString(args))
    }

    @AfterReturning(pointcut = "applicationPackage()", returning = "result")
    fun logAfterReturning(joinPoint: JoinPoint, result: Any?) {
        val methodName = getMethodName(joinPoint)
        val className = getClassName(joinPoint)

        log.info("✅ {}.{}() returned: {}", className, methodName, resultToString(result))
    }

    @AfterThrowing(pointcut = "applicationPackage()", throwing = "exception")
    fun logAfterTrowing(joinPoint: JoinPoint, exception: Exception) {
        val methodName = getMethodName(joinPoint)
        val className = getClassName(joinPoint)

        log.warn(
            "❌ {}.{}() threw exception: {} - {}",
            className,
            methodName,
            exception::class.java,
            exception.message
        )
    }

    @Pointcut(
            "execution(public * messenger.media.service.controller..*(..)) || " +
            "execution(public * messenger.media.service.service..*(..))"
    )
    fun applicationPackage() {}

    private fun argsToString(args: Array<Any?>?): String {
        if (args == null || args.isEmpty()) return "[]"

        return args.asSequence()
            .map { arg ->
                when {
                    arg == null -> "null"
                    arg is String -> "\"$arg\""

                    arg is HibernateProxy -> {
                        arg.hibernateLazyInitializer
                            .persistentClass
                            .simpleName
                    }

                    arg::class.java.isAnnotationPresent(Entity::class.java) -> {
                        arg::class.java.simpleName
                    }

                    else -> arg.toString()
                }
            }
            .joinToString(prefix = "[", postfix = "]")
    }

    private fun resultToString(result: Any?): String {
        if (result == null) return "null"

        if (result is Collection<*>) {
            return "Collection[size=${result.size}]"
        }

        val str = result.toString()
        return if (str.length > 100) {
            str.substring(0, 100) + "..."
        } else {
            str
        }
    }

    private fun getMethodName(joinPoint: JoinPoint): String {
        return joinPoint.signature.name
    }

    private fun getClassName(joinPoint: JoinPoint): String {
        return joinPoint.target.javaClass.simpleName
    }
}