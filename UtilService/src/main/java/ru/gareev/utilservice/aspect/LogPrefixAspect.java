package ru.gareev.utilservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

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

    @Around("@annotation(BackgroundSubmit)")
    public Object addSubmitLogTag(ProceedingJoinPoint joinPoint) throws Throwable{
        MDC.put("TASK","SUBMIT_BACKGROUND");
        try{
            return joinPoint.proceed();
        }finally {
            MDC.remove("TASK");
        }
    }

    @Around("@annotation(BackgroundApprove)")
    public Object addApproveLogTag(ProceedingJoinPoint joinPoint) throws Throwable{
        MDC.put("TASK","APPROVE_BACKGROUND");
        try{
            return joinPoint.proceed();
        }finally {
            MDC.remove("TASK");
        }
    }

}
