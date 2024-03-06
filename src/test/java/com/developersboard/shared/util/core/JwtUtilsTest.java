package com.developersboard.shared.util.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.developersboard.shared.util.core.JwtUtils.JwtTokenType;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
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

  @Test
  public void generatedKeyLengthShouldBeValid() {
    // For 36 bytes new byte[36], the calculation would generally be:
    //
    // 36 bytes = 288 bits.
    // Base64 encodes each 6 bits into one character, so you would expect 288 / 6 = 48 characters.
    String key = JwtUtils.generateSecretKey();
    assertEquals(48, key.length(), "The length of the key should be 48 characters.");
  }

  @Test
  public void shouldGenerateUniqueKeys() {
    Set<String> keys = new HashSet<>();
    for (int i = 0; i < 100; i++) {
      String key = JwtUtils.generateSecretKey();
      assertTrue(keys.add(key), "Generated key should be unique.");
    }
  }

  @Test
  public void testKeyPattern() {
    String key = JwtUtils.generateSecretKey();
    Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]*$");
    assertTrue(pattern.matcher(key).matches(), "The key should match the Base64 URL Safe pattern.");
  }
}
