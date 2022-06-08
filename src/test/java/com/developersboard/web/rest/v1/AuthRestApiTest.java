package com.developersboard.web.rest.v1;

import com.developersboard.TestUtils;
import com.developersboard.backend.service.security.CookieService;
import com.developersboard.constant.SecurityConstants;
import com.developersboard.web.payload.request.LoginRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AuthRestApiTest {

  @Mock private CookieService cookieService;

  @InjectMocks private AuthRestApi authRestApi;

  private MockMvc mockMvc;
  private String loginUri;
  private String refreshUri;
  private String logoutUri;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(authRestApi).build();
    loginUri = String.join("/", SecurityConstants.API_V1_AUTH_ROOT_URL, SecurityConstants.LOGIN);
    refreshUri =
        String.join("/", SecurityConstants.API_V1_AUTH_ROOT_URL, SecurityConstants.REFRESH_TOKEN);
    logoutUri = String.join("/", SecurityConstants.API_V1_AUTH_ROOT_URL, SecurityConstants.LOGOUT);
  }

  @Test
  void testLoginPathWithoutRequestBodyReturnsBadRequest() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post(loginUri))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void testLoginPathWithoutUsernameReturnsBadRequest(TestInfo testInfo) throws Exception {
    var loginRequest =
        TestUtils.toJson(new LoginRequest(StringUtils.EMPTY, testInfo.getDisplayName()));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(loginUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void testLoginPathWithoutPasswordReturnsBadRequest(TestInfo testInfo) throws Exception {
    var loginRequest =
        TestUtils.toJson(new LoginRequest(testInfo.getDisplayName(), StringUtils.EMPTY));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(loginUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void testRefreshPathWithoutCookieReturnsBadRequest() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(refreshUri))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void testLogoutReturnsOKWithSuccessResponse() throws Exception {
    Assertions.assertNotNull(cookieService);

    mockMvc
        .perform(MockMvcRequestBuilders.delete(logoutUri))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
