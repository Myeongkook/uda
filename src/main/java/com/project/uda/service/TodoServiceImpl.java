package com.project.uda.service;

import com.project.uda.entity.Todo;
import com.project.uda.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;


    /*
    * Todo option 별로 나눠야 함
    * */
    @Override
    public List<Todo> getTodoList(Long coupleId) {
        return todoRepository.findAllByCouple_Id(coupleId);
    }

    @Override
    @Transactional
    public Todo addTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    @Transactional
    public void deleteTodo(List<Todo> todos) {
        todoRepository.deleteAllByIdInBatch(todos.stream()
                .map(Todo::getId)
                .collect(Collectors.toList()));
    }

    @Override
    public Todo updateTodo(Todo todo) {
        todoRepository.findById(todo.getId());
        return null;
    }
}
