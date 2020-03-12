package com.example.demo.services;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * TodoNotFoundException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TodoNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TodoNotFoundException(Long id) {
        super("Could not find a todo with id " + id);
    }

}