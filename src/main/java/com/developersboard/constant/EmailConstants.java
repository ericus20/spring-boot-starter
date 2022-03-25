package com.developersboard.constant;

/**
 * This class holds all constants used in Email operations.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class EmailConstants {

  /** Generic Constants. */
  public static final String EMAIL_LINK = "link";

  public static final String URLS = "urls";
  public static final String IMAGE_URL = "imageUrl";
  public static final String IMAGE_URL_INLINE = "imageUrlInline";
  public static final String EMAIL_VERIFY_TEMPLATE = "email/verify-email";
  public static final String MAIL_SUCCESS_MESSAGE = "Mail successfully sent!";
  public static final String MAIL_ERROR_MESSAGE = "There was an error sending mail!";
  public static final String MESSAGE = "message";
  public static final String EMAIL_WELCOME_TEMPLATE = "email/welcome";
  public static final String PASSWORD_RESET_TEMPLATE = "email/reset-password";
  public static final String PASSWORD_UPDATE_TEMPLATE = "email/password-update";
  public static final String APPOINTMENT_CANCEL_TEMPLATE = "email/appointment-cancellation";
  public static final String SIGNED_DOCUMENT_UPLOAD_TEMPLATE =
      "email/tax/signed-document-upload.html";
  public static final String SIMULATING_SENDING_AN_EMAIL = "Simulating sending an email...";

  public static final String DASHBOARD_LINK = "dashboardLink";
  public static final String ABOUT_US_LINK = "aboutUsLink";
  public static final String HELP_LINK = "helpLink";
  public static final String COPY_ABOUT_US = "/copy/about-us";

  /** Message Constants. */
  public static final String CONFIRMATION_PENDING_EMAIL_SUBJECT =
      "[Starter App] You are almost there...";

  public static final String CONFIRMATION_SUCCESS_EMAIL_SUBJECT =
      "[Starter App] Thank you for choosing Starter App";
  public static final String PASSWORD_RESET_EMAIL_SUBJECT =
      "[Starter App] How to Reset Your Password";
  public static final String PASSWORD_RESET_SUCCESS_SUBJECT =
      "[Starter App] Password successfully updated.";
  public static final String APPOINTMENT_CANCEL_SUCCESS_SUBJECT =
      "[Starter App] Appointment cancelled!";
  public static final String TAX_DOCUMENT_UPLOAD_SUCCESS_SUBJECT =
      "[Starter App] New Documents Uploaded";

  private EmailConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
