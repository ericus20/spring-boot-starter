package com.developersboard.constant;

/**
 * This class holds all profile type constants used in the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class EnvConstants {

  /** The dev profile exposes development specific beans and configurations. */
  public static final String DEVELOPMENT = "development";

  /** The prod profile exposes production-specific beans and configurations. */
  public static final String PRODUCTION = "production";

  /** The test profile exposes testing specific beans and configurations. */
  public static final String TEST = "test";

  /** The test profile exposes integration testing specific beans and configurations. */
  public static final String INTEGRATION_TEST = "integration-test";

  /** The test profile exposes integration testing specific beans and configurations. */
  public static final String INTEGRATION_TEST_CI = "integration-test-ci";

  /** The test profile exposes docker-specific beans and configurations. */
  public static final String DOCKER = "docker";

  private EnvConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
