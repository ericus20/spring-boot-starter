package com.developersboard.backend.service.mail;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.constant.EmailConstants;
import com.developersboard.exception.InvalidServiceRequestException;
import com.developersboard.shared.util.UserUtils;
import com.developersboard.shared.util.core.WebUtils;
import com.developersboard.web.payload.request.mail.FeedbackRequest;
import com.developersboard.web.payload.request.mail.HtmlEmailRequest;
import com.icegreen.greenmail.util.GreenMailUtil;
import java.io.File;
import java.util.Collections;
import java.util.Objects;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmailServiceIntegrationTest extends IntegrationTestUtils {

  public static final String PROFILE_IMAGE_JPEG = "/profileImage.jpeg";

  private transient String body;
  private transient String subject;
  private transient String sender;
  private transient String recipient;

  @BeforeEach
  void setUp() {
    greenMail.start();
    sender = StringUtils.EMPTY;
    recipient = StringUtils.EMPTY;
  }

  @BeforeAll
  void setup() {
    // Use random content to avoid potential residual lingering problems
    subject = GreenMailUtil.random();
    body = GreenMailUtil.random();
  }

  @AfterEach
  void tearDown() {
    greenMail.stop();
  }

  @Test
  void sendEmail() {

    var message = new SimpleMailMessage();
    message.setText(body);
    message.setSubject(subject);
    message.setTo(FAKER.internet().emailAddress());
    message.setFrom(FAKER.internet().emailAddress());
    sender = message.getFrom();
    recipient = Objects.requireNonNull(message.getTo())[0];
    subject = message.getSubject();

    emailService.sendMail(message);

    // Retrieve using GreenMail API
    var messages = greenMail.getReceivedMessages();
    Assertions.assertEquals(body, GreenMailUtil.getBody(messages[0]).trim());

    assertEmailResponse(false);
  }

  @Test
  void sendEmailWithFeedback() {

    var feedback = new FeedbackRequest();
    feedback.setMessage(body);
    feedback.setSubject(subject);
    feedback.setName(FAKER.name().fullName());
    // This is a feedback from a user to an internal email. (usually from contact page)
    feedback.setTo(systemProperties.getEmail());
    feedback.setEmail(FAKER.internet().emailAddress());

    sender = feedback.getEmail();
    recipient = feedback.getTo();
    subject = feedback.getSubject();

    emailService.sendMailWithFeedback(feedback);

    // Retrieve using GreenMail API
    Message[] messages = greenMail.getReceivedMessages();
    Assertions.assertEquals(body, GreenMailUtil.getBody(messages[0]).trim());

    assertEmailResponse(false);
  }

  @Test
  void sendHtmlEmail() {

    var userDto = UserUtils.createUserDto(false);
    var links = WebUtils.getDefaultEmailUrls();

    var emailRequest = new HtmlEmailRequest();
    emailRequest.setUrls(links);
    emailRequest.setSender(userDto);
    emailRequest.setSubject(subject);
    emailRequest.setReceiver(userDto);
    emailRequest.setTo(userDto.getEmail());
    emailRequest.setFrom(userDto.getEmail());
    emailRequest.getRecipients().add(FAKER.internet().emailAddress());
    emailRequest.setTemplate(EmailConstants.EMAIL_WELCOME_TEMPLATE);

    sender = emailRequest.getFrom();
    recipient = emailRequest.getTo();
    subject = emailRequest.getSubject();

    emailService.sendHtmlEmail(emailRequest);

    assertEmailResponse(false, 2);
  }

  @Test
  void sendHtmlEmailWithInvalidAttachmentThrowsException() {
    var file = new File(StringUtils.EMPTY);

    var userDto = UserUtils.createUserDto(false);
    var links = WebUtils.getDefaultEmailUrls();

    var emailRequest = new HtmlEmailRequest();
    emailRequest.setUrls(links);
    emailRequest.setSubject(subject);
    emailRequest.setFrom(userDto.getEmail());
    emailRequest.setTo(userDto.getEmail());
    emailRequest.setAttachments(Collections.singleton(file));
    emailRequest.setTemplate(EmailConstants.EMAIL_WELCOME_TEMPLATE);

    sender = emailRequest.getFrom();
    recipient = emailRequest.getTo();
    subject = emailRequest.getSubject();

    Assertions.assertThrows(
        InvalidServiceRequestException.class,
        () -> emailService.sendHtmlEmailWithAttachment(emailRequest));
  }

  @Test
  void sendHtmlEmailWithAttachment() throws Exception {

    var uploadFileResource = new ClassPathResource(PROFILE_IMAGE_JPEG, getClass());
    var file = uploadFileResource.getFile();
    Assertions.assertTrue(file.exists());
    Assertions.assertNotNull(file);

    var userDto = UserUtils.createUserDto(true);
    var links = WebUtils.getDefaultEmailUrls();

    var emailRequest = new HtmlEmailRequest();
    emailRequest.setUrls(links);
    emailRequest.setSubject(subject);
    emailRequest.setTo(userDto.getEmail());
    emailRequest.setFrom(userDto.getEmail());
    emailRequest.setAttachments(Collections.singleton(file));
    emailRequest.setTemplate(EmailConstants.EMAIL_WELCOME_TEMPLATE);

    sender = emailRequest.getFrom();
    recipient = emailRequest.getTo();
    subject = emailRequest.getSubject();

    emailService.sendHtmlEmailWithAttachment(emailRequest);

    assertEmailResponse(true);
  }

  @Test
  void sendAccountVerificationEmail() {

    var userDto = UserUtils.createUserDto(false);
    var token = jwtService.generateJwtToken(userDto.getUsername());
    subject = EmailConstants.CONFIRMATION_PENDING_EMAIL_SUBJECT;
    recipient = userDto.getEmail();

    // Send email verification
    emailService.sendAccountVerificationEmail(userDto, token);
    assertEmailResponse(false);
  }

  @Test
  void sendAccountConfirmationEmail() {

    var userDto = UserUtils.createUserDto(false);
    subject = EmailConstants.CONFIRMATION_SUCCESS_EMAIL_SUBJECT;
    recipient = userDto.getEmail();

    // Send email verification
    emailService.sendAccountConfirmationEmail(userDto);
    assertEmailResponse(false);
  }

  @Test
  void sendPasswordResetEmail() {

    var userDto = UserUtils.createUserDto(false);
    var token = jwtService.generateJwtToken(userDto.getUsername());
    subject = EmailConstants.PASSWORD_RESET_EMAIL_SUBJECT;
    recipient = userDto.getEmail();

    // Send email verification
    emailService.sendPasswordResetEmail(userDto, token);
    assertEmailResponse(false);
  }

  @Test
  void sendPasswordResetConfirmationEmail() {

    var userDto = UserUtils.createUserDto(false);
    subject = EmailConstants.PASSWORD_RESET_SUCCESS_SUBJECT;
    recipient = userDto.getEmail();

    // Send email verification
    emailService.sendPasswordResetConfirmationEmail(userDto);
    assertEmailResponse(false);
  }

  /**
   * Asserts the email response.
   *
   * @param isMultipart {@code true} if the email is multipart, {@code false} otherwise
   */
  private void assertEmailResponse(boolean isMultipart) {
    assertEmailResponse(isMultipart, 1);
  }

  /**
   * Asserts the email response.
   *
   * @param isMultipart {@code true} if the email is multipart, {@code false} otherwise
   * @param numberOfMessages the number of messages
   */
  private void assertEmailResponse(boolean isMultipart, int numberOfMessages) {
    // Retrieve using GreenMail API
    Message[] messages = greenMail.getReceivedMessages();

    // If no sender is specified, then the email must be from the system.
    if (StringUtils.isBlank(sender)) {
      sender = systemProperties.getEmail();
    }

    if (StringUtils.isBlank(recipient)) {
      recipient = systemProperties.getEmail();
    }

    Assertions.assertAll(
        () -> {
          Assertions.assertEquals(numberOfMessages, messages.length);
          Assertions.assertEquals(subject, messages[0].getSubject());

          // The email is reformatted as internet address Example <"John Doe" <johndoe@hotmail.com>>
          // We want to check that the email is part of the string returned
          Assertions.assertTrue(messages[0].getFrom()[0].toString().contains(sender));
          Assertions.assertTrue(
              messages[0].getRecipients(RecipientType.TO)[0].toString().contains(recipient));

          Assertions.assertEquals(isMultipart, messages[0].getContent() instanceof MimeMultipart);
        });
  }
}
