package com.developersboard.web.rest.v1;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.TestUtils;
import com.developersboard.constant.AdminConstants;
import com.developersboard.constant.SecurityConstants;
import com.developersboard.enums.OperationStatus;
import com.developersboard.enums.TokenType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import com.developersboard.web.payload.request.LoginRequest;
import com.developersboard.web.payload.response.JwtResponseBuilder;
import java.util.UUID;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@TestInstance(Lifecycle.PER_CLASS)
class UserRestApiIntegrationTest extends IntegrationTestUtils {

  private transient String loginUri;
  private transient String loginRequestJson;

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

  /** Enabling user with authorization should return 200. */
  @Test
  void enableUserWithAuthorization() throws Exception {
    var userDto = createAndAssertUser(UserUtils.createUserDto(false));
    Assertions.assertFalse(userDto.isEnabled());

    // Authenticate to retrieve access token
    var jwtResponse = getJwtResponse();
    Assertions.assertNotNull(jwtResponse);

    var accessToken = jwtResponse.getAccessToken();
    var bearerToken = getBearerToken(accessToken);
    var enableUrl =
        String.format("%s/%s/enable", AdminConstants.API_V1_USERS_ROOT_URL, userDto.getPublicId());

    performRequest(
            MockMvcRequestBuilders.put(enableUrl)
                .cookie(new Cookie(TokenType.ACCESS.getName(), accessToken))
                .header(HttpHeaders.AUTHORIZATION, bearerToken))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    // Assert that user is enabled
    Assertions.assertTrue(
        userService.existsByUsernameOrEmailAndEnabled(userDto.getUsername(), userDto.getEmail()));
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
    UserDto userDto = createAndAssertUser(UserUtils.createUserDto(true));
    Assertions.assertTrue(userDto.isEnabled());

    // Authenticate to retrieve access token
    var jwtResponse = getJwtResponse();

    var disableUrl =
        String.format("%s/%s/disable", AdminConstants.API_V1_USERS_ROOT_URL, userDto.getPublicId());

    var accessToken = jwtResponse.getAccessToken();
    var bearerToken = String.format("%s %s", SecurityConstants.BEARER, accessToken);

    performRequest(
            MockMvcRequestBuilders.put(disableUrl).header(HttpHeaders.AUTHORIZATION, bearerToken))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    // Assert that user is enabled
    Assertions.assertFalse(
        userService.existsByUsernameOrEmailAndEnabled(userDto.getUsername(), userDto.getEmail()));
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
    // Endpoint: PUT /api/v1/users/{publicId}/enable

    var enableUrl = String.format("%s/%s", AdminConstants.API_V1_USERS_ROOT_URL, UUID.randomUUID());
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(enableUrl)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
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
