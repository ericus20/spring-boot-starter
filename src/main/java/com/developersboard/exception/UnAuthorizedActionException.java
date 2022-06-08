package com.developersboard.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Processes unauthorized exception.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "You do not have access to this resource")
public class UnAuthorizedActionException extends RuntimeException {
  @Serial private static final long serialVersionUID = -6375585304859610399L;

  /**
   * Constructs a new runtime exception with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public UnAuthorizedActionException(String message) {
    super(message);
  }
}
