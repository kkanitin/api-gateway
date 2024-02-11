package com.example.mock.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LoggingService {

    public void logError(String url, Exception exception) {
        log.error(String.format("ERROR %s", url), exception);
    }

    public void logInbound(String payload, String header) {
        log("Inbound", payload, header);
    }

    public void logOutbound(String payload, String header) {
        log("Outbound", payload, header);
    }

    private void log(String type, String payload, String header) {
        log.info("{} {} {} {}", "PAYLOAD", type, payload, header);
    }
}
