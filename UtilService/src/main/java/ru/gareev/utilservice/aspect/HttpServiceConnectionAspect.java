package ru.gareev.utilservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import ru.gareev.utilservice.exceptions.LocalNetworkException;

@Aspect
@Component
public class HttpServiceConnectionAspect {
    @Around("@annotation(DocumentServiceConnection)")
    public Object handleNetworkExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (RestClientException rce) {
            throw new LocalNetworkException("document service unavailable");
        }
    }
}
