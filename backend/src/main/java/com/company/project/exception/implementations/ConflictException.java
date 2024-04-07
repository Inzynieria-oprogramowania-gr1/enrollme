package com.company.project.exception.implementations;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends AbstractLocalException {
    public ConflictException(String message) {
        super(message);
    }
}
