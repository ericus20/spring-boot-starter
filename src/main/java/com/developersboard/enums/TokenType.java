package com.developersboard.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * TokenType defines the type of tokens supported in the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum TokenType {
  /** The access token. */
  ACCESS("accessToken"),

  /** The JSESSIONID token. */
  JSESSIONID("JSESSIONID"),

  /** The refresh token */
  REFRESH("refreshToken");

  private final String name;
}
