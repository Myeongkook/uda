package com.project.uda.repository;

import com.project.uda.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Long> {
    List<Todo> findAllByCouple_Id(Long Couple);
}
