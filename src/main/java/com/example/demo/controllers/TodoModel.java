package com.example.demo.controllers;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TodoModel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor 
public class TodoModel {

    @NotNull(message = "The title should not be empty")
    @Size(min = 1, max = 60, message = "Title should be between 1 and 60 characters")
    String title;

    boolean completed;
    
}