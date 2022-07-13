package com.developersboard.constant;

import com.developersboard.constant.user.PasswordConstants;
import com.developersboard.constant.user.SignUpConstants;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpMethod;

/**
 * This class holds all security-related URL mappings constants.
 *
 * @author George Anguah
 * @version 1.0
 * @since 1.0
 */
public final class SecurityConstants {

  public static final String API_V1_AUTH_ROOT_URL = "/api/v1/auth";
  public static final String API_V1_AUTH_URL_MAPPING = "/api/v1/auth/**";
  public static final String API_ROOT_URL_MAPPING = "/api/**";
  public static final String BEARER = "Bearer";
  public static final String BEARER_PREFIX = "Bearer ";
  public static final String X_XSRF_TOKEN = "x-xsrf-token";
  public static final String H2_CONSOLE_URL_MAPPING = "/console/*";
  public static final String JSESSIONID = "JSESSIONID";
  public static final String LOGIN_LOGOUT = "/login?logout";
  public static final String LOGOUT = "/logout";
  public static final String LOGIN = "/login";
  public static final String LOGIN_FAILURE_URL = "/login?error";
  public static final String REFRESH_TOKEN = "/refresh-token";
  public static final String REMEMBER_ME = "remember-me";
  public static final String ROOT_PATH = "/";
  public static final String SAME_SITE = "strict";

  public static final String LOGIN_VIEW_NAME = "user/login";

  public static final int DEFAULT_TOKEN_DURATION = 7;

  public static final int SECURITY_STRENGTH = 12;
  public static final List<String> ALLOWED_HTTP_METHODS =
      List.of(
          HttpMethod.GET.name(),
          HttpMethod.POST.name(),
          HttpMethod.PUT.name(),
          HttpMethod.DELETE.name(),
          HttpMethod.PATCH.name(),
          HttpMethod.OPTIONS.name());
  public static final List<String> ALLOWED_HTTP_HEADERS = List.of("*");
  public static final List<String> EXPOSED_HTTP_HEADERS = List.of("*");
  private static final String[] PUBLIC_MATCHERS = {
    "/css/**",
    "/js/**",
    "/images/**",
    "/fonts/**",
    "/webjars/**",
    "/resources/**",
    "/static/**",
    "/console/**",
    "/actuator/health",
    "/v3/api-docs/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    ROOT_PATH,
    String.join("/", SecurityConstants.LOGIN, "**"),
    String.join("/", SignUpConstants.SIGN_UP_MAPPING, "**"),
    String.join("/", ContactConstants.CONTACT_URL_MAPPING, "**"),
    String.join("/", PasswordConstants.PASSWORD_RESET_ROOT_MAPPING, "**"),
    "/api/v1/users/datatables"
  };

  private SecurityConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }

  /**
   * Public matchers to allow access to the application.
   *
   * @return public matchers.
   */
  public static Collection<String> getPublicMatchers() {
    return Collections.unmodifiableCollection(Arrays.asList(PUBLIC_MATCHERS));
  }
}
