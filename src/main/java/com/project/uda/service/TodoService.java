package com.project.uda.service;

import com.project.uda.entity.Todo;

import java.util.List;

public interface TodoService {

    List<Todo> getTodoList(Long coupleId);
    Todo addTodo(Todo todo);
    void deleteTodo(List<Todo> todos);
    Todo updateTodo(Todo todo);
}
