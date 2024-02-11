package com.example.mock.config.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import static com.example.mock.constant.AppConstant.HEADER_REQUEST_METHOD;
import static com.example.mock.constant.AppConstant.HEADER_REQUEST_URI;
import static com.example.mock.constant.AppConstant.HEADER_TIME_ELAPSED_IN_MS;

public class ResponseWrapper extends HttpServletResponseWrapper {

    protected ByteArrayOutputStream byteArrayOutputStream;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    public String toString() {
        return byteArrayOutputStream.toString();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ResponseOutputStream(super.getOutputStream(), byteArrayOutputStream);
    }

    public String getJsonHeaders(RequestWrapper requestWrapper, long timeElapsedInMs) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        Enumeration<String> headerNames = Collections.enumeration(getHeaderNames());

        map.put(HEADER_REQUEST_URI, requestWrapper.getRequestURI());
        map.put(HEADER_REQUEST_METHOD, requestWrapper.getMethod());
        map.put(HEADER_TIME_ELAPSED_IN_MS, String.valueOf(timeElapsedInMs));

        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement().toUpperCase();
            String value = getHeader(key);
            map.put(key, value);
        }

        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException ex) {
            return map.entrySet().stream()
                    .map(entry -> entry.getKey().toUpperCase() + ":" + entry.getValue())
                    .collect(Collectors.joining(",", "{", "}"));
        }
    }
}
