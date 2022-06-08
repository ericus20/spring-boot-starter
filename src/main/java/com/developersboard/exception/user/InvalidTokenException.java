package com.developersboard.exception.user;

import java.io.Serial;

/**
 * This is an exception for invalid token exceptions.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public class InvalidTokenException extends RuntimeException {
  @Serial private static final long serialVersionUID = -4814040831176808554L;

  /**
   * Constructs a new runtime exception with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public InvalidTokenException(final String message) {
    super(message);
  }
}
