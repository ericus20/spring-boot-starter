package com.developersboard.exception;

import java.io.Serial;

/**
 * Used in storage service related exceptions.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public class StorageException extends RuntimeException {
  @Serial private static final long serialVersionUID = 2292821525781156962L;

  /**
   * Constructs a new runtime exception with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public StorageException(String message) {
    super(message);
  }

  /**
   * A convenient way to initialize with a given message and exception.
   *
   * @param message the message
   * @param cause the exception
   */
  public StorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
