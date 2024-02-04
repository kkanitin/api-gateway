package com.example.mock.config.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;

import java.io.IOException;

@Log4j2
public class LoggingFilter implements Filter, Ordered {

    private static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";
    private static final String CORRELATION_ID_LOG_VAR_NAME = "correlationId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String correlationId = req.getHeader(CORRELATION_ID_HEADER_NAME);
        log.info(CORRELATION_ID_LOG_VAR_NAME + " : " + correlationId);
        log.info("Request Info: " + req.getMethod() + " " + req.getRequestURI());

        chain.doFilter(request, response);

        log.info("Response Info: " + response.getContentType());

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
