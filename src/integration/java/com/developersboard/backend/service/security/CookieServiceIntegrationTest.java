package com.developersboard.backend.service.security;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.enums.TokenType;
import java.time.Duration;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;

class CookieServiceIntegrationTest extends IntegrationTestUtils {

  private static final int DURATION = 1;

  @Autowired private transient CookieService cookieService;

  @Autowired private transient JwtService jwtService;

  @Autowired private transient EncryptionService encryptionService;

  @Test
  void testGeneratesEncryptedCookieWithTokenThenDecrypt(TestInfo testInfo) {
    var jwtToken = jwtService.generateJwtToken(testInfo.getDisplayName());
    Assertions.assertTrue(jwtService.isValidJwtToken(jwtToken));

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
  void testGeneratesCookieTokenThenDeletesIt(TestInfo testInfo) {
    var jwtToken = jwtService.generateJwtToken(testInfo.getDisplayName());

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
}
