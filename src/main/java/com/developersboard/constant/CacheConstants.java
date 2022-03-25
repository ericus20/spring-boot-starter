package com.developersboard.constant;

/**
 * Constants used in the Cache Maps as keys to the values.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class CacheConstants {

  public static final String USERS = "users";
  public static final String ROLES = "roles";
  public static final String USER_DETAILS = "userDetails";

  private CacheConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
