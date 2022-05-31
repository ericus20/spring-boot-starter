package com.developersboard.constant.user;

import com.developersboard.constant.ErrorConstants;

/**
 * This class holds all constants used in PasswordToken implementations.
 *
 * @author Eric Opoku
 */
public final class PasswordConstants {

  /** URL Mapping Constants for forget password path. */
  public static final String PASSWORD_RESET_ROOT_MAPPING = "/password-reset";
  /** URL Mapping Constants for change password path. */
  public static final String PASSWORD_CHANGE_PATH = "/change";

  /** View Name Constant for email form. */
  public static final String PASSWORD_RESET_START_VIEW_NAME = "user/password-reset-start";
  /** View Name Constant for change password. */
  public static final String PASSWORD_RESET_COMPLETE_VIEW_NAME = "user/password-reset-complete";
  /** Model Key Constant for email success. */
  public static final String PASSWORD_RESET_EMAIL_SENT_KEY = "passwordResetEmailSent";

  public static final String EMAIL_ERROR_KEY = "emailError";

  /** Model Key Constant for forget password attribute name. */
  public static final String PASSWORD_RESET_SUCCESS = "passwordResetSuccess";
  /** Model Key Constant message. */
  public static final String ACCOUNT_IN_SESSION = "Account is currently in session!";

  public static final String SAME_PASSWORD = "New password is the same as current one";
  public static final String PASSWORD_UPDATE_ERROR = "An error occurred while updating password";

  /** Constructor for Password Token Constants made private. */
  private PasswordConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
