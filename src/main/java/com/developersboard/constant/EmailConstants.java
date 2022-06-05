package com.developersboard.constant;

/**
 * This class holds all constants used in Email operations.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class EmailConstants {

  /** Email link is used to render links within html templates. */
  public static final String EMAIL_LINK = "link";

  /** Extra emails can be passed to the email template through the urls. */
  public static final String URLS = "urls";

  /** Email verification html template */
  public static final String EMAIL_VERIFY_TEMPLATE = "email/verify-email";

  /** Mail successfully sent message key */
  public static final String MAIL_SUCCESS_MESSAGE = "Mail successfully sent!";

  /** Mail message key */
  public static final String MESSAGE = "message";

  /** Email welcome html template */
  public static final String EMAIL_WELCOME_TEMPLATE = "email/welcome";

  /** Email password reset html template */
  public static final String PASSWORD_RESET_TEMPLATE = "email/reset-password";

  /** Email password update html template */
  public static final String PASSWORD_UPDATE_TEMPLATE = "email/password-update";

  /** Simulation message to be displayed in dev mode */
  public static final String SIMULATING_SENDING_AN_EMAIL = "Simulating sending an email...";

  /** About us link */
  public static final String HOME_LINK = "home";

  public static final String CONTACT_US_LINK = "contact-us";
  public static final String ABOUT_US_LINK = "aboutUsLink";

  public static final String COPY_ABOUT_US = "/copy/about-us";

  /** Message Constants. */
  public static final String CONFIRMATION_PENDING_EMAIL_SUBJECT = "You are almost there...";

  public static final String CONFIRMATION_SUCCESS_EMAIL_SUBJECT = "Thank you for choosing us";
  public static final String PASSWORD_RESET_EMAIL_SUBJECT = "How to Reset Your Password";
  public static final String PASSWORD_RESET_SUCCESS_SUBJECT = "Password successfully updated.";

  private EmailConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
