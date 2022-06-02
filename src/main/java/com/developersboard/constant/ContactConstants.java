package com.developersboard.constant;

/**
 * This class holds all Contact constants used in the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class ContactConstants {

  /** URL Mapping Constants. */
  public static final String CONTACT_URL_MAPPING = "/contact";

  /** The key which identifies the feedback payload in the Model. */
  public static final String FEEDBACK = "feedback";

  public static final String FEEDBACK_SUCCESS_KEY = "feedbackSuccess";

  /** View Name Constants. */
  public static final String CONTACT_VIEW_NAME = "contact-us";

  public static final String FEEDBACK_ERROR_MESSAGE = "Error submitting feedback";

  private ContactConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
