package com.example.demo.controllers;

import java.util.List;

import com.example.demo.models.Todo;

import org.mapstruct.Mapper;


/**
 * TodoMapper
 */
@Mapper
public interface TodoMapper {

    TodoModel toModel(Todo todo);

    List<TodoModel> toModels(List<Todo> todos);

    Todo toEntity(TodoModel todo);
}