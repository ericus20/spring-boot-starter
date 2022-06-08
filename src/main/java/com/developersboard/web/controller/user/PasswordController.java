package com.developersboard.web.controller.user;

import com.developersboard.backend.service.mail.EmailService;
import com.developersboard.backend.service.security.EncryptionService;
import com.developersboard.backend.service.security.JwtService;
import com.developersboard.backend.service.user.UserService;
import com.developersboard.constant.ErrorConstants;
import com.developersboard.constant.HomeConstants;
import com.developersboard.constant.user.PasswordConstants;
import com.developersboard.constant.user.UserConstants;
import com.developersboard.enums.UserHistoryType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import com.developersboard.shared.util.core.SecurityUtils;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
@RequestMapping(PasswordConstants.PASSWORD_RESET_ROOT_MAPPING)
public class PasswordController {

  private final JwtService jwtService;
  private final UserService userService;
  private final EmailService emailService;
  private final PasswordEncoder passwordEncoder;
  private final EncryptionService encryptionService;

  /**
   * Processes the password reset form.
   *
   * @return the view name of password reset form.
   */
  @GetMapping
  public String passwordReset() {
    return PasswordConstants.PASSWORD_RESET_START_VIEW_NAME;
  }

  /**
   * Processes the forgot password email submitted and generates a link to resets password. Link is
   * then emailed to user.
   *
   * @param model the model
   * @param email the email of user
   * @return the view name of password reset form.
   */
  @PostMapping
  public String forgetPassword(Model model, @RequestParam final String email) {
    var userDto = userService.findByEmail(email);
    if (Objects.nonNull(userDto)) {
      model.addAttribute(UserConstants.EMAIL, userDto.getEmail());

      // send email to the user to verify email to complete sign-up process.
      String token = jwtService.generateJwtToken(userDto.getUsername());
      userDto.setVerificationToken(token);
      userService.saveOrUpdate(UserUtils.convertToUser(userDto), false);

      var encryptedToken = encryptionService.encrypt(token);
      var encodedToken = encryptionService.encode(encryptedToken);
      emailService.sendPasswordResetEmail(userDto, encodedToken);
    } else {
      LOG.debug(UserConstants.USER_NOT_FOUND + " email: {}", email);
    }

    model.addAttribute(PasswordConstants.PASSWORD_RESET_EMAIL_SENT_KEY, true);

    return PasswordConstants.PASSWORD_RESET_START_VIEW_NAME;
  }

  /**
   * Processes the generated link with token to reset user's password.
   *
   * @param token the token to validate against user
   * @param model the model
   * @return the view name for password reset
   */
  @GetMapping(PasswordConstants.PASSWORD_CHANGE_PATH)
  public String changePassword(@RequestParam final String token, Model model) {
    if (SecurityUtils.isAuthenticated()) {
      // if the user is already logged in, abort the process.
      LOG.debug(PasswordConstants.ACCOUNT_IN_SESSION);
      model.addAttribute(ErrorConstants.ERROR, PasswordConstants.ACCOUNT_IN_SESSION);
      return PasswordConstants.PASSWORD_RESET_START_VIEW_NAME;
    }

    var decodedToken = encryptionService.decode(token);
    var decryptedToken = encryptionService.decrypt(decodedToken);

    if (StringUtils.isBlank(decryptedToken) || !jwtService.isValidJwtToken(decryptedToken)) {
      LOG.debug(ErrorConstants.INVALID_TOKEN);
      model.addAttribute(ErrorConstants.ERROR, ErrorConstants.INVALID_TOKEN);
      return PasswordConstants.PASSWORD_RESET_START_VIEW_NAME;
    }

    var username = jwtService.getUsernameFromToken(decryptedToken);
    if (StringUtils.isBlank(username)
        || !userService.isValidUsernameAndToken(username, decryptedToken)) {
      LOG.debug(ErrorConstants.UNAUTHORIZED_ACCESS);
      model.addAttribute(ErrorConstants.ERROR, ErrorConstants.UNAUTHORIZED_ACCESS);
      return PasswordConstants.PASSWORD_RESET_START_VIEW_NAME;
    }

    model.addAttribute(UserConstants.USERNAME, username);
    return PasswordConstants.PASSWORD_RESET_COMPLETE_VIEW_NAME;
  }

  /**
   * Processes the post request after the new password has been submitted to change the user's
   * password.
   *
   * @param model the model
   * @param userDto the parameters with user's password details
   * @param redirectAttributes the redirect attribute.
   * @return the change password view name
   */
  @PostMapping(PasswordConstants.PASSWORD_CHANGE_PATH)
  public String changePassword(
      @ModelAttribute UserDto userDto, Model model, RedirectAttributes redirectAttributes) {
    var storedUserDto = userService.findByUsername(userDto.getUsername());
    if (Objects.isNull(storedUserDto)) {
      LOG.debug(UserConstants.USER_NOT_FOUND + " username: {}", userDto.getUsername());
      redirectAttributes.addFlashAttribute(ErrorConstants.ERROR, UserConstants.USER_NOT_FOUND);
      return HomeConstants.REDIRECT_TO_LOGIN;
    }
    // check if user's new password is the same as the current one
    if (passwordEncoder.matches(userDto.getPassword(), storedUserDto.getPassword())) {
      model.addAttribute(ErrorConstants.ERROR, PasswordConstants.SAME_PASSWORD);
      model.addAttribute(UserConstants.USERNAME, userDto.getUsername());
      return PasswordConstants.PASSWORD_RESET_COMPLETE_VIEW_NAME;
    }

    UserUtils.enableUser(storedUserDto);
    storedUserDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
    var updatedUserDto = userService.updateUser(storedUserDto, UserHistoryType.PASSWORD_UPDATE);
    if (Objects.isNull(updatedUserDto)) {
      LOG.debug(PasswordConstants.PASSWORD_UPDATE_ERROR);
      redirectAttributes.addAttribute(
          ErrorConstants.ERROR, PasswordConstants.PASSWORD_UPDATE_ERROR);
      return HomeConstants.REDIRECT_TO_LOGIN;
    }

    emailService.sendPasswordResetConfirmationEmail(storedUserDto);
    redirectAttributes.addFlashAttribute(PasswordConstants.PASSWORD_RESET_SUCCESS, true);

    return HomeConstants.REDIRECT_TO_LOGIN;
  }
}
