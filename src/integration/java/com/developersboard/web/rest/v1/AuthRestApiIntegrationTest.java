package com.developersboard.web.rest.v1;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.TestUtils;
import com.developersboard.backend.service.security.CookieService;
import com.developersboard.backend.service.security.EncryptionService;
import com.developersboard.backend.service.security.JwtService;
import com.developersboard.backend.service.user.UserService;
import com.developersboard.constant.SecurityConstants;
import com.developersboard.enums.TokenType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import com.developersboard.web.payload.request.LoginRequest;
import com.developersboard.web.payload.response.JwtResponseBuilder;
import java.time.Duration;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
class AuthRestApiIntegrationTest extends IntegrationTestUtils {

  @Autowired private transient MockMvc mockMvc;

  @Autowired private transient CookieService cookieService;

  @Autowired private transient UserService userService;

  @Autowired private transient JwtService jwtService;

  @Autowired private transient EncryptionService encryptionService;

  private transient String loginUri;
  private transient String logoutUri;
  private transient String refreshUri;
  private transient String loginRequestJson;
  private transient UserDto storedUser;
  private transient Duration refreshTokenDuration;

  @BeforeEach
  void setUp(TestInfo testInfo) {
    var userDto = UserUtils.createUserDto(testInfo.getDisplayName(), true);
    storedUser = createAndAssertUser(userService, userDto);

    var loginRequest = new LoginRequest(storedUser.getUsername(), userDto.getPassword());
    loginRequestJson = TestUtils.toJson(loginRequest);

    String delimiter = "/";
    loginUri =
        String.join(delimiter, SecurityConstants.API_V1_AUTH_ROOT_URL, SecurityConstants.LOGIN);
    logoutUri =
        String.join(delimiter, SecurityConstants.API_V1_AUTH_ROOT_URL, SecurityConstants.LOGOUT);
    refreshUri =
        String.join(
            delimiter, SecurityConstants.API_V1_AUTH_ROOT_URL, SecurityConstants.REFRESH_TOKEN);
    refreshTokenDuration = Duration.ofDays(SecurityConstants.REFRESH_TOKEN_DURATION);
  }

  @Test
  void loginPathPreflightReturnsOk() throws Exception {
    performRequest(MockMvcRequestBuilders.options(loginUri))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void refreshPathPreflightReturnsOk() throws Exception {
    performRequest(MockMvcRequestBuilders.options(refreshUri))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void logoutPathPreflightReturnsOk() throws Exception {
    performRequest(MockMvcRequestBuilders.options(logoutUri))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void loginPathWithValidCredentialsReturnsAccessToken() throws Exception {
    MvcResult mvcResult =
        performRequest(MockMvcRequestBuilders.post(loginUri))
            .andExpectAll(expectedResponseDetails(storedUser))
            .andReturn();

    String contentAsString = mvcResult.getResponse().getContentAsString();
    JwtResponseBuilder jwtResponse = TestUtils.parse(contentAsString, JwtResponseBuilder.class);

    String encryptedAccessToken = jwtResponse.getAccessToken();
    Assertions.assertFalse(jwtService.isValidJwtToken(encryptedAccessToken));

    String originalAccessToken = encryptionService.decrypt(encryptedAccessToken);
    Assertions.assertTrue(jwtService.isValidJwtToken(originalAccessToken));
  }

  /**
   * Refreshing with a valid refresh token returns a new access token.
   *
   * @throws Exception if an error occurs
   */
  @Test
  void validRefreshTokenReturnsNewAccessToken() throws Exception {

    var jwtToken = jwtService.generateJwtToken(storedUser.getUsername());
    var cookie = cookieService.createTokenCookie(jwtToken, TokenType.REFRESH);

    MvcResult mvcResult =
        performRequest(
                MockMvcRequestBuilders.get(refreshUri).cookie(cookieService.createCookie(cookie)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String contentAsString = mvcResult.getResponse().getContentAsString();
    JwtResponseBuilder jwtResponse = TestUtils.parse(contentAsString, JwtResponseBuilder.class);

    String encryptedAccessToken = jwtResponse.getAccessToken();
    // Assert that the access token returned by the refresh token is valid
    Assertions.assertFalse(jwtService.isValidJwtToken(encryptedAccessToken));

    String originalAccessToken = encryptionService.decrypt(encryptedAccessToken);

    // Assert that the access token returned by the refresh token is different from the original
    Assertions.assertNotEquals(jwtToken, originalAccessToken);
  }

  @Test
  void loginLogoutReturnsResponseWithoutRefreshToken() throws Exception {
    // login
    performRequest(MockMvcRequestBuilders.post(loginUri))
        .andExpectAll(expectedResponseDetails(storedUser))
        .andReturn();

    // logout
    performRequest(MockMvcRequestBuilders.delete(logoutUri))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.cookie().exists(TokenType.REFRESH.getName()))
        .andExpect(
            MockMvcResultMatchers.cookie().value(TokenType.REFRESH.getName(), StringUtils.EMPTY))
        .andReturn();
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
            .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
            .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, HttpMethod.POST)
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginRequestJson));
  }

  /**
   * Returns the expected response details.
   *
   * @param userDto the userDto
   * @return the result matchers
   */
  private ResultMatcher[] expectedResponseDetails(UserDto userDto) {
    long value = refreshTokenDuration.toSeconds();
    return new ResultMatcher[] {
      MockMvcResultMatchers.status().isOk(),
      MockMvcResultMatchers.cookie().exists(TokenType.REFRESH.getName()),
      MockMvcResultMatchers.cookie().httpOnly(TokenType.REFRESH.getName(), true),
      MockMvcResultMatchers.cookie().maxAge(TokenType.REFRESH.getName(), Math.toIntExact(value)),
      MockMvcResultMatchers.jsonPath("$").isMap(),
      MockMvcResultMatchers.jsonPath("$.username").value(userDto.getUsername()),
      MockMvcResultMatchers.jsonPath("$.publicId").value(userDto.getPublicId()),
      MockMvcResultMatchers.jsonPath("$.email").value(userDto.getEmail()),
      MockMvcResultMatchers.jsonPath("$.roles").value(UserUtils.getRoles(userDto.getUserRoles())),
      MockMvcResultMatchers.jsonPath("$.accessToken").exists()
    };
  }
}
