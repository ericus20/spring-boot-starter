package com.developersboard.exception.user;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Responsible for user not found exception specifically.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User does not exist")
public class UserNotFoundException extends RuntimeException {
  @Serial private static final long serialVersionUID = 4992489948815528687L;

  /**
   * Constructs a new runtime exception with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public UserNotFoundException(String message) {
    super(message);
  }
}
