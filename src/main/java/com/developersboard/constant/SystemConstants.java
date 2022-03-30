package com.developersboard.constant;

/**
 * Generic system constants used in the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class SystemConstants {

  public static final String SYSTEM_NAME = "Crest Bank Inc.";
  public static final String ROUTING_NUMBER = "695984269";

  private SystemConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
