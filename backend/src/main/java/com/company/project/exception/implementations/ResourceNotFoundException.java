package com.company.project.exception.implementations;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends AbstractLocalException{
  public ResourceNotFoundException(String message){
    super(message);
  }
}
