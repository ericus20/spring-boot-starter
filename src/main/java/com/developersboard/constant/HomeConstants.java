package com.developersboard.constant;

/**
 * This class holds all Home constants used in the application.
 *
 * @author Matthew Puentes
 * @version 1.0
 * @since 1.0
 */
public final class HomeConstants {

  /** URL Mapping Constants. */
  public static final String INDEX_URL_MAPPING = "/";

  /** Redirect URL Mapping Constants. */
  public static final String REDIRECT_TO_LOGIN = "redirect:/login";

  /** View Name Constants. */
  public static final String INDEX_VIEW_NAME = "index";

  private HomeConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
