package com.developersboard.shared.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.developersboard.web.payload.request.SignUpRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.platform.commons.util.ReflectionUtils;

class SignUpUtilsTest {

  @Test
  void callingConstructorShouldThrowException() {
    Assertions.assertThrows(
        AssertionError.class, () -> ReflectionUtils.newInstance(SignUpUtils.class));
  }

  @Test
  void shouldGenerateSignUpRequestWithRandomCredentialsWhenNoParametersProvided() {
    SignUpRequest signUpRequest = SignUpUtils.createSignUpRequest();

    assertNotNull(signUpRequest);
    assertNotNull(signUpRequest.getUsername());
    assertTrue(signUpRequest.getPassword().length() >= SignUpUtils.PASSWORD_MIN_LENGTH);
    assertTrue(signUpRequest.getPassword().length() <= SignUpUtils.PASSWORD_MAX_LENGTH);
    assertNotNull(signUpRequest.getEmail());
  }

  @Test
  void shouldGenerateSignUpRequestWithSpecifiedUsernameWhenUsernameIsProvided(TestInfo testInfo) {
    String expectedUsername = testInfo.getDisplayName();
    SignUpRequest signUpRequest = SignUpUtils.createSignUpRequest(expectedUsername);

    assertEquals(expectedUsername, signUpRequest.getUsername());
  }

  @Test
  void shouldGenerateSignUpRequestWithSpecifiedUsernameAndEmailWhenUsernameAndEmailAreProvided() {
    String expectedUsername = "testUser";
    String expectedEmail = "test@example.com";
    SignUpRequest signUpRequest = SignUpUtils.createSignUpRequest(expectedUsername, expectedEmail);

    assertEquals(expectedUsername, signUpRequest.getUsername());
    assertEquals(expectedEmail, signUpRequest.getEmail());
  }

  @Test
  void shouldExtractConfirmAccountLinkWhenHtmlContainsConfirmationLink() {
    String htmlString =
        "<html><body><a href='http://localhost/sign-up/verify?token=123'>Confirm Account</a></body></html>";
    String expectedLink = "http://localhost/sign-up/verify?token=123";
    String actualLink = SignUpUtils.extractConfirmAccountLink(htmlString);

    assertEquals(expectedLink, actualLink);
  }

  @Test
  void shouldReturnNullWhenHtmlDoesNotContainConfirmationLink() {
    String htmlString = "<html><body>No Link Here</body></html>";

    assertNull(SignUpUtils.extractConfirmAccountLink(htmlString));
  }

  @Test
  void shouldThrowNullPointerExceptionWhenHtmlStringIsNull() {
    assertThrows(NullPointerException.class, () -> SignUpUtils.extractConfirmAccountLink(null));
  }
}
