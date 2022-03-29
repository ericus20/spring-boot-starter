package com.developersboard.backend.service.mail.impl;

import com.developersboard.constant.EmailConstants;
import com.developersboard.constant.ProfileTypeConstants;
import com.developersboard.web.payload.request.mail.EmailRequest;
import com.developersboard.web.payload.request.mail.HtmlEmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Class simulates the operation of email sending without a real time call.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@Profile(ProfileTypeConstants.DEV)
public class MockEmailServiceImpl extends AbstractEmailServiceImpl {

  /**
   * Sends an email with the provided simple mail message object.
   *
   * @param simpleMailMessage the simple mail message.
   */
  @Override
  public void sendMail(SimpleMailMessage simpleMailMessage) {
    LOG.info(EmailConstants.SIMULATING_SENDING_AN_EMAIL);
    LOG.info("Simple Mail Message content is {}", simpleMailMessage);
    LOG.info(EmailConstants.MAIL_SUCCESS_MESSAGE);
  }

  /**
   * Sends an email with the provided EmailRequestBuilder details.
   *
   * @param emailRequest the email format
   * @see EmailRequest
   */
  @Override
  public void sendHtmlEmail(HtmlEmailRequest emailRequest) {
    LOG.info(EmailConstants.SIMULATING_SENDING_AN_EMAIL);
    LOG.info("Email request details include: {}", emailRequest);
    LOG.info(EmailConstants.MAIL_SUCCESS_MESSAGE);
  }

  /**
   * Sends an email with the provided details and template for html with an attachment.
   *
   * @param emailRequest the email format
   */
  @Override
  public void sendHtmlEmailWithAttachment(HtmlEmailRequest emailRequest) {
    LOG.info(EmailConstants.SIMULATING_SENDING_AN_EMAIL);
    LOG.info("attachments to be emailed are {}", emailRequest.getAttachments());
    LOG.info(EmailConstants.MAIL_SUCCESS_MESSAGE);
  }
}
