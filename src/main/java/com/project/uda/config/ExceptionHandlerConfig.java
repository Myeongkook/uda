package com.project.uda.config;

import com.project.uda.dto.Message;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.StandardCharsets;

@RestControllerAdvice
public class ExceptionHandlerConfig {

    @ExceptionHandler(Exception.class)
    public ResponseEntity rootExceptionHandler(Exception e){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity(new Message(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), false), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
