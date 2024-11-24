package com.arishev.task.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class TaskAspect {

    private final Logger logger = LoggerFactory.getLogger(TaskAspect.class);

    @Before("@annotation(com.arishev.task.aspect.annotations.LogExecution)")
    public void logMethodStart(JoinPoint joinPoint) {
        logger.info("Execute method - " + joinPoint.getSignature().toShortString());
    }

    @AfterThrowing(
            pointcut = "@annotation(com.arishev.task.aspect.annotations.LogException)",
            throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        logger.error("Exception from method - " + joinPoint.getSignature().toShortString() + ": \\n" +
                        "exception message: " + ex.getMessage() + "\\n" +
                        "method input parameters: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(
            pointcut = "@annotation(com.arishev.task.aspect.annotations.LogException)",
            returning = "res"
            )
    public void logExecutionResult(JoinPoint joinPoint, Object res) {
        logger.info("Result of calling method - " + joinPoint.getSignature().toShortString() + ":" + res);
    }

    @Around("@annotation(com.arishev.task.aspect.annotations.LogProfiling)")
    public Object logProfiling(ProceedingJoinPoint proceedingJoinPoint) {

        Object result;
        try {
            long startTime = System.currentTimeMillis();
            result = proceedingJoinPoint.proceed();
            long stopTime = System.currentTimeMillis();

            logger.info("Execution time (ms) for method: " +
                    proceedingJoinPoint.getSignature().toShortString() + " = " +
                    (stopTime - startTime));
        } catch (Throwable e) {
           throw new RuntimeException(e.getMessage());
        }

        return result;
    }

}
