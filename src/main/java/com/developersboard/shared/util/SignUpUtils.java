package com.developersboard.shared.util;

import com.developersboard.constant.ErrorConstants;
import com.developersboard.web.payload.request.SignUpRequest;
import java.util.Objects;
import net.datafaker.Faker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * SignUp utility class that holds sign-up related methods used across application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class SignUpUtils {

  /** Maximum password length for the password generation. */
  public static final int PASSWORD_MAX_LENGTH = 15;

  /** The Constant FAKER. */
  private static final Faker FAKER = new Faker();

  /** Minimum password length for the password generation. */
  private static final int PASSWORD_MIN_LENGTH = 4;

  private SignUpUtils() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }

  /**
   * Creates a user using the mock mvc request.
   *
   * @return the signUpRequest
   */
  public static SignUpRequest createSignUpRequest() {
    return createSignUpRequest(FAKER.internet().username());
  }

  /**
   * Creates a user using the mock mvc request.
   *
   * @param username the username
   * @return the signUpRequest
   */
  public static SignUpRequest createSignUpRequest(String username) {
    return createSignUpRequest(username, FAKER.internet().emailAddress());
  }

  /**
   * Creates a user using the mock mvc request.
   *
   * @param username the username
   * @param email the email
   * @return the signUpRequest
   */
  public static SignUpRequest createSignUpRequest(String username, String email) {
    var signUpRequest = new SignUpRequest();
    signUpRequest.setUsername(username);
    signUpRequest.setPassword(FAKER.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH));
    signUpRequest.setEmail(email);

    return signUpRequest;
  }

  /**
   * Extracts the confirmation button link from the given HTML string.
   *
   * @param htmlString The HTML string to parse.
   * @return The confirmation button link or null if not found.
   */
  public static String extractConfirmAccountLink(String htmlString) {
    // Parse the HTML string using Jsoup
    Document document = Jsoup.parse(htmlString);

    // Select the confirm button link using CSS selector
    Element confirmButton =
        document.select("a[href^='http://localhost/sign-up/verify?token=']").first();

    if (Objects.nonNull(confirmButton)) {
      // Retrieve the href attribute from the confirm button link
      return confirmButton.attr("href");
    } else {
      // Handle the case where the confirm button link is not found
      return null;
    }
  }
}
