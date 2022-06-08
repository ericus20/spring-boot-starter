package com.developersboard.backend.service.security;

import com.developersboard.backend.service.security.impl.CookieServiceImpl;
import com.developersboard.backend.service.security.impl.JwtServiceImpl;
import com.developersboard.constant.ProfileTypeConstants;
import com.developersboard.constant.SecurityConstants;
import com.developersboard.enums.TokenType;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.env.MockEnvironment;

@TestInstance(Lifecycle.PER_CLASS)
class CookieServiceTest {

  private static final int LENGTH_OF_KEY_VALUE_PAIR = 2;
  private static final int DURATION = 1;
  private static final String HTTP_ONLY = "HttpOnly";
  private static final String MAX_AGE = "Max-Age";
  private static final String SAME_SITE = "SameSite";
  private transient JwtService jwtService;
  private transient CookieService cookieService;
  private transient String jwtToken;

  @BeforeAll
  void beforeAll() {
    jwtService = new JwtServiceImpl("secret");

    var environment = new MockEnvironment();
    environment.addActiveProfile(ProfileTypeConstants.TEST);

    cookieService = new CookieServiceImpl(environment);
  }

  @BeforeEach
  void setUp(TestInfo testInfo) {
    jwtToken = jwtService.generateJwtToken(testInfo.getDisplayName());
  }

  @Test
  void testGeneratesCookieWithTokenThenDecrypt() {
    var tokenCookie = cookieService.createTokenCookie(jwtToken, TokenType.ACCESS);

    Assertions.assertEquals(jwtToken, tokenCookie.getValue());
  }

  @Test
  void testCreateTokenCookieWithNullThrowsException() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          Duration duration = Duration.ofHours(DURATION);
          cookieService.createTokenCookie(null, TokenType.ACCESS, duration);
        });
  }

  @Test
  void testCreateHttpCookie() {
    var duration = Duration.ofHours(DURATION);
    var cookie = cookieService.createCookie(TokenType.ACCESS.getName(), jwtToken, duration);
    Assertions.assertNotNull(cookie);

    assertCookie(cookie.getName(), cookie.toString(), jwtToken, duration);
  }

  @Test
  void testCreateHttpCookieWithNullDurationShouldUseDefaultDuration() {
    Duration duration = Duration.ofDays(SecurityConstants.DEFAULT_TOKEN_DURATION);
    var cookie = cookieService.createCookie(TokenType.ACCESS.getName(), jwtToken, null);
    Assertions.assertNotNull(cookie);

    // creating cookie with null duration should use the default duration
    assertCookie(cookie.getName(), cookie.toString(), jwtToken, duration);
  }

  @Test
  void testGeneratesCookieTokenThenDeletesIt() {
    var tokenCookie =
        cookieService.createTokenCookie(jwtToken, TokenType.ACCESS, Duration.ofHours(DURATION));
    Assertions.assertTrue(StringUtils.isNotBlank(tokenCookie.getValue()));

    var httpCookie = cookieService.deleteTokenCookie(TokenType.ACCESS);
    Assertions.assertTrue(StringUtils.isBlank(httpCookie.getValue()));
  }

  @Test
  void testDeleteTokenCookieWithNullThrowsException() {
    Assertions.assertThrows(
        NullPointerException.class, () -> cookieService.deleteTokenCookie(null));
  }

  @Test
  void testAddTokenCookieToHeaderWithDefaultDuration() {
    var duration = Duration.ofDays(SecurityConstants.DEFAULT_TOKEN_DURATION);

    var httpHeaders = cookieService.addCookieToHeaders(TokenType.REFRESH, jwtToken);
    assertAddCookieToHeader(httpHeaders, jwtToken, duration);
  }

  @Test
  void testAddTokenCookieToHeaderWithNullDuration() {
    var duration = Duration.ofDays(SecurityConstants.DEFAULT_TOKEN_DURATION);

    var httpHeaders = cookieService.addCookieToHeaders(TokenType.REFRESH, jwtToken, null);
    assertAddCookieToHeader(httpHeaders, jwtToken, duration);
  }

  @Test
  void testAddTokenCookieToHeaderWithCustomDuration() {
    var duration = Duration.ofDays(DURATION);

    var httpHeaders = cookieService.addCookieToHeaders(TokenType.REFRESH, jwtToken, duration);
    assertAddCookieToHeader(httpHeaders, jwtToken, duration);
  }

  private void assertAddCookieToHeader(HttpHeaders httpHeaders, String token, Duration duration) {
    Assertions.assertTrue(httpHeaders.containsKey(HttpHeaders.SET_COOKIE));

    var httpCookie = httpHeaders.getFirst(HttpHeaders.SET_COOKIE);
    assertCookie(TokenType.REFRESH.getName(), httpCookie, token, duration);
  }

  private void assertCookie(String name, String value, String token, Duration duration) {
    Map<String, String> parsedCookies = parseCookies(value);

    Assertions.assertEquals(token, parsedCookies.get(name));
    Assertions.assertTrue(parsedCookies.containsKey(HTTP_ONLY));
    Assertions.assertEquals(parsedCookies.get(MAX_AGE), String.valueOf(duration.getSeconds()));
    Assertions.assertEquals(parsedCookies.get(SAME_SITE), SecurityConstants.SAME_SITE);
  }

  private Map<String, String> parseCookies(String cookie) {
    Assertions.assertNotNull(cookie);

    Map<String, String> cookies = new HashMap<>();
    var cookieArray = cookie.split(";");

    for (var cookieValue : cookieArray) {
      var cookieKeyValue = cookieValue.strip().split("=");
      if (cookieKeyValue.length == LENGTH_OF_KEY_VALUE_PAIR) {
        cookies.put(cookieKeyValue[0], cookieKeyValue[1]);
      } else {
        cookies.put(cookieKeyValue[0], "");
      }
    }
    return cookies;
  }
}
