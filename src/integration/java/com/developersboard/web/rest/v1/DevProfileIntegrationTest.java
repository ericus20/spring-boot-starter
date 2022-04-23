package com.developersboard.web.rest.v1;

import com.developersboard.constant.ProfileTypeConstants;
import com.developersboard.constant.SecurityConstants;
import com.developersboard.enums.TokenType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(ProfileTypeConstants.DEV)
public class DevProfileIntegrationTest {

  @Autowired private transient MockMvc mockMvc;

  /**
   * The purpose of this test is to verify that request can be processed without csrf token in dev
   * as the csrf token is disabled in dev profile.
   *
   * @throws Exception if any error occurs
   */
  @Test
  void logoutRequestWithoutCsrfToken() throws Exception {

    var logoutUri =
        String.join("/", SecurityConstants.API_V1_AUTH_ROOT_URL, SecurityConstants.LOGOUT);

    // logout
    mockMvc
        .perform(MockMvcRequestBuilders.delete(logoutUri).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.cookie().exists(TokenType.REFRESH.getName()))
        .andExpect(
            MockMvcResultMatchers.cookie().value(TokenType.REFRESH.getName(), StringUtils.EMPTY))
        .andReturn();
  }
}
