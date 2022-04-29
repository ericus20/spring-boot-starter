package com.developersboard.shared.util.core;

import com.developersboard.shared.util.core.JwtUtils.JwtTokenType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

class JwtUtilsTest {

  @Test
  void callingConstructorShouldThrowException() {
    Assertions.assertThrows(
        AssertionError.class, () -> ReflectionUtils.newInstance(JwtUtils.class));
  }

  @Test
  void generateTokenWithBlankTokenShouldReturnNull() {
    Assertions.assertNull(JwtUtils.generateTestJwtToken(StringUtils.EMPTY, JwtTokenType.MALFORMED));
  }

  @Test
  void generateTokenWithMoreThanThreePartsShouldReturnNull() {
    var jwt = "this.is.not.valid.jwt";
    Assertions.assertNull(JwtUtils.generateTestJwtToken(jwt, JwtTokenType.MALFORMED));
  }

  @Test
  void generateTokenWithBlankHeaderShouldReturnNull() {
    var jwt = ".not.valid";
    Assertions.assertNull(JwtUtils.generateTestJwtToken(jwt, JwtTokenType.MALFORMED));
  }

  @Test
  void generateTokenWithBlankPayloadShouldReturnNull() {
    var jwt = "not..valid";
    Assertions.assertNull(JwtUtils.generateTestJwtToken(jwt, JwtTokenType.UNSUPPORTED));
  }

  @Test
  void generateTokenWithBlankSignatureShouldReturnNull() {
    var jwt = "not.valid.";
    Assertions.assertNull(JwtUtils.generateTestJwtToken(jwt, JwtTokenType.BAD_SIGNATURE));
  }

  @Test
  void generateTokenShouldReturnExpectedBrokenToken() {
    var jwt = "should.be.valid";
    String badSignatureJwt = JwtUtils.generateTestJwtToken(jwt, JwtTokenType.BAD_SIGNATURE);

    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(badSignatureJwt);
          Assertions.assertNotEquals(jwt, badSignatureJwt);
        });
  }
}
