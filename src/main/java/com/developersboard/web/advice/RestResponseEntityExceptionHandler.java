package com.developersboard.web.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * A global exception handler for REST API.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handles IllegalArgumentException and IllegalStateException thrown by the REST API.
   *
   * @param ex The exception thrown by the REST API.
   * @param request The request object.
   * @return A ResponseEntity object.
   */
  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    String message = ex.getMessage();
    return handleExceptionInternal(ex, message, new HttpHeaders(), HttpStatus.CONFLICT, request);
  }
}
