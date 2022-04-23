package com.developersboard.backend.service.mail;

import com.developersboard.shared.dto.UserDto;
import com.developersboard.web.payload.request.mail.EmailRequest;
import com.developersboard.web.payload.request.mail.FeedbackRequest;
import com.developersboard.web.payload.request.mail.HtmlEmailRequest;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
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
   * @throws MessagingException the messaging exception
   * @throws UnsupportedEncodingException the unsupported encoding exception
   * @throws FileNotFoundException if the specified attachment file is not found
   */
  void sendHtmlEmail(HtmlEmailRequest emailRequest)
      throws MessagingException, UnsupportedEncodingException, FileNotFoundException;

  /**
   * Sends an email with the provided details and template for html with an attachment.
   *
   * @param emailRequest the email format
   * @throws MessagingException the messaging exception
   * @throws UnsupportedEncodingException the unsupported encoding exception
   * @throws FileNotFoundException if the specified attachment file is not found
   */
  void sendHtmlEmailWithAttachment(HtmlEmailRequest emailRequest)
      throws MessagingException, UnsupportedEncodingException, FileNotFoundException;

  /**
   * Sends an email given a feedback Pojo.
   *
   * @param feedbackRequestBuilder the feedback pojo.
   * @see FeedbackRequest
   */
  void sendMailWithFeedback(FeedbackRequest feedbackRequestBuilder)
      throws UnsupportedEncodingException;

  /**
   * Sends an email to the provided user to verify account.
   *
   * @param userDto the user
   * @param token the token
   */
  void sendAccountVerificationEmail(UserDto userDto, String token)
      throws MessagingException, UnsupportedEncodingException, FileNotFoundException;

  /**
   * Sends an email to the provided user to confirm account activation.
   *
   * @param userDto the user
   */
  void sendAccountConfirmationEmail(UserDto userDto)
      throws MessagingException, UnsupportedEncodingException, FileNotFoundException;

  /**
   * Sends an email to the provided user to reset password.
   *
   * @param userDto the user
   * @param token the password token
   */
  void sendPasswordResetEmail(UserDto userDto, String token)
      throws MessagingException, UnsupportedEncodingException, FileNotFoundException;

  /**
   * Send password reset confirmation email to user.
   *
   * @param userDto the user
   */
  void sendPasswordResetConfirmationEmail(final UserDto userDto)
      throws MessagingException, UnsupportedEncodingException, FileNotFoundException;
}
