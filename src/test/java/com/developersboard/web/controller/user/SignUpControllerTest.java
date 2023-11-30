package com.developersboard.web.controller.user;

import com.developersboard.constant.user.SignUpConstants;
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
public class SignUpControllerTest {

  private transient MockMvc mockMvc;

  @InjectMocks private transient SignUpController signUpController;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(signUpController).build();
  }

  @Test
  void testSignUpPathAndViewName() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(SignUpConstants.SIGN_UP_MAPPING))
        .andExpect(MockMvcResultMatchers.view().name(SignUpConstants.SIGN_UP_VIEW_NAME))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void testSignUpWithoutUserDtoReturns400() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post(SignUpConstants.SIGN_UP_MAPPING))
        .andExpect(MockMvcResultMatchers.status().is4xxClientError());
  }
}
