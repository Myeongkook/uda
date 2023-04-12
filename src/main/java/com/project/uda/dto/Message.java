package com.project.uda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class Message {
    private HttpStatus httpStatus;
    private String message;
    private Object data;
}
