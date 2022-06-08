package com.developersboard.web.controller;

import com.developersboard.annotation.Loggable;
import com.developersboard.backend.service.mail.EmailService;
import com.developersboard.config.properties.SystemProperties;
import com.developersboard.constant.ContactConstants;
import com.developersboard.constant.user.UserConstants;
import com.developersboard.shared.util.core.SecurityUtils;
import com.developersboard.web.payload.request.mail.FeedbackRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The controller for handling all security related mappings.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(path = ContactConstants.CONTACT_URL_MAPPING)
public class ContactController {

  private final EmailService emailService;
  private final SystemProperties systemProperties;

  /**
   * Prepares and return the contactUs form for a new submission.
   *
   * @param model the model
   * @return the contact-us view name
   */
  @GetMapping
  public String contactUs(Model model) {
    var feedback = new FeedbackRequest();
    // if user is logged in, populate the form with their details
    if (SecurityUtils.isAuthenticated()) {
      var authorizedUserDto = SecurityUtils.getAuthorizedUserDto();
      model.addAttribute(UserConstants.USER_MODEL_KEY, authorizedUserDto);
      BeanUtils.copyProperties(authorizedUserDto, feedback);
    }

    model.addAttribute(ContactConstants.FEEDBACK, feedback);
    return ContactConstants.CONTACT_VIEW_NAME;
  }

  /**
   * Receives a populated feedback object then sends an email with it.
   *
   * @param feedback the feedback
   * @param model the model
   * @return the status
   */
  @Loggable
  @PostMapping
  public String contactUs(@ModelAttribute @Valid FeedbackRequest feedback, Model model) {
    feedback.setFrom(feedback.getEmail());
    feedback.setTo(systemProperties.getEmail());
    emailService.sendMailWithFeedback(feedback);

    model.addAttribute(ContactConstants.FEEDBACK_SUCCESS_KEY, true);

    model.addAttribute(ContactConstants.FEEDBACK, new FeedbackRequest());
    return ContactConstants.CONTACT_VIEW_NAME;
  }
}
