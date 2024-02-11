package com.example.mock.controller;

import com.example.mock.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mock")
public class MockController {

    @GetMapping
    public ResponseEntity<CommonResponse> helloWorld() {
        return ResponseEntity.ok(new CommonResponse("hello world"));
    }
}
