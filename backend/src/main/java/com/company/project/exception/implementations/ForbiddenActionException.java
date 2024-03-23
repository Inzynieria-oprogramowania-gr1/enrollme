package com.company.project.exception.implementations;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenActionException extends AbstractLocalException {
    public ForbiddenActionException(String message){
        super(message);
      }
}
