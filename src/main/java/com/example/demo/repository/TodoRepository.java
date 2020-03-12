package com.example.demo.repository;

import com.example.demo.models.Todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TodoRepository
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    
}