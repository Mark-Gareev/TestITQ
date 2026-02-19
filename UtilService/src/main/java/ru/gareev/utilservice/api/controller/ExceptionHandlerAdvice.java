package ru.gareev.utilservice.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.gareev.utilservice.exceptions.LocalNetworkException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler({LocalNetworkException.class})
    public ProblemDetail handleLocalNetworkException(LocalNetworkException lne) {
        ProblemDetail res = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("Local Network Error", lne);
        res.setDetail(lne.getMessage());
        return res;
    }
}
