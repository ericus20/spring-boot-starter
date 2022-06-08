package com.developersboard.backend.service.security;

import com.developersboard.backend.service.security.impl.JwtServiceImpl;
import com.developersboard.shared.util.core.JwtUtils;
import com.developersboard.shared.util.core.JwtUtils.JwtTokenType;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtServiceTest {

  private transient JwtService jwtService;

  @BeforeAll
  void beforeAll() {
    jwtService = new JwtServiceImpl("secret");
  }

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
    int twoDaysPast = -JwtUtils.JWT_SIGNATURE_PART;
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
    var jwt = jwtService.generateJwtToken(testInfo.getDisplayName());
    var badSignatureJwtToken = JwtUtils.generateTestJwtToken(jwt, JwtTokenType.BAD_SIGNATURE);
    Assertions.assertFalse(jwtService.isValidJwtToken(badSignatureJwtToken));
  }

  @Test
  void isValidJwtTokenWithMalformedJwtToken(TestInfo testInfo) {
    var jwt = jwtService.generateJwtToken(testInfo.getDisplayName());
    var malformedJwtToken = JwtUtils.generateTestJwtToken(jwt, JwtTokenType.MALFORMED);
    Assertions.assertFalse(jwtService.isValidJwtToken(malformedJwtToken));
  }

  @Test
  void isValidJwtTokenWithUnsupportedJwtToken(TestInfo testInfo) {
    var jwt = jwtService.generateJwtToken(testInfo.getDisplayName());
    var unSupportedJwtToken = JwtUtils.generateTestJwtToken(jwt, JwtTokenType.UNSUPPORTED);
    Assertions.assertFalse(jwtService.isValidJwtToken(unSupportedJwtToken));
  }
}
