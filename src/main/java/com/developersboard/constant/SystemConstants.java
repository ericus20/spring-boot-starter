package com.developersboard.constant;

/**
 * Generic system constants used in the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class SystemConstants {

  public static final String ROUTING_NUMBER = "695984269";
  public static final String ERROR = "error";
  public static final String ERROR_MESSAGE = "errorMessage";
  public static final String SYSTEM_NAME = "Spring Boot Starter.";

  private SystemConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
