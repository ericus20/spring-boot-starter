package com.developersboard.web.rest.v1;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.constant.AdminConstants;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
class UserRestApiIntegrationTest extends IntegrationTestUtils {

  @Autowired private transient MockMvc mockMvc;

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

  /** Enabling a user without publicId should fail. Should return 40 Unauthorized. */
  @Test
  void enableUserNoPublicId() throws Exception {
    // Endpoint: PUT /api/v1/users/{publicId}/enable

    var enableUrl =
        String.format("%s/%s/enable", AdminConstants.API_V1_USERS_ROOT_URL, StringUtils.EMPTY);
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
}
