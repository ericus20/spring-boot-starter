package com.developersboard.constant.user;

import com.developersboard.constant.ErrorConstants;

/**
 * This class holds all constants used in PasswordToken implementations.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class PasswordConstants {

  /** URL Mapping Constants for forget password path. */
  public static final String FORGOT_ROOT_MAPPING = "/user/forgot/";
  /** URL Mapping Constants for change password path. */
  public static final String PASSWORD_CHANGE_PATH = "change";
  /** View Name Constant for email form. */
  public static final String EMAIL_ADDRESS_VIEW_NAME = "user/email-form";
  /** View Name Constant for change password. */
  public static final String CHANGE_VIEW_NAME = "user/password-reset-form";
  /** Model Key Constant for email success. */
  public static final String EMAIL_SENT_KEY = "emailSent";

  public static final String EMAIL_ERROR_KEY = "emailError";

  /** Model Key Constant for forget password attribute name. */
  public static final String RESET_ATTRIBUTE_NAME = "passwordReset";
  /** Model Key Constant message. */
  public static final String ACCOUNT_IN_SESSION = "Account is currently in session!";

  public static final String RESET_ERROR = "An error processing password reset";
  public static final String SAME_PASSWORD = "New password is the same as current one";
  public static final String UNAUTHORIZED_ACCESS = "Unauthorized Access detected...";

  /** Constructor for Password Token Constants made private. */
  private PasswordConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
