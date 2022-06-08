package com.developersboard.exception.user;

import java.io.Serial;

/**
 * Responsible for user already exists exception specifically.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public class UserAlreadyExistsException extends RuntimeException {
  @Serial private static final long serialVersionUID = -2959136757722416883L;

  /**
   * Constructs a new runtime exception with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
