package com.developersboard.exception;

import com.developersboard.constant.StorageConstants;
import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Processes invalidFileFormat exception.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = StorageConstants.PATH_CANNOT_BE_NULL)
public class InvalidFileFormatException extends RuntimeException {
  @Serial private static final long serialVersionUID = 3736658997506675274L;

  /**
   * Constructs a new runtime exception with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public InvalidFileFormatException(String message) {
    super(message);
  }
}
