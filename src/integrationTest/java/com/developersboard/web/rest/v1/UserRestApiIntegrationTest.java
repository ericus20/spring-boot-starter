package com.developersboard.web.rest.v1;

import static org.hamcrest.Matchers.hasSize;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.TestUtils;
import com.developersboard.constant.AdminConstants;
import com.developersboard.constant.SecurityConstants;
import com.developersboard.constant.user.ProfileConstants;
import com.developersboard.constant.user.UserConstants;
import com.developersboard.enums.OperationStatus;
import com.developersboard.enums.TokenType;
import com.developersboard.shared.util.SignUpUtils;
import com.developersboard.shared.util.UserUtils;
import com.developersboard.web.payload.request.LoginRequest;
import com.developersboard.web.payload.response.JwtResponseBuilder;
import com.icegreen.greenmail.util.GreenMailUtil;
import jakarta.servlet.http.Cookie;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@TestInstance(Lifecycle.PER_CLASS)
class UserRestApiIntegrationTest extends IntegrationTestUtils {

  private transient String loginUri;
  private transient String loginRequestJson;

  @BeforeEach
  void setUp() {
    greenMail.start();
  }

  @AfterEach
  void tearDown() {
    greenMail.stop();
  }

  @BeforeAll
  void beforeAll() {
    var adminDto = UserUtils.createUserDto(true);
    createAndAssertAdmin(adminDto);

    var loginRequest = new LoginRequest(adminDto.getUsername(), adminDto.getPassword());
    loginRequestJson = TestUtils.toJson(loginRequest);

    var delimiter = "/";
    loginUri =
        String.join(delimiter, SecurityConstants.API_V1_AUTH_ROOT_URL, SecurityConstants.LOGIN);
  }

  @Test
  void getUsersWithoutAuthorization() throws Exception {
    performRequest(MockMvcRequestBuilders.get(AdminConstants.API_V1_USERS_ROOT_URL))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andReturn();
  }

  @Test
  void getUsersWithAuthorization() throws Exception {
    createAndAssertUser(UserUtils.createUserDto(true));

    // Authenticate to retrieve access token
    var jwtResponse = getJwtResponse();
    Assertions.assertNotNull(jwtResponse);

    var accessToken = jwtResponse.getAccessToken();
    var bearerToken = getBearerToken(accessToken);

    performRequest(
            MockMvcRequestBuilders.get(AdminConstants.API_V1_USERS_ROOT_URL)
                .cookie(new Cookie(TokenType.ACCESS.getName(), accessToken))
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .param("size", "1"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
        .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(hasSize(1)));
  }

  @Test
  void enableUserWithoutAuthorization() throws Exception {
    var publicId = UUID.randomUUID().toString();
    var enableUrl = String.format("%s/%s/enable", AdminConstants.API_V1_USERS_ROOT_URL, publicId);
    performRequest(MockMvcRequestBuilders.put(enableUrl))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  /** Enabling user with authorization should return 200. */
  @Test
  void enableUserWithAuthorization() throws Exception {
    // Create a new user and assert that the response is successful (201)
    String username = FAKER.internet().username();
    String email = FAKER.internet().emailAddress();
    MvcResult mvcResult = createAndAssertUser(username, email);

    // Assert that user is not enabled
    Assertions.assertFalse(userService.existsByUsernameOrEmailAndEnabled(username, email));

    // Extract the publicId of the user
    String location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
    String publicId = StringUtils.substringAfterLast(location, File.separator);
    Assertions.assertNotNull(publicId);

    // Authenticate to retrieve access token
    var jwtResponse = getJwtResponse();
    Assertions.assertNotNull(jwtResponse);

    var accessToken = jwtResponse.getAccessToken();
    var bearerToken = getBearerToken(accessToken);
    var enableUrl = String.format("%s/%s/enable", AdminConstants.API_V1_USERS_ROOT_URL, publicId);

    performRequest(
            MockMvcRequestBuilders.put(enableUrl)
                .cookie(new Cookie(TokenType.ACCESS.getName(), accessToken))
                .header(HttpHeaders.AUTHORIZATION, bearerToken))
        .andExpect(MockMvcResultMatchers.status().isOk());

    // Assert that user is enabled
    Assertions.assertTrue(userService.existsByUsernameOrEmailAndEnabled(username, email));
  }

  @Test
  void enableUserWithAuthorizationAndInvalidPublicId() throws Exception {
    // Authenticate to retrieve access token
    var jwtResponse = getJwtResponse();
    Assertions.assertNotNull(jwtResponse);

    var accessToken = jwtResponse.getAccessToken();
    var bearerToken = getBearerToken(accessToken);
    var enableUrl =
        String.format("%s/%s/enable", AdminConstants.API_V1_USERS_ROOT_URL, UUID.randomUUID());

    MvcResult result =
        performRequest(
                MockMvcRequestBuilders.put(enableUrl)
                    .header(HttpHeaders.AUTHORIZATION, bearerToken))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    Assertions.assertTrue(
        result.getResponse().getContentAsString().contains(OperationStatus.FAILURE.name()));
  }

  /** Disabling user with authorization should return 200. */
  @Test
  void disableUserWithAuthorization() throws Exception {
    String username = FAKER.internet().username();
    String email = FAKER.internet().emailAddress();
    MvcResult mvcResult = createAndConfirmUserAccount(username, email);

    // Assert that user is enabled
    Assertions.assertTrue(userService.existsByUsernameOrEmailAndEnabled(username, email));

    // Extract the publicId of the user
    String location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
    String publicId = StringUtils.substringAfterLast(location, File.separator);
    Assertions.assertNotNull(publicId);

    // Authenticate to retrieve access token
    var jwtResponse = getJwtResponse();

    var disableUrl = String.format("%s/%s/disable", AdminConstants.API_V1_USERS_ROOT_URL, publicId);

    var accessToken = jwtResponse.getAccessToken();
    var bearerToken = String.format("%s %s", SecurityConstants.BEARER, accessToken);

    performRequest(
            MockMvcRequestBuilders.put(disableUrl).header(HttpHeaders.AUTHORIZATION, bearerToken))
        .andExpect(MockMvcResultMatchers.status().isOk());

    // Assert that user is not enabled
    Assertions.assertFalse(userService.existsByUsernameOrEmailAndEnabled(username, email));
  }

  @Test
  void disableUserWithAuthorizationAndInvalidPublicId() throws Exception {
    // Authenticate to retrieve access token
    var jwtResponse = getJwtResponse();
    Assertions.assertNotNull(jwtResponse);

    var accessToken = jwtResponse.getAccessToken();
    var disableUser =
        String.format("%s/%s/disable", AdminConstants.API_V1_USERS_ROOT_URL, UUID.randomUUID());

    MvcResult result =
        performRequest(
                MockMvcRequestBuilders.put(disableUser)
                    .cookie(new Cookie(TokenType.ACCESS.getName(), accessToken)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    Assertions.assertTrue(
        result.getResponse().getContentAsString().contains(OperationStatus.FAILURE.name()));
  }

  /** Enabling user without authorization should fail. Should return 401 Unauthorized. */
  @Test
  void enableUserNoAuthorization() throws Exception {
    // Endpoint: PUT /api/v1/users/{publicId}/enable

    var enableUrl =
        String.format("%s/%s/enable", AdminConstants.API_V1_USERS_ROOT_URL, UUID.randomUUID());
    mockMvc
        .perform(
            MockMvcRequestBuilders.put(enableUrl).with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  /** Disabling a user without authorization should fail. Should return 401 Unauthorized. */
  @Test
  void disableUserNoAuthorization() throws Exception {
    // Endpoint: PUT /api/v1/users/{publicId}/enable

    var enableUrl =
        String.format("%s/%s/disable", AdminConstants.API_V1_USERS_ROOT_URL, UUID.randomUUID());
    mockMvc
        .perform(
            MockMvcRequestBuilders.put(enableUrl).with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  /** Delete a user */
  @Test
  void deleteUser(TestInfo testInfo) throws Exception {
    // Endpoint: DELETE /api/v1/users/{publicId}
    var userDto = createAndAssertUser(testInfo.getDisplayName(), true);
    Assertions.assertTrue(userService.existsByUsername(userDto.getUsername()));

    // Authenticate to retrieve access token
    var jwtResponse = getJwtResponse();
    Assertions.assertNotNull(jwtResponse);

    var accessToken = jwtResponse.getAccessToken();
    var bearerToken = getBearerToken(accessToken);
    var enableUrl =
        String.format("%s/%s", AdminConstants.API_V1_USERS_ROOT_URL, userDto.getPublicId());

    var result =
        performRequest(
                MockMvcRequestBuilders.delete(enableUrl)
                    .header(HttpHeaders.AUTHORIZATION, bearerToken))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    Assertions.assertTrue(
        result.getResponse().getContentAsString().contains(OperationStatus.SUCCESS.name()));

    Assertions.assertFalse(userService.existsByUsername(userDto.getUsername()));
  }

  /** Deleting a user without authorization should fail. Should return 401 Unauthorized. */
  @Test
  void deleteUserNoAuthorization() throws Exception {
    // Endpoint: PUT /api/v1/users

    var enableUrl = String.format("%s/%s", AdminConstants.API_V1_USERS_ROOT_URL, UUID.randomUUID());
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(enableUrl)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  /** Creating a user without SignUpRequest should fail. Should return 400 BadRequest. */
  @Test
  void createUserWithoutSignUpRequest() throws Exception {
    // Endpoint: POST /api/v1/users

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(AdminConstants.API_V1_USERS_ROOT_URL)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  /** Creating a user. */
  @Test
  void createUserThenVerifyAccountSuccessfully() throws Exception {
    // Endpoint: POST /api/v1/users/{publicId}/enable

    createAndConfirmUserAccount();
  }

  /** Creating a user. */
  @Test
  void createUserThrowsExceptionWithExistingUser() throws Exception {
    // Endpoint: POST /api/v1/users/{publicId}/enable

    String username = FAKER.internet().username();
    createAndConfirmUserAccount(username);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(AdminConstants.API_V1_USERS_ROOT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(SignUpUtils.createSignUpRequest(username)))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(UserConstants.USERNAME_OR_EMAIL_EXITS));
  }

  private void createAndConfirmUserAccount() throws Exception {
    createAndConfirmUserAccount(FAKER.internet().username(), FAKER.internet().emailAddress());
  }

  private void createAndConfirmUserAccount(String username) throws Exception {
    createAndConfirmUserAccount(username, FAKER.internet().emailAddress());
  }

  private MvcResult createAndConfirmUserAccount(String username, String email) throws Exception {
    // Verify no email is sent before sign-up
    var messagesBeforeSignUp = greenMail.getReceivedMessages();
    Assertions.assertEquals(0, messagesBeforeSignUp.length);

    // Create a new user and assert that the response is successful (201)
    MvcResult mvcResult = createAndAssertUser(username, email);

    // Retrieve using GreenMail API
    var messagesAfterSignUp = greenMail.getReceivedMessages();
    Assertions.assertFalse(GreenMailUtil.getBody(messagesAfterSignUp[0]).isEmpty());

    var message = messagesAfterSignUp[0];
    Assertions.assertFalse(GreenMailUtil.getBody(message).isEmpty());

    String htmlString = new String(message.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    String confirmAccountLink = SignUpUtils.extractConfirmAccountLink(htmlString);

    Assertions.assertNotNull(confirmAccountLink);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(confirmAccountLink)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.view().name(ProfileConstants.REDIRECT_TO_PROFILE))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

    return mvcResult;
  }

  /**
   * Authenticate and return jwt response.
   *
   * @return jwt response
   */
  private JwtResponseBuilder getJwtResponse() throws Exception {
    var mvcResult = performRequest(MockMvcRequestBuilders.post(loginUri)).andReturn();
    var contentAsString = mvcResult.getResponse().getContentAsString();

    return TestUtils.parse(contentAsString, JwtResponseBuilder.class);
  }

  /**
   * Constructs a bearer token from the given access token.
   *
   * @param accessToken access token
   * @return bearer token
   */
  private String getBearerToken(String accessToken) {
    return String.format("%s %s", SecurityConstants.BEARER, accessToken);
  }

  /**
   * Performs mockMvc request with the specified request details and content.
   *
   * @param request the request
   * @return the result actions
   */
  private ResultActions performRequest(MockHttpServletRequestBuilder request) throws Exception {
    return mockMvc.perform(
        request
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, HttpMethod.POST)
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginRequestJson));
  }
}
