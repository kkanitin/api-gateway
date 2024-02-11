package com.example.mock.controller;

import com.example.mock.response.CommonResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rate")
@Log4j2
public class RateLimitController {

    @GetMapping
    public ResponseEntity<CommonResponse> rateLimit() {
        return ResponseEntity.ok(new CommonResponse("rate limit"));
    }
}
