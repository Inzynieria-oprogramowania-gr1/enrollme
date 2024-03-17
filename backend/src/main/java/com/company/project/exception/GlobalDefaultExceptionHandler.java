package com.company.project.exception;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import lombok.val;

@ControllerAdvice
class GlobalDefaultExceptionHandler {
  public static final String DEFAULT_ERROR_VIEW = "error";
  
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason="Server error :(")
  @ExceptionHandler(value = Exception.class)
  public void defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
    if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null){
        e.printStackTrace();
        throw e;
    }
    e.printStackTrace();
  }
}