package com.project.uda.controller;

import com.project.uda.dto.Message;
import com.project.uda.entity.Todo;
import com.project.uda.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("")
    public ResponseEntity<Message> getAllTodoListByCouple(@RequestBody Long coupleId){
        return ResponseEntity.ok().body(new Message(HttpStatus.OK, "success", todoService.getTodoList(coupleId)));
    }

    @PostMapping("")
    public ResponseEntity<Message> addTodoList(@RequestBody Todo todo){
        return ResponseEntity.ok().body(new Message(HttpStatus.OK, "success", null));
    }

}
