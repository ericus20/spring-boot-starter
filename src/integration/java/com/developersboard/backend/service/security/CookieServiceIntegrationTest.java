package com.developersboard.backend.service.security;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.constant.SecurityConstants;
import com.developersboard.enums.TokenType;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.http.HttpHeaders;

class CookieServiceIntegrationTest extends IntegrationTestUtils {

  private static final int LENGTH_OF_KEY_VALUE_PAIR = 2;
  private static final int DURATION = 1;
  private static final String HTTP_ONLY = "HttpOnly";
  private static final String MAX_AGE = "Max-Age";
  private static final String SAME_SITE = "SameSite";

  private transient String jwtToken;

  @BeforeEach
  void setUp(TestInfo testInfo) {
    jwtToken = jwtService.generateJwtToken(testInfo.getDisplayName());
  }

  @Test
  void testGeneratesEncryptedCookieWithTokenThenDecrypt() {
    var tokenCookie = cookieService.createTokenCookie(jwtToken, TokenType.ACCESS);
    String decryptedJwtToken = encryptionService.decrypt(tokenCookie.getValue());

    Assertions.assertEquals(jwtToken, decryptedJwtToken);
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
    var duration = Duration.ofDays(SecurityConstants.REFRESH_TOKEN_DURATION);

    var httpHeaders = cookieService.addCookieToHeaders(TokenType.REFRESH, jwtToken);
    assertAddCookieToHeader(httpHeaders, jwtToken, duration);
  }

  @Test
  void testAddTokenCookieToHeaderWithNullDuration() {
    var duration = Duration.ofDays(SecurityConstants.REFRESH_TOKEN_DURATION);

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
    Map<String, String> parsedCookies = parseCookies(httpCookie);

    var rawJwt = encryptionService.decrypt(parsedCookies.get(TokenType.REFRESH.getName()));

    Assertions.assertEquals(rawJwt, token);
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
