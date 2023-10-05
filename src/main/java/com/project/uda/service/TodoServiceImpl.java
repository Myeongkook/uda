package com.project.uda.service;

import com.project.uda.entity.Todo;
import com.project.uda.repository.CoupleRepository;
import com.project.uda.repository.TodoRepository;
import com.project.uda.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;
    private final CoupleRepository coupleRepository;


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
        if(todo.getCouple() == null){
            throw new IllegalArgumentException("커플 정보가 존재하지 않습니다.");
        }
        return todoRepository.save(todo);
    }

    /*
    * Todo 권한 관련 체크 추가
    * */
    @Override
    @Transactional
    public void deleteTodo(List<Todo> todos) {
        Long coupleId = SessionUtil.getSessionUser().getCouple().getId();
        todoRepository.deleteAllByIdInBatch(todos.stream()
                .filter(t -> t.getCouple().getId().equals(coupleId))
                .map(Todo::getId)
                .collect(Collectors.toList()));
    }

    @Override
    public Todo updateTodo(Todo todo) {
        todoRepository.findById(todo.getId());
        return null;
    }
}
