package ru.gareev.utilservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CreatingTaskAspect {
    @Around("@annotation(CreatingTask)")
    public Object addLogTag(ProceedingJoinPoint joinPoint) throws Throwable {
        MDC.put("TASK", "CREATE_DOCUMENTS");
        try {
            return joinPoint.proceed();
        } finally {
            MDC.remove("TASK");
        }

    }
}
