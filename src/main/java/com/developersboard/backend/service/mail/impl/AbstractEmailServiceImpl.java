package com.developersboard.backend.service.mail.impl;

import com.developersboard.backend.service.mail.EmailService;
import com.developersboard.constant.EmailConstants;
import com.developersboard.constant.UserConstants;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.core.WebUtils;
import com.developersboard.web.payload.request.mail.FeedbackRequest;
import com.developersboard.web.payload.request.mail.HtmlEmailRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import javax.mail.internet.InternetAddress;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.mail.SimpleMailMessage;
import org.thymeleaf.context.Context;

/**
 * This abstract class leverages the required methods by the EmailService.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public abstract class AbstractEmailServiceImpl implements EmailService {

  /**
   * Sends an email given a feedback Pojo.
   *
   * @param feedbackRequestBuilder the feedback pojo.
   * @see FeedbackRequest
   */
  @Override
  public void sendMailWithFeedback(final FeedbackRequest feedbackRequestBuilder) {
    sendMail(prepareSimpleMailMessage(feedbackRequestBuilder));
  }

  /**
   * Sends an email to the provided user to verify account.
   *
   * @param userDto the userDto
   * @param token the token
   */
  @Override
  public void sendAccountVerificationEmail(UserDto userDto, String token) {
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);

    var emailRequest =
        prepareHtmlEmailRequest(
            userDto,
            token,
            "/sign-up-path",
            EmailConstants.EMAIL_VERIFY_TEMPLATE,
            EmailConstants.CONFIRMATION_PENDING_EMAIL_SUBJECT);
    // prepare the email request then send it.
    sendHtmlEmail(prepareEmailRequest(emailRequest));
  }

  /**
   * Sends an email to the provided user to confirm account activation.
   *
   * @param userDto the userDto
   */
  @Override
  public void sendAccountConfirmationEmail(UserDto userDto) {
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);

    HtmlEmailRequest emailRequest =
        prepareHtmlEmailRequest(
            userDto,
            null,
            "/profile-path",
            EmailConstants.EMAIL_WELCOME_TEMPLATE,
            EmailConstants.CONFIRMATION_SUCCESS_EMAIL_SUBJECT);
    // prepare the email request then send it.
    sendHtmlEmail(prepareEmailRequest(emailRequest));
  }

  /**
   * Sends an email to the provided user to reset password.
   *
   * @param userDto the userDto
   * @param token the password token
   */
  @Override
  public void sendPasswordResetEmail(UserDto userDto, String token) {
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);

    HtmlEmailRequest emailRequest =
        prepareHtmlEmailRequest(
            userDto,
            token,
            "/reset-password-path",
            EmailConstants.PASSWORD_RESET_TEMPLATE,
            EmailConstants.PASSWORD_RESET_EMAIL_SUBJECT);
    // prepare the email request then send it.
    sendHtmlEmail(prepareEmailRequest(emailRequest));
  }

  /**
   * Send password reset confirmation email to user.
   *
   * @param userDto the user dto
   */
  @Override
  public void sendPasswordResetConfirmationEmail(UserDto userDto) {
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);

    HtmlEmailRequest emailRequest =
        prepareHtmlEmailRequest(
            userDto,
            null,
            null,
            EmailConstants.PASSWORD_UPDATE_TEMPLATE,
            EmailConstants.PASSWORD_RESET_SUCCESS_SUBJECT);
    // prepare the email request then send it.
    sendHtmlEmail(prepareEmailRequest(emailRequest));
  }

  /**
   * Prepares the html request object with the appropriate details given.
   *
   * @param userDto the userDto
   * @param token the token
   * @param path the path
   * @param template the template
   * @param subject the subject
   * @return the prepared htmlEmailRequest
   */
  private HtmlEmailRequest prepareHtmlEmailRequest(
      UserDto userDto, String token, String path, String template, String subject) {

    String link = null;
    if (Objects.nonNull(token)) {
      link = WebUtils.getGenericUri(path, token);
    } else if (Objects.nonNull(path)) {
      link = WebUtils.getGenericUri(path);
    }
    // get the links used in the email
    Map<String, String> links = WebUtils.getDefaultEmailUrls();
    if (Objects.nonNull(link)) {
      links.put(EmailConstants.EMAIL_LINK, link);
    }

    HtmlEmailRequest emailRequest = new HtmlEmailRequest();
    emailRequest.setTemplate(template);
    emailRequest.setUrls(links);
    emailRequest.setReceiver(userDto);
    emailRequest.setSubject(subject);
    return emailRequest;
  }

  /**
   * Prepares a context with current content.
   *
   * @param emailRequest the emailRequest
   * @return emailRequest with context
   */
  public static HtmlEmailRequest prepareEmailRequest(final HtmlEmailRequest emailRequest) {
    Context context = new Context();
    context.setVariable(EmailConstants.URLS, emailRequest.getUrls());
    // set the appropriate to and from based on the details available
    configureSenderAndReceiverDetails(emailRequest, context);
    emailRequest.setSubject(emailRequest.getSubject());
    if (Objects.nonNull(emailRequest.getUrls().get(EmailConstants.EMAIL_LINK))) {
      context.setVariable(
          EmailConstants.EMAIL_LINK, emailRequest.getUrls().get(EmailConstants.EMAIL_LINK));
      LOG.info(emailRequest.getUrls().get(EmailConstants.EMAIL_LINK));
    }
    if (Objects.nonNull(emailRequest.getMessage())) {
      context.setVariable(EmailConstants.MESSAGE, emailRequest.getMessage());
    }
    emailRequest.setContext(context);
    return emailRequest;
  }

  /**
   * set the appropriate to and from based on the details available.
   *
   * @param emailRequest the emailRequest
   * @param context the context
   */
  private static void configureSenderAndReceiverDetails(
      HtmlEmailRequest emailRequest, Context context) {
    if (Objects.nonNull(emailRequest.getReceiver())) {
      context.setVariable(UserConstants.USERNAME, emailRequest.getReceiver().getUsername());
      emailRequest.setTo(emailRequest.getReceiver().getEmail());
    }
    if (Objects.nonNull(emailRequest.getSender())) {
      emailRequest.setFrom(emailRequest.getSender().getEmail());
    }
  }

  /**
   * Prepares a simple mail message object with the feedback pojo details.
   *
   * @param feedbackRequest the feedback pojo.
   * @return the simple mail message.
   */
  private SimpleMailMessage prepareSimpleMailMessage(final FeedbackRequest feedbackRequest) {
    var simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setSubject(feedbackRequest.getSubject());
    simpleMailMessage.setTo(feedbackRequest.getTo());
    simpleMailMessage.setSentDate(new Date());
    simpleMailMessage.setText(feedbackRequest.getMessage());
    simpleMailMessage.setReplyTo(feedbackRequest.getFrom());
    simpleMailMessage.setCc(feedbackRequest.getRecipients().toArray(new String[0]));

    // prepare the update the simpleMailMessage with senders name.
    configureInternetAddress(feedbackRequest, simpleMailMessage);
    return simpleMailMessage;
  }

  /**
   * Masks the email address with the senders name.
   *
   * @param feedback the feedback
   * @param mailMessage the mailMessage
   */
  private void configureInternetAddress(FeedbackRequest feedback, SimpleMailMessage mailMessage) {
    try {
      InternetAddress address = new InternetAddress(feedback.getEmail(), feedback.getName());
      mailMessage.setFrom(String.valueOf(address));
    } catch (UnsupportedEncodingException e) {
      LOG.error("Could not create an internet address for the feedback {}", feedback, e);
    }
  }
}
