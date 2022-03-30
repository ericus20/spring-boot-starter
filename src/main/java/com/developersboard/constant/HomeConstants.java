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

  public static final String REDIRECT_TO_LOGIN = "redirect:/login";
  public static final String ACCOUNT_OVERVIEW_URL_MAPPING = "/account-overview";

  /** Redirect URL Mapping Constants. */
  public static final String REDIRECT_TO_ACCOUNT_OVERVIEW = "redirect:/account-overview";

  /** View Name Constants. */
  public static final String INDEX_VIEW_NAME = "index";

  public static final String ACCOUNT_OVERVIEW_NAME = "overview";

  private HomeConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
