package com.developersboard.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handles all generic exceptions for the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error processing your request")
public class InvalidServiceRequestException extends RuntimeException {
  @Serial private static final long serialVersionUID = 4518149233942557017L;

  /**
   * Constructs a new runtime exception with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public InvalidServiceRequestException(final String message) {
    super(message);
  }

  /**
   * Constructs a new runtime exception with the specified cause and a detail message. This
   * constructor is useful for runtime exceptions that are little more than wrappers for other
   * throwables.
   *
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method)
   * @since 1.4
   */
  public InvalidServiceRequestException(final Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new runtime exception with the specified detail message and cause.
   *
   * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
   * incorporated in this runtime exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method).
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   * @since 1.4
   */
  public InvalidServiceRequestException(final String message, Throwable cause) {
    super(message, cause);
  }
}
