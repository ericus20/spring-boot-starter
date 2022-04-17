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

  /** Invalid token. */
  public static final String INVALID_TOKEN = "Invalid token";

  /** Unauthorized Access detected. When the user is not authorized to access the resource. */
  public static final String UNAUTHORIZED_ACCESS_MESSAGE =
      "Unauthorized Access detected... Returning to login page.";

  /** Username cannot be left blank. */
  public static final String BLANK_USERNAME = "Username cannot be left blank";

  /** Username should be at least 3 and at most 50 characters. */
  public static final String USERNAME_SIZE =
      "Username should be at least 3 and at most 50 characters";

  /** Email cannot be left blank. */
  public static final String BLANK_EMAIL = "Email cannot be left blank";

  /** A valid email format is required. */
  public static final String INVALID_EMAIL = "A valid email format is required";

  /** Password cannot be left blank. */
  public static final String BLANK_PASSWORD = "Password cannot be left blank";

  /** Password should be at least 4 characters. */
  public static final String PASSWORD_SIZE = "Password should be at least 4 characters";

  /** Logs a message for debugging purposes with the given user that couldn't be created. */
  public static final String COULD_NOT_CREATE_USER = "Could not create user {}";

  /** Null elements not allowed. */
  public static final String NULL_ELEMENTS_NOT_ALLOWED = "Null elements not allowed!";

  private ErrorConstants() {
    throw new AssertionError(NOT_INSTANTIABLE);
  }
}
