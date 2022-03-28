package com.developersboard.backend.service.mail.impl;

import com.developersboard.constant.EmailConstants;
import com.developersboard.constant.ProfileTypeConstants;
import com.developersboard.web.payload.request.mail.EmailRequest;
import com.developersboard.web.payload.request.mail.HtmlEmailRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

/**
 * SmtpEmailServiceImpl Class has the operation of email sending in a real time.
 *
 * @author Eric Opoku
 * @version 1.0
 * @see com.developersboard.backend.service.mail.EmailService
 * @since 1.0
 */
@Slf4j
@Service
@Profile({ProfileTypeConstants.PROD, ProfileTypeConstants.TEST})
public class SmtpEmailServiceImpl extends AbstractEmailServiceImpl {

  private final transient JavaMailSender mailSender;
  private final transient TemplateEngine templateEngine;

  /**
   * Controller to inject dependencies.
   *
   * @param mailSender the mail sender
   * @param templateEngine the template engine
   */
  public SmtpEmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
  }

  /**
   * Sends an email with the provided simple mail message object.
   *
   * @param simpleMailMessage the simple mail message.
   */
  @Override
  public void sendMail(SimpleMailMessage simpleMailMessage) {
    LOG.debug("Sending mail with content {}", simpleMailMessage);
    mailSender.send(simpleMailMessage);
    LOG.debug(EmailConstants.MAIL_SUCCESS_MESSAGE);
  }

  /**
   * Sends an email with the provided EmailRequestBuilder details.
   *
   * @param emailRequest the email format
   * @see EmailRequest
   */
  @Override
  public void sendHtmlEmail(HtmlEmailRequest emailRequest) {
    LOG.info("Template used is {}", templateEngine);
    LOG.debug("Sending html email with details {}", emailRequest);
    try {
      mailSender.send(prepareMimeMessage(emailRequest));
      LOG.debug(EmailConstants.MAIL_SUCCESS_MESSAGE);
    } catch (MessagingException | UnsupportedEncodingException e) {
      LOG.error("Unexpected exception sending an html email", e);
    }
  }

  /**
   * Prepares a MimeMessage with provided EmailFormat.
   *
   * @param emailFormat the emailFormat
   * @return MimeMessage the MimeMessage
   */
  private MimeMessage prepareMimeMessage(HtmlEmailRequest emailFormat)
      throws MessagingException, UnsupportedEncodingException {

    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper =
        new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
    helper.setTo(emailFormat.getTo());
    helper.setSentDate(new Date());
    // Set multiple recipients and cc them as needed
    if (Objects.nonNull(emailFormat.getRecipients()) && !emailFormat.getRecipients().isEmpty()) {
      helper.setCc(emailFormat.getRecipients().toArray(String[]::new));
    }
    String body = templateEngine.process(emailFormat.getTemplate(), emailFormat.getContext());
    helper.setText(body, true);
    helper.setSubject(emailFormat.getSubject());
    // set up the senders address with the given name
    setFromAndReplyTo(emailFormat, helper);
    return mimeMessage;
  }

  /**
   * Setup the senders address using Internet Address.
   *
   * @param emailFormat the email format
   * @param helper the mime message helper
   * @throws UnsupportedEncodingException if there is any issue with encoding
   * @throws MessagingException if there is any exception processing the message
   */
  private void setFromAndReplyTo(HtmlEmailRequest emailFormat, MimeMessageHelper helper)
      throws UnsupportedEncodingException, MessagingException {
    InternetAddress internetAddress;
    if (Objects.nonNull(emailFormat.getFrom()) && Objects.nonNull(emailFormat.getSender())) {
      internetAddress =
          new InternetAddress(emailFormat.getFrom(), emailFormat.getSender().getName());
    } else {
      internetAddress = new InternetAddress(emailFormat.getFrom(), "Starter App");
    }
    helper.setFrom(String.valueOf(internetAddress));
    helper.setReplyTo(internetAddress);

    if (Objects.nonNull(emailFormat.getReceiver())) {
      internetAddress =
          new InternetAddress(emailFormat.getTo(), emailFormat.getReceiver().getName());
      helper.setTo(internetAddress);
    }
    helper.setTo(internetAddress);
  }
}
