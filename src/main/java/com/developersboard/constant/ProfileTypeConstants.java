package com.developersboard.constant;

/**
 * This class holds all profile type constants used in the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class ProfileTypeConstants {

  /** The dev profile exposes development specific beans and configurations. */
  public static final String DEV = "development";

  /** The prod profile exposes production specific beans and configurations. */
  public static final String PROD = "production";

  /** The test profile exposes testing specific beans and configurations. */
  public static final String TEST = "test";

  private ProfileTypeConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
