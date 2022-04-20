package com.developersboard.backend.service.security;

import com.developersboard.IntegrationTestUtils;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

class JwtServiceIntegrationTest extends IntegrationTestUtils {

  @Test
  void generateJwtTokenThenValidate(TestInfo testInfo) {
    var jwtToken = jwtService.generateJwtToken(testInfo.getDisplayName());
    Assertions.assertTrue(jwtService.isValidJwtToken(jwtToken));
  }

  @Test
  void generateJwtTokenWithNullThrowsException() {
    Assertions.assertThrows(NullPointerException.class, () -> jwtService.generateJwtToken(null));
  }

  @Test
  void generateJwtTokenThenVerifyUsername(TestInfo testInfo) {
    String jwtToken = jwtService.generateJwtToken(testInfo.getDisplayName());

    Assertions.assertEquals(testInfo.getDisplayName(), jwtService.getUsernameFromToken(jwtToken));
  }

  @Test
  void getUsernameFromTokenWithNullThrowsException() {
    Assertions.assertThrows(
        NullPointerException.class, () -> jwtService.getUsernameFromToken(null));
  }

  @Test
  void generateExpiredJwtTokenThenValidate(TestInfo testInfo) {
    // Basically setting current date - 2 which is two days before today.
    int twoDaysPast = -JWT_SIGNATURE_PART;
    var expirationDate = DateUtils.addDays(new Date(), twoDaysPast);
    String jwtToken = jwtService.generateJwtToken(testInfo.getDisplayName(), expirationDate);

    Assertions.assertFalse(jwtService.isValidJwtToken(jwtToken));
  }

  @Test
  void isValidJwtTokenWithIllegalArgument() {
    Assertions.assertFalse(jwtService.isValidJwtToken(null));
  }

  @Test
  void isValidJwtTokenWithBadSignatureJwtToken(TestInfo testInfo) {
    var badSignatureJwtToken = getTestJwtTokenByType(JwtTokenType.BAD_SIGNATURE, testInfo);
    Assertions.assertFalse(jwtService.isValidJwtToken(badSignatureJwtToken));
  }

  @Test
  void isValidJwtTokenWithMalformedJwtToken(TestInfo testInfo) {
    var malformedJwtToken = getTestJwtTokenByType(JwtTokenType.MALFORMED, testInfo);
    Assertions.assertFalse(jwtService.isValidJwtToken(malformedJwtToken));
  }

  @Test
  void isValidJwtTokenWithUnsupportedJwtToken(TestInfo testInfo) {
    var unSupportedJwtToken = getTestJwtTokenByType(JwtTokenType.UNSUPPORTED, testInfo);
    Assertions.assertFalse(jwtService.isValidJwtToken(unSupportedJwtToken));
  }
}
