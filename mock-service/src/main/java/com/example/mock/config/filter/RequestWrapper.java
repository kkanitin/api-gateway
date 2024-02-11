package com.example.mock.config.filter;

import com.example.mock.constant.AppConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import static com.example.mock.constant.AppConstant.HEADER_REQUEST_METHOD;
import static com.example.mock.constant.AppConstant.HEADER_REQUEST_URI;

public class RequestWrapper extends HttpServletRequestWrapper {

    protected byte[] body;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        BufferedReader reader = request.getReader();
        try (StringWriter writer = new StringWriter()) {
            int read;
            char[] buf = new char[1024 * 8];
            while ((read = reader.read(buf)) != -1) {
                writer.write(buf, 0, read);
            }
            this.body = writer.getBuffer().toString().getBytes();
        }
    }

    public String toString() {
        return new String(body, StandardCharsets.UTF_8);
    }

    @Override
    public ServletInputStream getInputStream() {
        return new RequestInputStream(body);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    public String getJsonHeaders(String correlationId) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        Enumeration<String> headerNames = getHeaderNames();

        map.put(HEADER_REQUEST_URI, getRequestURI());
        map.put(HEADER_REQUEST_METHOD, getMethod());

        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement().toUpperCase();
            String value = getHeader(key);
            map.put(key, value);
        }

        map.putIfAbsent(AppConstant.CORRELATION_ID_HEADER_NAME, correlationId);

        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException ex) {
            return map.entrySet().stream()
                    .map(entry -> entry.getKey().toUpperCase() + ":" + entry.getValue())
                    .collect(Collectors.joining(",", "{", "}"));
        }
    }
}
