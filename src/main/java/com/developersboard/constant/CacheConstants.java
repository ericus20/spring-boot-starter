package com.developersboard.constant;

/**
 * Constants used in the Cache Maps as keys to the values.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class CacheConstants {

  /** Constant for the cache key for the user. */
  public static final String USERS = "users";

  /** Constant for the cache key for the roles */
  public static final String ROLES = "roles";

  /** Constant for the cache key for user details */
  public static final String USER_DETAILS = "userDetails";

  /** Constant for the cache key for the user histories */
  public static final String USER_HISTORIES = "userHistories";

  private CacheConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
