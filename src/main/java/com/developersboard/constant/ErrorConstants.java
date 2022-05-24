package com.developersboard.constant;

/**
 * This class holds all the error messages used in the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class ErrorConstants {

  /** This class is not meant to be instantiated. */
  public static final String NOT_INSTANTIABLE = "This class cannot be instantiated";

  public static final String ERROR = "error";

  /** Invalid token. */
  public static final String INVALID_TOKEN = "Invalid token";

  /** Unauthorized Access detected. When the user is not authorized to access the resource. */
  public static final String UNAUTHORIZED_ACCESS = "Unauthorized Access detected...";

  /** Null elements not allowed. */
  public static final String NULL_ELEMENTS_NOT_ALLOWED = "Null elements not allowed!";

  private ErrorConstants() {
    throw new AssertionError(NOT_INSTANTIABLE);
  }
}
