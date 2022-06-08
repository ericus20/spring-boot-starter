package com.developersboard.shared.util.core;

import com.developersboard.constant.ErrorConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * This utility class holds custom operations on JWT used in the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public final class JwtUtils {

  public static final int NUM_OF_JWT_PARTS = 3;
  public static final int JWT_HEADER_PART = 0;
  public static final int JWT_PAYLOAD_PART = 1;
  public static final int JWT_SIGNATURE_PART = 2;
  public static final String DELIMITER = ".";

  private JwtUtils() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }

  /**
   * Generate an invalid jwt token based on the type provided. Jwt has the format
   * header(algorithm).payload.signature
   *
   * @param jwt the jwt token
   * @param tokenType the token type
   * @return the jwt token
   */
  public static String generateTestJwtToken(String jwt, JwtTokenType tokenType) {
    if (StringUtils.isNotBlank(jwt)) {
      var separatedJwtToken = jwt.split("\\.");
      if (separatedJwtToken.length == NUM_OF_JWT_PARTS) {
        var header = separatedJwtToken[JWT_HEADER_PART];
        var payload = separatedJwtToken[JWT_PAYLOAD_PART];
        var signature = separatedJwtToken[JWT_SIGNATURE_PART];

        return generateToken(tokenType, header, payload, signature);
      }
    }
    return null;
  }

  private static String generateToken(
      JwtTokenType tokenType, String header, String payload, String signature) {
    if (StringUtils.isNotBlank(header)
        && StringUtils.isNotBlank(payload)
        && StringUtils.isNotBlank(signature)) {

      if (tokenType == JwtTokenType.BAD_SIGNATURE) {
        return String.join(
            DELIMITER, header, payload.substring(payload.length() / JWT_SIGNATURE_PART), signature);
      } else if (tokenType == JwtTokenType.MALFORMED) {
        return String.join(DELIMITER, header, payload);
      } else if (tokenType == JwtTokenType.UNSUPPORTED) {
        return String.join(DELIMITER, header, payload, StringUtils.EMPTY);
      }
    }
    return null;
  }

  public enum JwtTokenType {
    BAD_SIGNATURE,
    MALFORMED,
    UNSUPPORTED
  }
}
