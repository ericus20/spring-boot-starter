package com.developersboard.backend.service.security;

import com.developersboard.backend.service.security.impl.JwtServiceImpl;
import com.developersboard.shared.util.core.JwtUtils;
import com.developersboard.shared.util.core.JwtUtils.JwtTokenType;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"jwt.secret=secret"})
@ContextConfiguration(classes = {JwtServiceTest.ContextConfiguration.class})
class JwtServiceTest {

  @Autowired private transient JwtService jwtService;

  public static class ContextConfiguration {
    @Bean
    JwtService jwtService() {
      return new JwtServiceImpl();
    }
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
