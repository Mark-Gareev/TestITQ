package ru.gareev.utilservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.gareev.utilservice.util.Constants;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String corrId = request.getHeader(Constants.corIdHeader);
        if(corrId == null) {
            corrId = UUID.randomUUID().toString();
        }
        MDC.put(Constants.corIdKey, corrId);
        response.setHeader("X-Correlation-ID", corrId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(corrId);
        }
    }
}
