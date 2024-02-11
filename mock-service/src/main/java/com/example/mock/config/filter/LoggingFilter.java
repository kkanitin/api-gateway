package com.example.mock.config.filter;

import com.example.mock.service.LoggingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.mock.constant.AppConstant.CORRELATION_ID_HEADER_NAME;
import static com.example.mock.constant.AppConstant.CORRELATION_ID_LOG_VAR_NAME;

@Log4j2
@Component
@AllArgsConstructor
public class LoggingFilter
        extends OncePerRequestFilter {

    private final LoggingService loggingService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String correlationId = Optional.ofNullable(request.getHeader(CORRELATION_ID_HEADER_NAME)).orElseGet(() -> UUID.randomUUID().toString());

        response.setHeader(CORRELATION_ID_HEADER_NAME, correlationId);
        ThreadContext.put(CORRELATION_ID_LOG_VAR_NAME, correlationId);

        RequestWrapper requestWrapper = new RequestWrapper(request);
        ResponseWrapper responseWrapper = new ResponseWrapper(response);


        long startTime = 0;
        try {
            String requestPayload = Strings.isEmpty(requestWrapper.toString()) ? getJsonParams(requestWrapper) : requestWrapper.toString();

            loggingService.logInbound(requestPayload, requestWrapper.getJsonHeaders(correlationId));

            startTime = System.currentTimeMillis();
            filterChain.doFilter(requestWrapper, responseWrapper);
        } catch (Exception ex) {
            loggingService.logError(requestWrapper.getRequestURI(), ex);
            throw ex;
        } finally {
            String responsePayload = Strings.isEmpty(responseWrapper.toString()) ? "{}" : responseWrapper.toString();

            long endTime = System.currentTimeMillis();
            long timeElapsed = endTime - startTime;
            loggingService.logOutbound(responsePayload, responseWrapper.getJsonHeaders(requestWrapper, timeElapsed));
            ThreadContext.remove(CORRELATION_ID_HEADER_NAME);
        }
    }

    private String getJsonParams(ServletRequestWrapper requestWrapper) {
        Map<String, String[]> parameterMap = requestWrapper.getParameterMap();
        try {
            return new ObjectMapper().writeValueAsString(parameterMap);
        } catch (JsonProcessingException ex) {
            return parameterMap.entrySet().stream()
                    .map(entry -> entry.getKey() + ":" + Arrays.toString(entry.getValue()))
                    .collect(Collectors.joining(",", "{", "}"));
        }
    }

    @Override
    protected boolean isAsyncDispatch(final HttpServletRequest request) {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }
}
