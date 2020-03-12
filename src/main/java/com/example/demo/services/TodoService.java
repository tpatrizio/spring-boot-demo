package com.example.demo.services;

import com.example.demo.models.Todo;
import com.example.demo.repository.TodoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * TodoService
 */
@Service
public class TodoService {

    @Autowired
    TodoRepository repository;

    public Page<Todo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Todo findById(Long id) throws TodoNotFoundException {
        return repository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
    }
    
    public Todo save(Todo todo) {
        return repository.save(todo);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
}