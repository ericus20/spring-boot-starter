package com.developersboard.shared.util.core;

import com.developersboard.constant.ErrorConstants;
import com.github.javafaker.Faker;

/**
 * This utility class holds custom operations on strings used in the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class StringUtils {

  public static final Faker FAKER = new Faker();

  private StringUtils() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
