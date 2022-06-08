package com.developersboard.backend.service.security.impl;

import com.developersboard.backend.service.security.JwtService;
import com.developersboard.constant.SecurityConstants;
import com.developersboard.constant.user.UserConstants;
import com.developersboard.enums.TokenType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 * This is the implementation of the jwt service.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

  private static final String TOKEN_CREATED_SUCCESS = "Token successfully created as {}";
  private static final int NUMBER_OF_DAYS_TO_EXPIRE = 1;
  private final transient String jwtSecret;

  public JwtServiceImpl(@Value("${jwt.secret}") String jwtSecret) {
    this.jwtSecret = jwtSecret;
  }

  /**
   * Generate a JwtToken for the specified username.
   *
   * @param username the username
   * @return the token
   */
  @Override
  public String generateJwtToken(final String username) {
    Validate.notBlank(username, UserConstants.BLANK_USERNAME);

    return generateJwtToken(username, DateUtils.addDays(new Date(), NUMBER_OF_DAYS_TO_EXPIRE));
  }

  /**
   * Generate a JwtToken for the specified username.
   *
   * @param username the username
   * @param expiration the expiration date
   * @return the token
   */
  @Override
  public String generateJwtToken(final String username, final Date expiration) {
    Validate.notBlank(username, UserConstants.BLANK_USERNAME);

    var jwtToken =
        Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();

    LOG.debug(TOKEN_CREATED_SUCCESS, jwtToken);
    return jwtToken;
  }

  /**
   * Retrieve username from the token.
   *
   * @param token the token
   * @return the username
   */
  @Override
  public String getUsernameFromToken(final String token) {
    Validate.notBlank(token, "Token cannot be blank");

    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  /**
   * Retrieves the jwt token from the request cookie or request header if present and valid.
   *
   * @param request the httpRequest
   * @param fromCookie if jwt should be retrieved from the cookies.
   * @return the jwt token
   */
  @Override
  public String getJwtToken(final HttpServletRequest request, final boolean fromCookie) {
    if (fromCookie) {
      return getJwtFromCookie(request);
    }

    return getJwtFromRequest(request);
  }

  /**
   * Validates the Jwt token passed to it.
   *
   * @param token the token
   * @return if valid or not
   */
  @Override
  public boolean isValidJwtToken(final String token) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {
      LOG.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      LOG.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      LOG.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      LOG.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      LOG.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }

  /**
   * Retrieves the jwt token from the request header if present and valid.
   *
   * @param request the httpRequest
   * @return the jwt token
   */
  private String getJwtFromRequest(final HttpServletRequest request) {
    var headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (StringUtils.isNotBlank(headerAuth)
        && headerAuth.startsWith(SecurityConstants.BEARER_PREFIX)) {
      return headerAuth.split(StringUtils.SPACE)[NUMBER_OF_DAYS_TO_EXPIRE];
    }
    return null;
  }

  /**
   * Retrieves the jwt token from the request cookie if present and valid.
   *
   * @param request the httpRequest
   * @return the jwt token
   */
  private String getJwtFromCookie(final HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (Objects.nonNull(cookies)) {
      for (Cookie cookie : cookies) {
        if (TokenType.ACCESS.getName().equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }
}
