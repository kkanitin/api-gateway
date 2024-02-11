package com.example.mock.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConstant {

    public static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";
    public static final String CORRELATION_ID_LOG_VAR_NAME = "correlationId";

    public static final String HEADER_REQUEST_URI = "REQUEST-URL";
    public static final String HEADER_REQUEST_METHOD = "REQUEST-METHOD";
    public static final String HEADER_TIME_ELAPSED_IN_MS = "TIME-ELAPSED-IN-MS";
}
