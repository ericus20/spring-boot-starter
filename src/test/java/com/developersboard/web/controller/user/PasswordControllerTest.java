package com.developersboard.web.controller.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.developersboard.backend.service.mail.impl.MockEmailServiceImpl;
import com.developersboard.backend.service.security.EncryptionService;
import com.developersboard.backend.service.security.JwtService;
import com.developersboard.backend.service.security.impl.EncryptionServiceImpl;
import com.developersboard.backend.service.security.impl.JwtServiceImpl;
import com.developersboard.backend.service.user.impl.UserServiceImpl;
import com.developersboard.constant.user.PasswordConstants;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import com.developersboard.shared.util.core.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class PasswordControllerTest {

  @Mock private Model model;

  private MockMvc mockMvc;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private UserServiceImpl userService;

  @Mock private MockEmailServiceImpl emailService;

  private PasswordController passwordController;

  @BeforeEach
  void setUp() {

    EncryptionService encryptionService = new EncryptionServiceImpl("salt", "password");
    JwtService jwtService = new JwtServiceImpl(JwtUtils.generateSecretKey());

    passwordController =
        new PasswordController(
            jwtService, userService, emailService, passwordEncoder, encryptionService);

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

  @Test
  void shouldRedirectToPasswordResetStartViewWhenEmailExists() {
    // Given
    UserDto userDto = UserUtils.createUserDto(false);
    doReturn(userDto).when(userService).findByEmail(userDto.getEmail());
    doNothing().when(emailService).sendPasswordResetEmail(any(UserDto.class), anyString());

    // When
    String viewName = passwordController.forgetPassword(model, userDto.getEmail());

    // Then
    verify(emailService).sendPasswordResetEmail(any(UserDto.class), anyString());
    assertEquals(PasswordConstants.PASSWORD_RESET_START_VIEW_NAME, viewName);
  }

  @Test
  void shouldStillRedirectToPasswordResetStartViewWhenEmailDoesNotExist() {
    // Given
    String email = "nonexistent@example.com";
    when(userService.findByEmail(anyString())).thenReturn(null);

    // When
    String viewName = passwordController.forgetPassword(model, email);

    // Then
    verify(emailService, never()).sendPasswordResetEmail(any(UserDto.class), anyString());
    assertEquals(PasswordConstants.PASSWORD_RESET_START_VIEW_NAME, viewName);
  }
}
