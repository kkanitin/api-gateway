package com.example.identity.config;

import com.example.identity.model.response.CommonResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CommonResponse> handleExpiredJwtException(ExpiredJwtException exception){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(CommonResponse.builder()
                        .status(HttpStatus.FORBIDDEN.getReasonPhrase())
                        .msg(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .msg(exception.getBody().getDetail())
                        .build());
    }
}
