package com.developersboard.backend.service.mail;

import com.developersboard.shared.dto.UserDto;
import com.developersboard.web.payload.request.mail.EmailRequest;
import com.developersboard.web.payload.request.mail.FeedbackRequest;
import com.developersboard.web.payload.request.mail.HtmlEmailRequest;
import org.springframework.mail.SimpleMailMessage;

/**
 * Provides operations on sending emails within the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public interface EmailService {

  /**
   * Sends an email with the provided simple mail message object.
   *
   * @param simpleMailMessage the simple mail message.
   */
  void sendMail(SimpleMailMessage simpleMailMessage);

  /**
   * Sends an email with the provided EmailRequestBuilder details.
   *
   * @param emailRequest the email format
   * @see EmailRequest
   */
  void sendHtmlEmail(HtmlEmailRequest emailRequest);

  /**
   * Sends an email with the provided details and template for html with an attachment.
   *
   * @param emailRequest the email format
   */
  void sendHtmlEmailWithAttachment(HtmlEmailRequest emailRequest);

  /**
   * Sends an email given a feedback Pojo.
   *
   * @param feedbackRequestBuilder the feedback pojo.
   * @see FeedbackRequest
   */
  void sendMailWithFeedback(FeedbackRequest feedbackRequestBuilder);

  /**
   * Sends an email to the provided user to verify account.
   *
   * @param userDto the user
   * @param token the token
   */
  void sendAccountVerificationEmail(UserDto userDto, String token);

  /**
   * Sends an email to the provided user to confirm account activation.
   *
   * @param userDto the user
   */
  void sendAccountConfirmationEmail(UserDto userDto);

  /**
   * Sends an email to the provided user to reset password.
   *
   * @param userDto the user
   * @param token the password token
   */
  void sendPasswordResetEmail(UserDto userDto, String token);

  /**
   * Send password reset confirmation email to user.
   *
   * @param userDto the user
   */
  void sendPasswordResetConfirmationEmail(final UserDto userDto);
}
