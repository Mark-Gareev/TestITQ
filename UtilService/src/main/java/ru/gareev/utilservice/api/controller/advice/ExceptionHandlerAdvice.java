package ru.gareev.utilservice.api.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.gareev.utilservice.exceptions.LocalNetworkException;
import ru.gareev.utilservice.util.Constants;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler({LocalNetworkException.class})
    public ProblemDetail handleLocalNetworkException(LocalNetworkException lne) {
        ProblemDetail res = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("Local Network Error", lne);
        res.setDetail("error id " + MDC.get(Constants.corIdKey));
        return res;
    }

    @ExceptionHandler({Exception.class})
    public ProblemDetail handleException(Exception e) {
        ProblemDetail res = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("Internal Server error", e);
        res.setDetail("error id " + MDC.get(Constants.corIdKey));
        return res;
    }
}
