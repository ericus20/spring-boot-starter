package com.developersboard.backend.service.security;

import com.developersboard.backend.service.security.impl.EncryptionServiceImpl;
import com.developersboard.exception.EncryptionException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class EncryptionServiceTest {

  private transient EncryptionService encryptionService;
  private transient String uri;

  @BeforeEach
  void setUp() {
    encryptionService = new EncryptionServiceImpl("salt", "password");

    uri =
        "http://localhost:8080/user/sign-up?id=MKbscZZTo3jMpiUM"
            + "Hh2n9fZUpqtTBcLrA+TOvhithkc=&token=ybrzAV891Y/28P7kf7mhz8GlRdlmNiXdjGc3eVJrkUyI"
            + "6dnmuPTcwVxqWbWP0wsC";
  }

  @Test
  void encryptAndDecryptRequestUri() {
    var encryptRequestUri = encryptionService.encrypt(uri);
    Assertions.assertNotNull(encryptRequestUri);
    Assertions.assertNotEquals(encryptRequestUri, uri);

    var decryptRequestUri = encryptionService.decrypt(encryptRequestUri);
    Assertions.assertNotNull(decryptRequestUri);
    Assertions.assertNotEquals(decryptRequestUri, encryptRequestUri);

    Assertions.assertEquals(uri, decryptRequestUri);
  }

  @Test
  void encryptWithNullThrowsException() {
    Assertions.assertNull(encryptionService.encrypt(StringUtils.EMPTY));
  }

  @Test
  void decryptWithNullThrowsException() {
    Assertions.assertNull(encryptionService.decrypt(StringUtils.EMPTY));
  }

  @Test
  void encryptThrowingExceptionShouldBeHandled(TestInfo testInfo) {
    try (MockedStatic<Cipher> cipher = Mockito.mockStatic(Cipher.class)) {
      cipher
          .when(() -> Cipher.getInstance(ArgumentMatchers.any()))
          .thenThrow(new NoSuchAlgorithmException());

      Assertions.assertThrows(
          EncryptionException.class, () -> encryptionService.encrypt(testInfo.getDisplayName()));
    }
  }

  @Test
  void decryptThrowingExceptionShouldBeHandled(TestInfo testInfo) {
    try (MockedStatic<Cipher> cipher = Mockito.mockStatic(Cipher.class)) {
      cipher
          .when(() -> Cipher.getInstance(ArgumentMatchers.any()))
          .thenThrow(new NoSuchAlgorithmException());

      Assertions.assertThrows(
          EncryptionException.class, () -> encryptionService.decrypt(testInfo.getDisplayName()));
    }
  }

  @Test
  void generatingSecretKeyInEncryptThrowingExceptionShouldBeHandled(TestInfo testInfo) {
    try (MockedStatic<SecretKeyFactory> mockStatic = Mockito.mockStatic(SecretKeyFactory.class)) {
      mockStatic
          .when(
              () -> {
                var string = ArgumentMatchers.anyString();
                SecretKeyFactory.getInstance(string);
              })
          .thenThrow(new NoSuchAlgorithmException());

      Assertions.assertThrows(
          EncryptionException.class, () -> encryptionService.encrypt(testInfo.getDisplayName()));
    }
  }

  @Test
  void generatingSecretKeyInDecryptThrowingExceptionShouldBeHandled(TestInfo testInfo)
      throws Exception {
    var mock = Mockito.mock(SecretKeyFactory.class);
    Mockito.when(mock.generateSecret(ArgumentMatchers.any()))
        .thenThrow(new InvalidKeySpecException());

    Assertions.assertThrows(
        EncryptionException.class, () -> encryptionService.decrypt(testInfo.getDisplayName()));
  }

  @Test
  void testEncodingAndDecoding() {
    var encodedUri = encryptionService.encode(uri);
    Assertions.assertNotNull(encodedUri);
    Assertions.assertNotEquals(encodedUri, uri);

    var decodedUri = encryptionService.decode(encodedUri);
    Assertions.assertNotNull(decodedUri);
    Assertions.assertNotEquals(decodedUri, encodedUri);

    Assertions.assertEquals(uri, decodedUri);
  }

  @Test
  void encodeWithNullThrowsException() {
    Assertions.assertNull(encryptionService.encode(StringUtils.EMPTY));
  }

  @Test
  void decodeWithNullThrowsException() {
    Assertions.assertNull(encryptionService.decode(StringUtils.EMPTY));
  }
}
