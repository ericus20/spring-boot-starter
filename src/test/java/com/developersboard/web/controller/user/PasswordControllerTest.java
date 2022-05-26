package com.developersboard.web.controller.user;

import com.developersboard.constant.user.PasswordConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class PasswordControllerTest {

  private transient MockMvc mockMvc;

  @InjectMocks private transient PasswordController passwordController;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(passwordController).build();
  }

  @Test
  void testResetStartPathAndViewName() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(PasswordConstants.PASSWORD_RESET_ROOT_MAPPING))
        .andExpect(
            MockMvcResultMatchers.view().name(PasswordConstants.PASSWORD_RESET_START_VIEW_NAME))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void testStartPasswordResetWithoutEmailReturns400() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post(PasswordConstants.PASSWORD_RESET_ROOT_MAPPING))
        .andExpect(MockMvcResultMatchers.status().is4xxClientError());
  }
}
