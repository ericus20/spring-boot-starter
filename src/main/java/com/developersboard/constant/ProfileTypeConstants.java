package com.developersboard.constant;

/**
 * This class holds all profile type constants used in the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class ProfileTypeConstants {

  public static final String DEV = "dev";
  public static final String DEV_DOCKER = "dev-docker";
  public static final String PROD = "prod";
  public static final String TEST = "test";

  private ProfileTypeConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
