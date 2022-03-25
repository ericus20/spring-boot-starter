package com.developersboard.backend.service.security;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 * This is the contract for the jwt service operations.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public interface JwtService {

  /**
   * Generate a JwtToken for the specified username.
   *
   * @param username the username
   * @return the token
   */
  String generateJwtToken(String username);

  /**
   * Generate a JwtToken for the specified username.
   *
   * @param username the username
   * @param expiration the expiration date
   * @return the token
   */
  String generateJwtToken(String username, Date expiration);

  /**
   * Retrieve username from the token.
   *
   * @param token the token
   * @return the username
   */
  String getUsernameFromToken(String token);

  /**
   * Retrieves the jwt token from the request cookie or request header if present and valid.
   *
   * @param request the httpRequest
   * @param fromCookie if jwt should be retrieved from the cookies.
   * @return the jwt token
   */
  String getJwtToken(HttpServletRequest request, boolean fromCookie);

  /**
   * Validates the Jwt token passed to it.
   *
   * @param token the token
   * @return if valid or not
   */
  boolean isValidJwtToken(String token);
}
