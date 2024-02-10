package com.example.mock.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("rate")
public class RateLimitController {

    @GetMapping
    public ResponseEntity<Map<String, String>> helloWorld() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("status", HttpStatus.OK.getReasonPhrase());
        map.put("msg", "rate limit test");
        return ResponseEntity.ok(map);
    }
}
