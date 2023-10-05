package com.project.uda.config;

import com.project.uda.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerConfig {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Message> handleException(Exception e) {
        log.error(e.getMessage(), e);
        Message errorMessage = new Message(HttpStatus.BAD_REQUEST, e.getMessage(), false);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorMessage);
    }
}
