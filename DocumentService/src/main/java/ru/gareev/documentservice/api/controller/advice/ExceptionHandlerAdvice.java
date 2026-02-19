package ru.gareev.documentservice.api.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.gareev.documentservice.exceptions.DocumentNotFoundException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler({DocumentNotFoundException.class})
    public ProblemDetail handleDocumentNotFoundException(DocumentNotFoundException dnf) {
        ProblemDetail res = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        log.error("DocumentNotFound", dnf);
        String detail = HttpStatus.NOT_FOUND.value() +
                "error id : " +
                MDC.get("correlationId") +
                "error message : " + dnf.getMessage();
        res.setDetail(detail);
        return res;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ProblemDetail handleValidationException(MethodArgumentNotValidException mnv) {
        ProblemDetail res = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        log.error("Bad request ", mnv);
        String detail = HttpStatus.NOT_FOUND.value() +
                "error id : " +
                MDC.get("correlationId");
        res.setDetail(detail);
        return res;
    }

    @ExceptionHandler({Exception.class})
    public ProblemDetail handleEveryEx(Exception e) {
        ProblemDetail res = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("Bad request ", e);
        String detail = HttpStatus.INTERNAL_SERVER_ERROR.value() +
                "error id : " +
                MDC.get("correlationId");
        res.setDetail(detail);
        return res;
    }
}
