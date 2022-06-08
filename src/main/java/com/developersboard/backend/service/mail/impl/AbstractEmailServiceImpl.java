package com.developersboard.backend.service.mail.impl;

import com.developersboard.backend.service.mail.EmailService;
import com.developersboard.constant.EmailConstants;
import com.developersboard.constant.user.PasswordConstants;
import com.developersboard.constant.user.ProfileConstants;
import com.developersboard.constant.user.SignUpConstants;
import com.developersboard.constant.user.UserConstants;
import com.developersboard.exception.InvalidServiceRequestException;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.core.ValidationUtils;
import com.developersboard.shared.util.core.WebUtils;
import com.developersboard.web.payload.request.mail.FeedbackRequest;
import com.developersboard.web.payload.request.mail.HtmlEmailRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
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
   * Prepares a context with current content.
   *
   * @param emailRequest the emailRequest
   * @return emailRequest with context
   */
  public static HtmlEmailRequest prepareEmailRequest(final HtmlEmailRequest emailRequest) {
    var context = new Context();
    context.setVariable(EmailConstants.URLS, emailRequest.getUrls());
    context.setVariable(UserConstants.USERNAME, emailRequest.getReceiver().getUsername());
    emailRequest.setTo(emailRequest.getReceiver().getEmail());
    emailRequest.setSubject(emailRequest.getSubject());

    if (emailRequest.getUrls().containsKey(EmailConstants.EMAIL_LINK)) {
      context.setVariable(
          EmailConstants.EMAIL_LINK, emailRequest.getUrls().get(EmailConstants.EMAIL_LINK));
      LOG.info(emailRequest.getUrls().get(EmailConstants.EMAIL_LINK));
    }
    emailRequest.setContext(context);

    return emailRequest;
  }

  @Override
  public void sendMailWithFeedback(final FeedbackRequest feedbackRequest) {
    try {
      var simpleMailMessage = prepareSimpleMailMessage(feedbackRequest);
      sendMail(simpleMailMessage);
    } catch (UnsupportedEncodingException e) {
      throw new InvalidServiceRequestException(e);
    }
  }

  @Override
  public void sendAccountVerificationEmail(UserDto userDto, String token) {
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);

    var emailRequest =
        prepareHtmlEmailRequest(
            userDto,
            token,
            SignUpConstants.SIGN_UP_MAPPING + SignUpConstants.SIGN_UP_VERIFY_MAPPING,
            EmailConstants.EMAIL_VERIFY_TEMPLATE,
            EmailConstants.CONFIRMATION_PENDING_EMAIL_SUBJECT);
    // prepare the email request then send it.
    sendHtmlEmail(prepareEmailRequest(emailRequest));
  }

  @Override
  public void sendAccountConfirmationEmail(final UserDto userDto) {
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);

    HtmlEmailRequest emailRequest =
        prepareHtmlEmailRequest(
            userDto,
            null,
            ProfileConstants.PROFILE_MAPPING,
            EmailConstants.EMAIL_WELCOME_TEMPLATE,
            EmailConstants.CONFIRMATION_SUCCESS_EMAIL_SUBJECT);
    // prepare the email request then send it.
    sendHtmlEmail(prepareEmailRequest(emailRequest));
  }

  @Override
  public void sendPasswordResetEmail(final UserDto userDto, final String token) {
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);

    HtmlEmailRequest emailRequest =
        prepareHtmlEmailRequest(
            userDto,
            token,
            PasswordConstants.PASSWORD_RESET_ROOT_MAPPING + PasswordConstants.PASSWORD_CHANGE_PATH,
            EmailConstants.PASSWORD_RESET_TEMPLATE,
            EmailConstants.PASSWORD_RESET_EMAIL_SUBJECT);
    // prepare the email request then send it.
    sendHtmlEmail(prepareEmailRequest(emailRequest));
  }

  @Override
  public void sendPasswordResetConfirmationEmail(final UserDto userDto) {
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

    // get the links used in the email
    Map<String, String> links = WebUtils.getDefaultEmailUrls();
    if (StringUtils.isNotBlank(path) && StringUtils.isNotBlank(token)) {
      links.put(EmailConstants.EMAIL_LINK, WebUtils.getGenericUri(path, token));
    } else if (StringUtils.isNotBlank(path)) {
      links.put(EmailConstants.EMAIL_LINK, WebUtils.getGenericUri(path));
    }
    links.put(UserConstants.USERNAME, userDto.getUsername());

    var emailRequest = new HtmlEmailRequest();
    emailRequest.setTemplate(template);
    emailRequest.setUrls(links);
    emailRequest.setReceiver(userDto);
    emailRequest.setSubject(subject);

    return emailRequest;
  }

  /**
   * Prepares a simple mail message object with the feedback pojo details.
   *
   * @param feedbackRequest the feedback pojo.
   * @return the simple mail message.
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  private SimpleMailMessage prepareSimpleMailMessage(final FeedbackRequest feedbackRequest)
      throws UnsupportedEncodingException {
    LOG.debug("Preparing simpleMailMessage with feedback: {}", feedbackRequest);

    var simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setSubject(feedbackRequest.getSubject());
    simpleMailMessage.setTo(feedbackRequest.getTo());
    simpleMailMessage.setSentDate(new Date());
    simpleMailMessage.setText(feedbackRequest.getMessage());
    simpleMailMessage.setReplyTo(feedbackRequest.getEmail());
    simpleMailMessage.setCc(feedbackRequest.getRecipients().toArray(new String[0]));

    // prepare the update the simpleMailMessage with senders name.
    configureInternetAddress(feedbackRequest, simpleMailMessage);

    LOG.debug("Prepared simpleMailMessage: {}", simpleMailMessage);
    return simpleMailMessage;
  }

  /**
   * Masks the email address with the senders name.
   *
   * @param feedback the feedback
   * @param mailMessage the mailMessage
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  private void configureInternetAddress(FeedbackRequest feedback, SimpleMailMessage mailMessage)
      throws UnsupportedEncodingException {

    ValidationUtils.validateInputs(feedback, feedback.getEmail(), feedback.getName());

    var address = new InternetAddress(feedback.getEmail(), feedback.getName());
    LOG.debug("InternetAddress for feedback: {}", address);

    mailMessage.setFrom(String.valueOf(address));
  }

  /**
   * Embed all files in the emailRequest as attachments in the email.
   *
   * @param emailRequest the emailRequest
   * @param mimeMessageHelper the message helper
   * @throws MessagingException the messaging exception
   * @throws FileNotFoundException if the file is not found
   */
  protected void addAttachments(HtmlEmailRequest emailRequest, MimeMessageHelper mimeMessageHelper)
      throws MessagingException, FileNotFoundException {

    for (File attachment : emailRequest.getAttachments()) {
      addAttachment(attachment, mimeMessageHelper);
    }
  }

  /**
   * Embed an attachment to an email.
   *
   * @param file the file
   * @param mimeMessageHelper the message helper
   * @throws MessagingException the messaging exception
   * @throws FileNotFoundException if the file is not found
   */
  private void addAttachment(File file, MimeMessageHelper mimeMessageHelper)
      throws MessagingException, FileNotFoundException {

    if (!file.exists()) {
      LOG.error("File does not exist: {}", file);
      throw new FileNotFoundException("File does not exist: " + file);
    }
    mimeMessageHelper.addAttachment(file.getName(), file);
    LOG.debug("Added a file attachment: {}", file.getName());
  }
}
