package ru.gareev.utilservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import ru.gareev.utilservice.util.Constants;

import java.util.UUID;

@Aspect
@Component
public class LogPrefixAspect {
    @Around("@annotation(CreatingTask)")
    public Object addCreateLogTag(ProceedingJoinPoint joinPoint) throws Throwable {
        MDC.put("TASK", "CREATE_DOCUMENTS");
        try {
            return joinPoint.proceed();
        } finally {
            MDC.remove("TASK");
        }

    }

    /**
     * there is no rest req for our background task, so we generate corId here
     */
    @Around("@annotation(BackgroundSubmit)")
    public Object addSubmitLogTag(ProceedingJoinPoint joinPoint) throws Throwable {
        MDC.put("TASK", "SUBMIT_BACKGROUND");
        MDC.put(Constants.corIdKey, UUID.randomUUID().toString());
        try {
            return joinPoint.proceed();
        } finally {
            MDC.remove("TASK");
            MDC.remove(Constants.corIdKey);
        }
    }

    @Around("@annotation(BackgroundApprove)")
    public Object addApproveLogTag(ProceedingJoinPoint joinPoint) throws Throwable {
        MDC.put("TASK", "APPROVE_BACKGROUND");
        MDC.put(Constants.corIdKey, UUID.randomUUID().toString());
        try {
            return joinPoint.proceed();
        } finally {
            MDC.remove("TASK");
            MDC.remove(Constants.corIdKey);
        }
    }

}
