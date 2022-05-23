package com.developersboard.constant.user;

import com.developersboard.constant.ErrorConstants;

/**
 * This class holds all constants used in SignUpController implementations.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class SignUpConstants {

  /** Generic log Constants. */
  public static final String USERNAME_EXITS_MESSAGE =
      "The username already exits. Displaying error to the user";

  public static final String EMAIL_EXISTS_MESSAGE =
      "The email already exits. Displaying error to the user";
  public static final String ACCOUNT_EXISTS = "Account already exist!";
  public static final String USER_CREATED_SUCCESS_MESSAGE = "User created Successfully {}";

  /** URL Mapping Constants. */
  public static final String SIGN_UP_MAPPING = "/sign-up";

  public static final String SIGN_UP_VERIFY_MAPPING = "/verify";

  /** View Name Constants. */
  public static final String SIGN_UP_VIEW_NAME = "user/sign-up";

  /** Model Key Constants. */
  public static final String USERNAME_OR_EMAIL_EXISTS_KEY = "usernameOrEmailExists";

  public static final String DUPLICATED_EMAIL_KEY = "duplicatedEmail";
  public static final String SIGN_UP_SUCCESS_KEY = "signedUp";
  public static final String SIGN_UP_PENDING_KEY = "pendingVerify";
  public static final String SIGN_UP_ERROR = "signUpError";

  private SignUpConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
