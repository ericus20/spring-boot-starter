package com.developersboard.web.controller.user;

import com.developersboard.annotation.Loggable;
import com.developersboard.backend.service.mail.EmailService;
import com.developersboard.backend.service.security.JwtService;
import com.developersboard.backend.service.user.UserService;
import com.developersboard.constant.ErrorConstants;
import com.developersboard.constant.HomeConstants;
import com.developersboard.constant.user.PasswordConstants;
import com.developersboard.constant.user.UserConstants;
import com.developersboard.enums.UserHistoryType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.SecurityUtils;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Password controller is responsible for handling all password resets.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(PasswordConstants.FORGOT_ROOT_MAPPING)
public class PasswordController {

  private final JwtService jwtService;
  private final UserService userService;
  private final EmailService emailService;
  private final PasswordEncoder passwordEncoder;

  /**
   * Processes the forgot password email submitted and generates a link to resets password. Link is
   * then emailed to user.
   *
   * @param model the model
   * @param email the email of user
   * @return the view name of email form.
   */
  @Loggable
  @PostMapping
  public String forgetPassword(final Model model, @RequestParam final String email) {
    UserDto userDto = userService.findByEmail(email);
    if (Objects.nonNull(userDto)) {
      model.addAttribute(UserConstants.EMAIL, userDto.getEmail());
      // Sends an email to the user to verify email to complete sign-up process.
      String token = jwtService.generateJwtToken(userDto.getPublicId());
      userService.saveVerificationToken(userDto, token);
      emailService.sendPasswordResetEmail(userDto, token);
      // if there wasn't any error processing password reset, set email sent key as true.
      if (!model.containsAttribute(PasswordConstants.EMAIL_ERROR_KEY)) {
        model.addAttribute(PasswordConstants.EMAIL_SENT_KEY, true);
      }
    } else {
      LOG.debug(UserConstants.COULD_NOT_FIND_USER, email);
    }
    return PasswordConstants.EMAIL_ADDRESS_VIEW_NAME;
  }

  /**
   * Processes the generated link with token to reset user's password.
   *
   * @param token the token to validate against user
   * @param model the model
   * @return the view name for password reset
   */
  @Loggable
  @GetMapping(PasswordConstants.PASSWORD_CHANGE_PATH)
  public String changePassword(@RequestParam final String token, final Model model) {

    if (SecurityUtils.isAuthenticated()) {
      model.addAttribute(ErrorConstants.ERROR_MESSAGE, PasswordConstants.ACCOUNT_IN_SESSION);
    } else if (!jwtService.isValidJwtToken(token)) {
      model.addAttribute(ErrorConstants.ERROR_MESSAGE, ErrorConstants.INVALID_TOKEN);
    } else {
      String publicUserId = jwtService.getUsernameFromToken(token);
      if (!userService.isVerificationTokenValid(publicUserId, token)) {
        LOG.debug(PasswordConstants.UNAUTHORIZED_ACCESS);
        model.addAttribute(ErrorConstants.ERROR_MESSAGE, PasswordConstants.UNAUTHORIZED_ACCESS);
      }

      var userDto = new UserDto();
      userDto.setPublicId(publicUserId);
      model.addAttribute(UserConstants.USER_MODEL_KEY, userDto);
    }

    if (model.containsAttribute(ErrorConstants.ERROR_MESSAGE)) {
      return PasswordConstants.EMAIL_ADDRESS_VIEW_NAME;
    }

    return PasswordConstants.CHANGE_VIEW_NAME;
  }

  /**
   * Processes the post request after the new password has been submitted to change the user's
   * password.
   *
   * @param userDto the parameters with user's password details
   * @param model the model.
   * @return the change password view name
   */
  @Loggable
  @PostMapping(PasswordConstants.PASSWORD_CHANGE_PATH)
  public String changeUserPassword(@ModelAttribute final UserDto userDto, final Model model) {
    if (Objects.nonNull(userDto)) {
      // check if user's new password is the same as the current one
      if (passwordEncoder.matches(userDto.getPassword(), userDto.getPassword())) {
        model.addAttribute(UserConstants.USER_MODEL_KEY, new UserDto());
        model.addAttribute(ErrorConstants.ERROR_MESSAGE, PasswordConstants.SAME_PASSWORD);
        return PasswordConstants.CHANGE_VIEW_NAME;
      }
      // Update the user password with the encoded one.
      userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
      UserDto updatedUserDto = userService.updateUser(userDto, UserHistoryType.PASSWORD_UPDATE);
      if (Objects.nonNull(updatedUserDto)) {
        // After a successful password reset, send a confirmation email to the user.
        emailService.sendPasswordResetConfirmationEmail(userDto);
      }
      model.addAttribute(PasswordConstants.RESET_ATTRIBUTE_NAME, true);
    } else {
      LOG.debug(UserConstants.USER_DTO_MUST_NOT_BE_NULL);
    }

    return HomeConstants.INDEX_VIEW_NAME;
  }
}
