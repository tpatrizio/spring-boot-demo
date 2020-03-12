package com.example.demo.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import com.example.demo.models.Todo;
import com.example.demo.services.TodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TodoController
 */
@Validated
@RestController
@RequestMapping(value = "/api/v1/todos", produces = MediaType.APPLICATION_JSON_VALUE) 
public class TodoController {

    @Autowired 
    TodoService service;

    @Autowired
    TodoMapper mapper;

    @GetMapping
    public ResponseEntity<List<TodoModel>> findAll(
        @RequestParam(required = false, defaultValue = "0") Integer page,
        @Valid @Positive(message = "Page size should be a positive number")
        @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<Todo> todoEntities = service.findAll(PageRequest.of(page, size));
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Todos-Total", Long.toString(todoEntities.getTotalElements()));
        List<TodoModel> todoModels = mapper.toModels(todoEntities.getContent());
        return new ResponseEntity<>(todoModels, headers, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TodoModel> save(@RequestBody @Valid TodoModel todo) {
        Todo savedTodo = service.save(mapper.toEntity(todo));
        return ResponseEntity.ok(mapper.toModel(savedTodo));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TodoModel> getById(@PathVariable long id) {
        Todo todo = service.findById(id);
        return new ResponseEntity<>(mapper.toModel(todo), HttpStatus.OK);
    }

}