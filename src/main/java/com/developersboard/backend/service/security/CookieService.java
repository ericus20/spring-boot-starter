package com.developersboard.backend.service.security;

import com.developersboard.enums.TokenType;
import java.time.Duration;
import javax.servlet.http.Cookie;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;

/**
 * Provides mechanism to create and manage cookies.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public interface CookieService {

  /**
   * Creates a servlet cookie from spring httpCookie.
   *
   * @param httpCookie the httpCookie
   * @return the cookie
   */
  Cookie createCookie(HttpCookie httpCookie);

  /**
   * Creates an httpOnly httpCookie.
   *
   * @param name the name of the cookie
   * @param value the value of the cookie
   * @param duration the duration till expiration
   * @return the cookie
   */
  HttpCookie createCookie(String name, String value, Duration duration);

  /**
   * Creates a cookie with the specified token and token type.
   *
   * @param token the token
   * @param tokenType the type of token
   * @return the cookie
   */
  HttpCookie createTokenCookie(String token, TokenType tokenType);

  /**
   * Creates a cookie with the specified token and token type.
   *
   * @param token the token
   * @param tokenType the type of token
   * @param duration the duration till expiration
   * @return the cookie
   */
  HttpCookie createTokenCookie(String token, TokenType tokenType, Duration duration);

  /**
   * Creates a cookie with the specified token.
   *
   * @param tokenType the token type
   * @return the cookie
   */
  HttpCookie deleteTokenCookie(TokenType tokenType);

  /**
   * Creates a cookie with the specified token.
   *
   * @param tokenType the token type
   * @return the httpHeaders
   */
  HttpHeaders addDeletedCookieToHeaders(TokenType tokenType);

  /**
   * creates a cookie with the specified token and tokenType then adds it to headers.
   *
   * @param tokenType the tokenType
   * @param token the token
   * @return the httpHeaders
   */
  HttpHeaders addCookieToHeaders(TokenType tokenType, String token);

  /**
   * creates a cookie with the specified token and tokenType then adds it to headers.
   *
   * @param tokenType the tokenType
   * @param token the token
   * @param duration the duration till expiration
   * @return the httpHeaders
   */
  HttpHeaders addCookieToHeaders(TokenType tokenType, String token, Duration duration);

  /**
   * creates a cookie with the specified token and tokenType with duration then adds it to the
   * headers.
   *
   * @param httpHeaders the httpHeaders
   * @param tokenType the tokenType
   * @param token the token
   * @param duration the duration till expiration
   */
  void addCookieToHeaders(
      HttpHeaders httpHeaders, TokenType tokenType, String token, Duration duration);
}
