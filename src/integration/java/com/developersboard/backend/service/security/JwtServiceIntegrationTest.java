package com.developersboard.backend.service.security;

import com.developersboard.IntegrationTestUtils;
import java.util.Date;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;

class JwtServiceIntegrationTest extends IntegrationTestUtils {

  public static final int NUM_OF_JWT_PARTS = 3;
  public static final int JWT_HEADER_PART = 0;
  public static final int JWT_PAYLOAD_PART = 1;
  public static final int JWT_SIGNATURE_PART = 2;
  public static final String DELIMITER = ".";

  private enum TokenType {
    BAD_SIGNATURE,
    MALFORMED,
    UNSUPPORTED
  }

  @Autowired private transient JwtService jwtService;

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
    var badSignatureJwtToken = getJwtTokenByType(TokenType.BAD_SIGNATURE, testInfo);
    Assertions.assertFalse(jwtService.isValidJwtToken(badSignatureJwtToken));
  }

  @Test
  void isValidJwtTokenWithMalformedJwtToken(TestInfo testInfo) {
    var malformedJwtToken = getJwtTokenByType(TokenType.MALFORMED, testInfo);
    Assertions.assertFalse(jwtService.isValidJwtToken(malformedJwtToken));
  }

  @Test
  void isValidJwtTokenWithUnsupportedJwtToken(TestInfo testInfo) {
    var unSupportedJwtToken = getJwtTokenByType(TokenType.UNSUPPORTED, testInfo);
    Assertions.assertFalse(jwtService.isValidJwtToken(unSupportedJwtToken));
  }

  /**
   * Generate an invalid jwt token based on the type provided. Jwt has the format
   * header(algorithm).payload.signature
   *
   * @param tokenType the token type
   * @param testInfo the testInfo
   * @return the jwt token
   */
  private String getJwtTokenByType(TokenType tokenType, TestInfo testInfo) {
    var jwtToken = jwtService.generateJwtToken(testInfo.getDisplayName());
    if (Objects.nonNull(jwtToken)) {
      var separatedJwtToken = jwtToken.split("\\.");
      if (separatedJwtToken.length == NUM_OF_JWT_PARTS) {
        var header = separatedJwtToken[JWT_HEADER_PART];
        var payload = separatedJwtToken[JWT_PAYLOAD_PART];
        var signature = separatedJwtToken[JWT_SIGNATURE_PART];

        if (StringUtils.isNotBlank(header)
            && StringUtils.isNotBlank(payload)
            && StringUtils.isNotBlank(signature)) {

          if (tokenType == TokenType.BAD_SIGNATURE) {
            return String.join(
                DELIMITER,
                header,
                payload.substring(payload.length() / JWT_SIGNATURE_PART),
                signature);
          } else if (tokenType == TokenType.MALFORMED) {
            return String.join(DELIMITER, header, payload);
          } else if (tokenType == TokenType.UNSUPPORTED) {
            return String.join(DELIMITER, header, payload, "");
          }
        }
      }
    }
    return null;
  }
}
