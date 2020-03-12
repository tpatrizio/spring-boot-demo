package com.example.demo.controllers.errors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

/**
 * ApiValidationError
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiValidationError extends ApiSubError {

    String object;

    String field;

    Object rejectedValue;

    String message;
    
    ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
    
}