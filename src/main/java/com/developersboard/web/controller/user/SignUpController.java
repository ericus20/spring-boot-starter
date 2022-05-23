package com.developersboard.web.controller.user;

import com.developersboard.backend.service.mail.EmailService;
import com.developersboard.backend.service.security.EncryptionService;
import com.developersboard.backend.service.security.JwtService;
import com.developersboard.backend.service.user.UserService;
import com.developersboard.constant.SignUpConstants;
import com.developersboard.constant.UserConstants;
import com.developersboard.web.payload.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles all request related to user's sign-up journey.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(SignUpConstants.SIGN_UP_MAPPING)
public class SignUpController {

  private final JwtService jwtService;
  private final UserService userService;
  private final EmailService emailService;
  private final EncryptionService encryptionService;

  /**
   * Returns user to the sign-up form to start registration.
   *
   * @param model the model to transfer objects to view
   * @return the view of the user registration page
   */
  @GetMapping
  public String signUp(final Model model) {
    model.addAttribute(UserConstants.USER_MODEL_KEY, new SignUpRequest());

    return SignUpConstants.SIGN_UP_VIEW_NAME;
  }
}
